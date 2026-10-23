package br.ufal.ic.p2.wepayu.exception.invalido;

import br.ufal.ic.p2.wepayu.exception.WePayUException;

public abstract class ValorInvalidoException extends WePayUException {
    protected ValorInvalidoException(String mensagem) {
        super(mensagem);
    }
}
