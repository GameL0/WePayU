package br.ufal.ic.p2.wepayu.exception.nulo;

public class IdentificacaoMembroNulaException extends ValorNuloException {
    public IdentificacaoMembroNulaException() {
        super("Identificacao do membro nao pode ser nula.");
    }
}
