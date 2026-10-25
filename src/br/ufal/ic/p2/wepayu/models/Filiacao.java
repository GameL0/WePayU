package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.exception.sindicato.EmpregadoNaoEhSindicalizadoException;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Filiacao {
    public abstract boolean ehSindicalizado();

    public abstract Filiacao sindicalizar(String idSindicato, BigDecimal taxaSindical) throws WePayUException;

    public abstract BigDecimal descontos(Periodo periodo);

    public boolean temIdSindicato(String idSindicato) {
        return false;
    }

    public String getAtributo(String atributo) throws WePayUException {
        throw new EmpregadoNaoEhSindicalizadoException();
    }

    public void lancaTaxaServico(LocalDate data, BigDecimal valor) throws WePayUException {
        throw new EmpregadoNaoEhSindicalizadoException();
    }

    public BigDecimal totalTaxasServico(Periodo periodo) throws EmpregadoNaoEhSindicalizadoException {
        throw new EmpregadoNaoEhSindicalizadoException();
    }
}
