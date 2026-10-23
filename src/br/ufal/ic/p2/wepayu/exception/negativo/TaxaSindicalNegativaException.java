package br.ufal.ic.p2.wepayu.exception.negativo;

public class TaxaSindicalNegativaException extends ValorNegativoException {
    public TaxaSindicalNegativaException() {
        super("Taxa sindical deve ser nao-negativa.");
    }
}
