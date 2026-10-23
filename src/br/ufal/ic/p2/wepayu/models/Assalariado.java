package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.negativo.ValorNegativoException;
import br.ufal.ic.p2.wepayu.exception.nulo.ValorNuloException;

import java.math.BigDecimal;

public class Assalariado extends Empregado {
    public Assalariado() {
    }

    public Assalariado(String nome, String endereco, BigDecimal salario) throws ValorNuloException, ValorNegativoException {
        super(nome, endereco, salario);
    }

    @Override
    public String getTipo() {
        return "assalariado";
    }
}
