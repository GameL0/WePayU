package br.ufal.ic.p2.wepayu.exception.naoencontrado;

public class MembroNaoExisteException extends NaoEncontradoException {
    public MembroNaoExisteException() {
        super("Membro nao existe.");
    }
}
