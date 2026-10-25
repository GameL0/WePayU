package br.ufal.ic.p2.wepayu.exception.sistema;

import br.ufal.ic.p2.wepayu.exception.WePayUException;

public abstract class SistemaException extends WePayUException {
    protected SistemaException(String mensagem) {
        super(mensagem);
    }
}
