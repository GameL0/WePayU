package br.ufal.ic.p2.wepayu.exception.naoencontrado;

import br.ufal.ic.p2.wepayu.exception.WePayUException;

public abstract class NaoEncontradoException extends WePayUException {
    protected NaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
