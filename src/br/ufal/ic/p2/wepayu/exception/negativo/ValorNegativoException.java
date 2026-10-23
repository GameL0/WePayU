package br.ufal.ic.p2.wepayu.exception.negativo;

import br.ufal.ic.p2.wepayu.exception.WePayUException;

public abstract class ValorNegativoException extends WePayUException {
    protected ValorNegativoException(String mensagem) {
        super(mensagem);
    }
}
