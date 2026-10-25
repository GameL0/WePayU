package br.ufal.ic.p2.wepayu.exception.pagamento;

import br.ufal.ic.p2.wepayu.exception.WePayUException;

public abstract class PagamentoException extends WePayUException {
    protected PagamentoException(String mensagem) {
        super(mensagem);
    }
}
