package br.ufal.ic.p2.wepayu.Exception;

public class EmpregadoNaoEComissionadoException extends RuntimeException {
    public EmpregadoNaoEComissionadoException() {
        super("Empregado nao eh comissionado.");
    }
}
