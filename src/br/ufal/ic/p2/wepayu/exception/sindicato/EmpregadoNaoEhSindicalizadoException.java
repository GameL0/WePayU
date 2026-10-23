package br.ufal.ic.p2.wepayu.exception.sindicato;

public class EmpregadoNaoEhSindicalizadoException extends SindicatoException {
    public EmpregadoNaoEhSindicalizadoException() {
        super("Empregado nao eh sindicalizado.");
    }
}
