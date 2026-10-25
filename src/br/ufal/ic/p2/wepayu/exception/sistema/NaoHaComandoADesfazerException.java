package br.ufal.ic.p2.wepayu.exception.sistema;

public class NaoHaComandoADesfazerException extends SistemaException {
    public NaoHaComandoADesfazerException() {
        super("Nao ha comando a desfazer.");
    }
}
