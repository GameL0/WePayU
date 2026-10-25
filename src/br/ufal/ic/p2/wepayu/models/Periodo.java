package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.data.DataInicialPosteriorException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

    public static Periodo terminandoEm(LocalDate ultimoDia, LocalDate diaAposAnterior) {
        try {
            return new Periodo(diaAposAnterior, ultimoDia.plusDays(1));
        } catch (DataInicialPosteriorException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean contem(LocalDate data) {
        return !data.isBefore(inicio) && data.isBefore(fim);
    }

    public long dias() {
        return ChronoUnit.DAYS.between(inicio, fim);
    }
}
