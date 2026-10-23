package br.ufal.ic.p2.wepayu.exception.nulo;

public class IdentificacaoNulaException extends ValorNuloException {
    public IdentificacaoNulaException() {
        super("Identificacao do empregado nao pode ser nula.");
    }
}
