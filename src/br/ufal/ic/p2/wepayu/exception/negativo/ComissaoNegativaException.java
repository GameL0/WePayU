package br.ufal.ic.p2.wepayu.exception.negativo;

public class ComissaoNegativaException extends ValorNegativoException {
    public ComissaoNegativaException() {
        super("Comissao deve ser nao-negativa.");
    }
}
