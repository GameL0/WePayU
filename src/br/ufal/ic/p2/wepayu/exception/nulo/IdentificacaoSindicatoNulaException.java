package br.ufal.ic.p2.wepayu.exception.nulo;

public class IdentificacaoSindicatoNulaException extends ValorNuloException {
    public IdentificacaoSindicatoNulaException() {
        super("Identificacao do sindicato nao pode ser nula.");
    }
}
