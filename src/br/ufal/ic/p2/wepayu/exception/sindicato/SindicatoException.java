package br.ufal.ic.p2.wepayu.exception.sindicato;

import br.ufal.ic.p2.wepayu.exception.WePayUException;

public abstract class SindicatoException extends WePayUException {
    protected SindicatoException(String mensagem) {
        super(mensagem);
    }
}
