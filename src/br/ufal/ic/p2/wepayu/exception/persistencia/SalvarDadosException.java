package br.ufal.ic.p2.wepayu.exception.persistencia;

public class SalvarDadosException extends PersistenciaException {
    public SalvarDadosException(Throwable causa) {
        super("Nao foi possivel salvar os dados.", causa);
    }
}
