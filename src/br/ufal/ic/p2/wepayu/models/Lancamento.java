package br.ufal.ic.p2.wepayu.models;

import java.time.LocalDate;

public abstract class Lancamento {
    private LocalDate data;

    protected Lancamento() {
    }

    protected Lancamento(LocalDate data) {
        this.data = data;
    }

    public boolean estaEm(Periodo periodo) {
        return periodo.contem(data);
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
