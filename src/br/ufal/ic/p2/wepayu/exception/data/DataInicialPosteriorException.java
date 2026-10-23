package br.ufal.ic.p2.wepayu.exception.data;

public class DataInicialPosteriorException extends DataException {
    public DataInicialPosteriorException() {
        super("Data inicial nao pode ser posterior aa data final.");
    }
}
