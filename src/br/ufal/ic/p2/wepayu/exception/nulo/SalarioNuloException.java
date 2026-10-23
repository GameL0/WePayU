package br.ufal.ic.p2.wepayu.exception.nulo;

public class SalarioNuloException extends ValorNuloException {
    public SalarioNuloException() {
        super("Salario nao pode ser nulo.");
    }
}
