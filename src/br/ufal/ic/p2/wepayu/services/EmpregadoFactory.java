package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.exception.tipo.TipoInvalidoException;
import br.ufal.ic.p2.wepayu.exception.tipo.TipoNaoAplicavelException;
import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.models.Assalariado;
import br.ufal.ic.p2.wepayu.models.Comissionado;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.Horista;

import java.math.BigDecimal;

public class EmpregadoFactory {

    public static Empregado criar(String nome, String endereco, String tipo, BigDecimal salario) throws WePayUException {
        switch (tipo) {
            case "assalariado": return new Assalariado(nome, endereco, salario);
            case "horista": return new Horista(nome, endereco, salario);
            case "comissionado": throw new TipoNaoAplicavelException();
            default: throw new TipoInvalidoException();
        }

    }

    public static Empregado criar(String nome, String endereco, String tipo, BigDecimal salario, BigDecimal comissao) throws WePayUException {
        switch (tipo) {
            case "assalariado": throw new TipoNaoAplicavelException();
            case "horista": throw new TipoNaoAplicavelException();
            case "comissionado": return new Comissionado(nome, endereco, salario, comissao);
            default: throw new TipoInvalidoException();
        }
    }
}
