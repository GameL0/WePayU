package br.ufal.ic.p2.wepayu.exception.sindicato;

public class IdSindicatoDuplicadoException extends SindicatoException {
    public IdSindicatoDuplicadoException() {
        super("Ha outro empregado com esta identificacao de sindicato");
    }
}
