package br.ufal.ic.p2.wepayu.exception.naoencontrado;

public class AtributoNaoExisteException extends NaoEncontradoException {
    public AtributoNaoExisteException() {
        super("Atributo nao existe.");
    }
}
