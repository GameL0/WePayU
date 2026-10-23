package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.data.DataInicialPosteriorException;

import java.time.LocalDate;

public final class Periodo {
    private final LocalDate inicio;
    private final LocalDate fim;

    public Periodo(LocalDate inicio, LocalDate fim) throws DataInicialPosteriorException {
        if (inicio.isAfter(fim)) {
            throw new DataInicialPosteriorException();
        }
        this.inicio = inicio;
        this.fim = fim;
    }

    public boolean contem(LocalDate data) {
        return !data.isBefore(inicio) && data.isBefore(fim);
    }
}
