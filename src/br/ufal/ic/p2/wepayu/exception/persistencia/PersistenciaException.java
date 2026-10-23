package br.ufal.ic.p2.wepayu.exception.persistencia;

import br.ufal.ic.p2.wepayu.exception.WePayUException;

public abstract class PersistenciaException extends WePayUException {
    protected PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
