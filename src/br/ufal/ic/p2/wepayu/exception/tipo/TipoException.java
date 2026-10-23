package br.ufal.ic.p2.wepayu.exception.tipo;

import br.ufal.ic.p2.wepayu.exception.WePayUException;

public abstract class TipoException extends WePayUException {
    protected TipoException(String mensagem) {
        super(mensagem);
    }
}
