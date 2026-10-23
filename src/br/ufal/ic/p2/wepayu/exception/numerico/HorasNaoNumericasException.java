package br.ufal.ic.p2.wepayu.exception.numerico;

public class HorasNaoNumericasException extends ValorNaoNumericoException {
    public HorasNaoNumericasException() {
        super("Horas devem ser numericas.");
    }
}
