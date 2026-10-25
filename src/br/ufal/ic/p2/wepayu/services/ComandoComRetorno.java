package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.exception.WePayUException;

@FunctionalInterface
public interface ComandoComRetorno<T> {
    T executar() throws WePayUException;
}
