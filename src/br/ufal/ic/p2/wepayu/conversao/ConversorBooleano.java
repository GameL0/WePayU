package br.ufal.ic.p2.wepayu.conversao;

import br.ufal.ic.p2.wepayu.exception.invalido.ValorBooleanoInvalidoException;

public final class ConversorBooleano {
    private ConversorBooleano() {
    }

    public static boolean converter(String texto) throws ValorBooleanoInvalidoException {
        if ("true".equals(texto)) {
            return true;
        }
        if ("false".equals(texto)) {
            return false;
        }
        throw new ValorBooleanoInvalidoException();
    }
}
