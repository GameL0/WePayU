package br.ufal.ic.p2.wepayu.conversao;

import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.exception.data.DataException;
import br.ufal.ic.p2.wepayu.exception.data.DataFinalInvalidaException;
import br.ufal.ic.p2.wepayu.exception.data.DataInicialInvalidaException;
import br.ufal.ic.p2.wepayu.models.Periodo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.function.Supplier;

public final class ConversorData {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("d/M/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private ConversorData() {
    }

    public static <E extends WePayUException> LocalDate converter(String texto, Supplier<E> erro) throws E {
        if (texto == null) {
            throw erro.get();
        }
        try {
            return LocalDate.parse(texto, FORMATO);
        } catch (DateTimeParseException e) {
            throw erro.get();
        }
    }

    public static Periodo converterPeriodo(String dataInicial, String dataFinal) throws DataException {
        LocalDate inicio = converter(dataInicial, DataInicialInvalidaException::new);
        LocalDate fim = converter(dataFinal, DataFinalInvalidaException::new);
        return new Periodo(inicio, fim);
    }
}
