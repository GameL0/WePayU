package br.ufal.ic.p2.wepayu.exception.invalido;

public class ValorBooleanoInvalidoException extends ValorInvalidoException {
    public ValorBooleanoInvalidoException() {
        super("Valor deve ser true ou false.");
    }
}
