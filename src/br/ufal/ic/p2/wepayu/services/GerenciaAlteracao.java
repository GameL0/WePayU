package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.conversao.ConversorBooleano;
import br.ufal.ic.p2.wepayu.conversao.ConversorNumero;
import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.exception.naoencontrado.AtributoNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.nulo.BancoNuloException;
import br.ufal.ic.p2.wepayu.exception.nulo.ComissaoNulaException;
import br.ufal.ic.p2.wepayu.exception.nulo.IdentificacaoSindicatoNulaException;
import br.ufal.ic.p2.wepayu.exception.nulo.SalarioNuloException;
import br.ufal.ic.p2.wepayu.exception.numerico.ComissaoNaoNumericoException;
import br.ufal.ic.p2.wepayu.exception.numerico.SalarioNaoNumericoException;
import br.ufal.ic.p2.wepayu.exception.pagamento.MetodoPagamentoInvalidoException;
import br.ufal.ic.p2.wepayu.models.Banco;
import br.ufal.ic.p2.wepayu.models.Correios;
import br.ufal.ic.p2.wepayu.models.EmMaos;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.MetodoPagamento;

import java.math.BigDecimal;

public class GerenciaAlteracao {
    private final GerenciaEmpregado empregados;
    private final GerenciaAgenda agendas;

    public GerenciaAlteracao(GerenciaEmpregado empregados, GerenciaAgenda agendas) {
        this.empregados = empregados;
        this.agendas = agendas;
    }

    public void alterar(String id, String atributo, String valor) throws WePayUException {
        Empregado empregado = empregados.buscar(id);
        switch (atributo) {
            case "nome":
                empregado.alterarNome(valor);
                break;
            case "endereco":
                empregado.alterarEndereco(valor);
                break;
            case "salario":
                empregado.alterarSalario(ConversorNumero.converter(valor, SalarioNuloException::new, SalarioNaoNumericoException::new));
                break;
            case "comissao":
                alterarComissao(empregado, valor);
                break;
            case "tipo":
                trocarTipo(id, empregado, EmpregadoFactory.criar(empregado.getNome(), empregado.getEndereco(), valor, empregado.getSalario()));
                break;
            case "metodoPagamento":
                empregado.setMetodoPagamento(metodoSemDadosBancarios(valor));
                break;
            case "agendaPagamento":
                empregado.setAgendaPagamento(agendas.buscarDisponivel(valor));
                break;
            case "sindicalizado":
                desfiliar(empregado, valor);
                break;
            default:
                throw new AtributoNaoExisteException();
        }
    }

    public void alterar(String id, String atributo, String tipo, String valorExtra) throws WePayUException {
        Empregado empregado = empregados.buscar(id);
        if (!"tipo".equals(atributo)) {
            throw new AtributoNaoExisteException();
        }
        Empregado novo = EmpregadoFactory.criarComValorExtra(empregado.getNome(), empregado.getEndereco(), tipo, empregado.getSalario(), valorExtra);
        trocarTipo(id, empregado, novo);
    }

    public void alterar(String id, String atributo, String metodo, String banco, String agencia, String contaCorrente) throws WePayUException {
        Empregado empregado = empregados.buscar(id);
        if (!"metodoPagamento".equals(atributo)) {
            throw new AtributoNaoExisteException();
        }
        if (!"banco".equals(metodo)) {
            throw new MetodoPagamentoInvalidoException();
        }
        empregado.setMetodoPagamento(new Banco(banco, agencia, contaCorrente));
    }

    private void alterarComissao(Empregado empregado, String valor) throws WePayUException {
        BigDecimal comissao = ConversorNumero.converter(valor, ComissaoNulaException::new, ComissaoNaoNumericoException::new);
        empregado.comoComissionado().alterarComissao(comissao);
    }

    private void trocarTipo(String id, Empregado atual, Empregado novo) {
        novo.herdarDadosDe(atual);
        empregados.substituir(id, novo);
    }

    private void desfiliar(Empregado empregado, String valor) throws WePayUException {
        if (ConversorBooleano.converter(valor)) {
            throw new IdentificacaoSindicatoNulaException();
        }
        empregado.desfiliar();
    }

    private MetodoPagamento metodoSemDadosBancarios(String metodo) throws WePayUException {
        switch (metodo) {
            case "emMaos": return new EmMaos();
            case "correios": return new Correios();
            case "banco": throw new BancoNuloException();
            default: throw new MetodoPagamentoInvalidoException();
        }
    }
}
