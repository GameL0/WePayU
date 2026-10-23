package br.ufal.ic.p2.wepayu.conversao;

import br.ufal.ic.p2.wepayu.exception.WePayUException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Supplier;

public final class ConversorNumero {
    private ConversorNumero() {
    }

    public static <N extends WePayUException, M extends WePayUException> BigDecimal converter(String texto, Supplier<N> nulo, Supplier<M> naoNumerico) throws N, M {
        if (texto == null || texto.isEmpty()) {
            throw nulo.get();
        }
        try {
            return new BigDecimal(texto.replace(",", "."));
        } catch (NumberFormatException e) {
            throw naoNumerico.get();
        }
    }

    public static <M extends WePayUException> BigDecimal converter(String texto, Supplier<M> naoNumerico) throws M {
        return converter(texto, naoNumerico, naoNumerico);
    }

    public static <M extends WePayUException> int converterInteiro(String texto, Supplier<M> naoNumerico) throws M {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw naoNumerico.get();
        }
    }

    public static String formatarMoeda(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP).toPlainString().replace(".", ",");
    }

    public static String formatarHoras(BigDecimal horas) {
        return horas.stripTrailingZeros().toPlainString().replace(".", ",");
    }
}
