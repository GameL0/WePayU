package br.ufal.ic.p2.wepayu.exception.data;

import br.ufal.ic.p2.wepayu.exception.WePayUException;

public abstract class DataException extends WePayUException {
    protected DataException(String mensagem) {
        super(mensagem);
    }
}
