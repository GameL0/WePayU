package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.exception.sindicato.EmpregadoNaoEhSindicalizadoException;

import java.math.BigDecimal;

public abstract class Filiacao {
    public abstract boolean ehSindicalizado();

    public abstract Filiacao sindicalizar(String idSindicato, BigDecimal taxaSindical) throws WePayUException;

    public boolean temIdSindicato(String idSindicato) {
        return false;
    }

    public MembroSindicato comoMembro() throws EmpregadoNaoEhSindicalizadoException {
        throw new EmpregadoNaoEhSindicalizadoException();
    }
}
