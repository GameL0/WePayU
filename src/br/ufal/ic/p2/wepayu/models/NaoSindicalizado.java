package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.WePayUException;

import java.math.BigDecimal;

public class NaoSindicalizado extends Filiacao {
    @Override
    public boolean ehSindicalizado() {
        return false;
    }

    @Override
    public BigDecimal descontos(Periodo periodo) {
        return BigDecimal.ZERO;
    }

    @Override
    public Filiacao sindicalizar(String idSindicato, BigDecimal taxaSindical) throws WePayUException {
        return new MembroSindicato(idSindicato, taxaSindical);
    }
}
