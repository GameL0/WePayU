package br.ufal.ic.p2.wepayu.persistencia;

import br.ufal.ic.p2.wepayu.exception.persistencia.CarregarDadosException;
import br.ufal.ic.p2.wepayu.exception.persistencia.SalvarDadosException;
import br.ufal.ic.p2.wepayu.models.Assalariado;
import br.ufal.ic.p2.wepayu.models.Banco;
import br.ufal.ic.p2.wepayu.models.CartaoDePonto;
import br.ufal.ic.p2.wepayu.models.Comissionado;
import br.ufal.ic.p2.wepayu.models.Historico;
import br.ufal.ic.p2.wepayu.models.Horista;
import br.ufal.ic.p2.wepayu.models.MembroSindicato;
import br.ufal.ic.p2.wepayu.models.TaxaServico;
import br.ufal.ic.p2.wepayu.models.Venda;
import br.ufal.ic.p2.wepayu.models.agenda.AgendaMensal;
import br.ufal.ic.p2.wepayu.models.agenda.AgendaMensalDiaFixo;
import br.ufal.ic.p2.wepayu.models.agenda.AgendaPagamento;
import br.ufal.ic.p2.wepayu.models.agenda.AgendaSemanal;
import br.ufal.ic.p2.wepayu.models.agenda.FabricaAgenda;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Persistencia {
    private final String caminhoArquivo;

    public Persistencia(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public void salvar(DadosSistema dados) throws SalvarDadosException {
        try (OutputStream saida = new BufferedOutputStream(new FileOutputStream(caminhoArquivo))) {
            escrever(dados, saida);
        } catch (IOException e) {
            throw new SalvarDadosException(e);
        }
    }

    public DadosSistema carregar() throws CarregarDadosException {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            return new DadosSistema();
        }
        try (InputStream entrada = new BufferedInputStream(new FileInputStream(arquivo))) {
            return ler(entrada);
        } catch (IOException e) {
            throw new CarregarDadosException(e);
        }
    }

    public byte[] serializar(DadosSistema dados) {
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        escrever(dados, saida);
        return saida.toByteArray();
    }

    public DadosSistema desserializar(byte[] bytes) {
        return ler(new ByteArrayInputStream(bytes));
    }

    private void escrever(DadosSistema dados, OutputStream saida) {
        try (XMLEncoder encoder = new XMLEncoder(saida)) {
            encoder.setPersistenceDelegate(BigDecimal.class, new RecriarPorTexto(BigDecimal.class, "new"));
            encoder.setPersistenceDelegate(LocalDate.class, new RecriarPorTexto(LocalDate.class, "parse"));
            PersistenceDelegate agenda = new RecriarAgenda();
            encoder.setPersistenceDelegate(AgendaSemanal.class, agenda);
            encoder.setPersistenceDelegate(AgendaMensal.class, agenda);
            encoder.setPersistenceDelegate(AgendaMensalDiaFixo.class, agenda);
            encoder.setPersistenceDelegate(Assalariado.class, porConstrutor("nome", "endereco", "salario"));
            encoder.setPersistenceDelegate(Horista.class, porConstrutor("nome", "endereco", "salario"));
            encoder.setPersistenceDelegate(Comissionado.class, porConstrutor("nome", "endereco", "salario", "comissao"));
            encoder.setPersistenceDelegate(Historico.class, porConstrutor("cartoes", "vendas", "divida", "dividaAntesDaUltimaFolha", "dataUltimaFolha"));
            encoder.setPersistenceDelegate(CartaoDePonto.class, porConstrutor("data", "horas"));
            encoder.setPersistenceDelegate(Venda.class, porConstrutor("data", "valor"));
            encoder.setPersistenceDelegate(TaxaServico.class, porConstrutor("data", "valor"));
            encoder.setPersistenceDelegate(MembroSindicato.class, porConstrutor("idSindicato", "taxaSindical", "taxasServico"));
            encoder.setPersistenceDelegate(Banco.class, porConstrutor("banco", "agencia", "contaCorrente"));
            encoder.writeObject(dados);
        }
    }

    private static PersistenceDelegate porConstrutor(String... propriedades) {
        return new RecriarPorConstrutor(propriedades);
    }

    private DadosSistema ler(InputStream entrada) {
        try (XMLDecoder decoder = new XMLDecoder(entrada)) {
            return (DadosSistema) decoder.readObject();
        }
    }

    private static class RecriarPorTexto extends PersistenceDelegate {
        private final Class<?> classe;
        private final String metodo;

        RecriarPorTexto(Class<?> classe, String metodo) {
            this.classe = classe;
            this.metodo = metodo;
        }

        @Override
        protected Expression instantiate(Object antigo, Encoder encoder) {
            return new Expression(antigo, classe, metodo, new Object[]{antigo.toString()});
        }

        @Override
        protected boolean mutatesTo(Object antigo, Object novo) {
            return novo != null && antigo.toString().equals(novo.toString());
        }
    }

    private static class RecriarPorConstrutor extends DefaultPersistenceDelegate {
        private final String[] propriedades;

        RecriarPorConstrutor(String... propriedades) {
            super(propriedades);
            this.propriedades = propriedades;
        }

        @Override
        protected boolean mutatesTo(Object antigo, Object novo) {
            return super.mutatesTo(antigo, novo) && temMesmosValores(antigo, novo);
        }

        private boolean temMesmosValores(Object antigo, Object novo) {
            for (String propriedade : propriedades) {
                if (!Objects.equals(ler(antigo, propriedade), ler(novo, propriedade))) {
                    return false;
                }
            }
            return true;
        }

        private static Object ler(Object alvo, String propriedade) {
            String getter = "get" + Character.toUpperCase(propriedade.charAt(0)) + propriedade.substring(1);
            try {
                return new Expression(alvo, getter, new Object[0]).getValue();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private static class RecriarAgenda extends PersistenceDelegate {
        @Override
        protected Expression instantiate(Object antigo, Encoder encoder) {
            return new Expression(antigo, FabricaAgenda.class, "interpretar", new Object[]{descricaoDe(antigo)});
        }

        @Override
        protected boolean mutatesTo(Object antigo, Object novo) {
            return novo != null && descricaoDe(antigo).equals(descricaoDe(novo));
        }

        private static String descricaoDe(Object agenda) {
            return ((AgendaPagamento) agenda).descricao();
        }
    }
}
