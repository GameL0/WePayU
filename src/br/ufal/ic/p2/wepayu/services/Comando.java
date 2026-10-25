package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.exception.WePayUException;

@FunctionalInterface
public interface Comando {
    void executar() throws WePayUException;
}
