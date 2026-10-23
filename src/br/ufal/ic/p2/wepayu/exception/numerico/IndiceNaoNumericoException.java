package br.ufal.ic.p2.wepayu.exception.numerico;

public class IndiceNaoNumericoException extends ValorNaoNumericoException {
    public IndiceNaoNumericoException() {
        super("Indice deve ser numerico.");
    }
}
