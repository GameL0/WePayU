package br.ufal.ic.p2.wepayu.Exception;

public class ValorPositivoException extends RuntimeException {
    public ValorPositivoException() {
        super("Valor deve ser positivo.");
    }
}
