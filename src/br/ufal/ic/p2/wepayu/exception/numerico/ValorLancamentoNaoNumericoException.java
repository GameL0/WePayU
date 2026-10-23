package br.ufal.ic.p2.wepayu.exception.numerico;

public class ValorLancamentoNaoNumericoException extends ValorNaoNumericoException {
    public ValorLancamentoNaoNumericoException() {
        super("Valor deve ser numerico.");
    }
}
