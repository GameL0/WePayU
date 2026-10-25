package br.ufal.ic.p2.wepayu.exception.pagamento;

public class MetodoPagamentoInvalidoException extends PagamentoException {
    public MetodoPagamentoInvalidoException() {
        super("Metodo de pagamento invalido.");
    }
}
