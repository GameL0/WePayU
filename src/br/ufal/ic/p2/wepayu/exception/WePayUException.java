package br.ufal.ic.p2.wepayu.exception;

public abstract class WePayUException extends Exception {
    protected WePayUException(String mensagem) {
        super(mensagem);
    }

    protected WePayUException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
