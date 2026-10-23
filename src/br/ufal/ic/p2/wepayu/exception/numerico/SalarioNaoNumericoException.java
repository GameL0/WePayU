package br.ufal.ic.p2.wepayu.exception.numerico;

public class SalarioNaoNumericoException extends ValorNaoNumericoException {
    public SalarioNaoNumericoException() {
        super("Salario deve ser numerico.");
    }
}
