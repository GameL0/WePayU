package br.ufal.ic.p2.wepayu.exception.negativo;

public class HorasNaoPositivasException extends ValorNegativoException {
    public HorasNaoPositivasException() {
        super("Horas devem ser positivas.");
    }
}
