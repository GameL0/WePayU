package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.exception.pagamento.EmpregadoNaoRecebeEmBancoException;

public abstract class MetodoPagamento {
    public abstract String descricao();

    public abstract String descricaoNaFolha(String endereco);

    public String getAtributo(String atributo) throws WePayUException {
        throw new EmpregadoNaoRecebeEmBancoException();
    }
}
