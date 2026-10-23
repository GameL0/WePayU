package br.ufal.ic.p2.wepayu.exception.numerico;

import br.ufal.ic.p2.wepayu.exception.WePayUException;

public abstract class ValorNaoNumericoException extends WePayUException {
    protected ValorNaoNumericoException(String mensagem) {
        super(mensagem);
    }
}
