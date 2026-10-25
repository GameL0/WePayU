package br.ufal.ic.p2.wepayu.exception.nulo;

public class ContaCorrenteNulaException extends ValorNuloException {
    public ContaCorrenteNulaException() {
        super("Conta corrente nao pode ser nulo.");
    }
}
