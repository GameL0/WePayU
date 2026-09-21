package br.ufal.ic.p2.wepayu.Exception;

public class EmpregadoNaoESindicalizadoException extends Exception {
    public EmpregadoNaoESindicalizadoException() {
        super("Empregado nao eh sindicalizado.");
    }
}