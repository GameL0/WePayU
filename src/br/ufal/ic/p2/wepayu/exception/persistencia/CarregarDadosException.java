package br.ufal.ic.p2.wepayu.exception.persistencia;

public class CarregarDadosException extends PersistenciaException {
    public CarregarDadosException(Throwable causa) {
        super("Nao foi possivel carregar os dados.", causa);
    }
}
