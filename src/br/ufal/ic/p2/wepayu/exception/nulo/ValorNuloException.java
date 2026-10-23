package br.ufal.ic.p2.wepayu.exception.nulo;

import br.ufal.ic.p2.wepayu.exception.WePayUException;

public abstract class ValorNuloException extends WePayUException {
    protected ValorNuloException(String mensagem) {
        super(mensagem);
    }
}
