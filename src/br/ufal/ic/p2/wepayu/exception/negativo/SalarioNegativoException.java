package br.ufal.ic.p2.wepayu.exception.negativo;

public class SalarioNegativoException extends ValorNegativoException {
    public SalarioNegativoException() {
        super("Salario deve ser nao-negativo.");
    }
}
