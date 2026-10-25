package br.ufal.ic.p2.wepayu.models;

import java.time.LocalDate;

public abstract class Lancamento {
    private final LocalDate data;

    protected Lancamento(LocalDate data) {
        this.data = data;
    }

    public boolean estaEm(Periodo periodo) {
        return periodo.contem(data);
    }

    public LocalDate getData() {
        return data;
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (outro == null || getClass() != outro.getClass()) {
            return false;
        }
        return data.equals(((Lancamento) outro).data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }
}
