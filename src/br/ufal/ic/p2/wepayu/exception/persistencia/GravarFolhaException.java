package br.ufal.ic.p2.wepayu.exception.persistencia;

public class GravarFolhaException extends PersistenciaException {
    public GravarFolhaException(Throwable causa) {
        super("Nao foi possivel gravar a folha de pagamento.", causa);
    }
}
