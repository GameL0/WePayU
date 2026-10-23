package br.ufal.ic.p2.wepayu.exception.naoencontrado;

public class NomeNaoExisteException extends NaoEncontradoException {
    public NomeNaoExisteException() {
        super("Nao ha empregado com esse nome.");
    }
}
