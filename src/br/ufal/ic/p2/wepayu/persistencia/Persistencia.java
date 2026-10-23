package br.ufal.ic.p2.wepayu.persistencia;

import br.ufal.ic.p2.wepayu.exception.persistencia.CarregarDadosException;
import br.ufal.ic.p2.wepayu.exception.persistencia.SalvarDadosException;

import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Persistencia {
    private final String caminhoArquivo;

    public Persistencia(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public void salvar(DadosSistema dados) throws SalvarDadosException {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(caminhoArquivo)))) {
            encoder.setPersistenceDelegate(BigDecimal.class, new BigDecimalDelegate());
            encoder.setPersistenceDelegate(LocalDate.class, new LocalDateDelegate());
            encoder.writeObject(dados);
        } catch (FileNotFoundException e) {
            throw new SalvarDadosException(e);
        }
    }

    public DadosSistema carregar() throws CarregarDadosException {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            return new DadosSistema();
        }
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(arquivo)))) {
            return (DadosSistema) decoder.readObject();
        } catch (FileNotFoundException e) {
            throw new CarregarDadosException(e);
        }
    }

    private static class BigDecimalDelegate extends PersistenceDelegate {
        @Override
        protected Expression instantiate(Object antigo, Encoder encoder) {
            return new Expression(antigo, BigDecimal.class, "new", new Object[]{antigo.toString()});
        }
    }

    private static class LocalDateDelegate extends PersistenceDelegate {
        @Override
        protected Expression instantiate(Object antigo, Encoder encoder) {
            return new Expression(antigo, LocalDate.class, "parse", new Object[]{antigo.toString()});
        }
    }
}
