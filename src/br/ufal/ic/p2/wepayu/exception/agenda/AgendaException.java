package br.ufal.ic.p2.wepayu.exception.agenda;

import br.ufal.ic.p2.wepayu.exception.WePayUException;

public abstract class AgendaException extends WePayUException {
    protected AgendaException(String mensagem) {
        super(mensagem);
    }
}
