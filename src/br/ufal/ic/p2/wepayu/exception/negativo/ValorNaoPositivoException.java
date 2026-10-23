package br.ufal.ic.p2.wepayu.exception.negativo;

public class ValorNaoPositivoException extends ValorNegativoException {
    public ValorNaoPositivoException() {
        super("Valor deve ser positivo.");
    }
}
