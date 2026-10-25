package br.ufal.ic.p2.wepayu.exception.nulo;

public class AgenciaNulaException extends ValorNuloException {
    public AgenciaNulaException() {
        super("Agencia nao pode ser nulo.");
    }
}
