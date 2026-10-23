package br.ufal.ic.p2.wepayu.exception.nulo;

public class TaxaSindicalNulaException extends ValorNuloException {
    public TaxaSindicalNulaException() {
        super("Taxa sindical nao pode ser nula.");
    }
}
