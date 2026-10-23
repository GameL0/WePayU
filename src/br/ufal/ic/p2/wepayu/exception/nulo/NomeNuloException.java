package br.ufal.ic.p2.wepayu.exception.nulo;

public class NomeNuloException extends ValorNuloException {
    public NomeNuloException() {
        super("Nome nao pode ser nulo.");
    }
}
