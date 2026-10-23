package br.ufal.ic.p2.wepayu.exception.naoencontrado;

public class EmpregadoNaoExisteException extends NaoEncontradoException {
    public EmpregadoNaoExisteException() {
        super("Empregado nao existe.");
    }
}
