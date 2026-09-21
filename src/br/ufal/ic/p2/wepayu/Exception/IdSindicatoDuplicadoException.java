package br.ufal.ic.p2.wepayu.Exception;

public class IdSindicatoDuplicadoException extends Exception {
    public IdSindicatoDuplicadoException() {
        super("Ha outro empregado com esta identificacao de sindicato");
    }
}