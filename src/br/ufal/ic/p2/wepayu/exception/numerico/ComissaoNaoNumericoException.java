package br.ufal.ic.p2.wepayu.exception.numerico;

public class ComissaoNaoNumericoException extends ValorNaoNumericoException {
    public ComissaoNaoNumericoException() {
        super("Comissao deve ser numerica.");
    }
}
