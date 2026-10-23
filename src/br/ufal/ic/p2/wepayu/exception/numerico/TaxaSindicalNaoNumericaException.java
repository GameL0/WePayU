package br.ufal.ic.p2.wepayu.exception.numerico;

public class TaxaSindicalNaoNumericaException extends ValorNaoNumericoException {
    public TaxaSindicalNaoNumericaException() {
        super("Taxa sindical deve ser numerica.");
    }
}
