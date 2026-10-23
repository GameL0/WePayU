package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.naoencontrado.AtributoNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.negativo.ComissaoNegativaException;
import br.ufal.ic.p2.wepayu.exception.negativo.ValorNegativoException;
import br.ufal.ic.p2.wepayu.exception.nulo.ValorNuloException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Comissionado extends Empregado {
    private BigDecimal comissao;


    public Comissionado(String nome, String endereco, BigDecimal salario, BigDecimal comissao) throws ValorNuloException, ValorNegativoException {
        super(nome, endereco, salario);
        if(comissao.compareTo(BigDecimal.ZERO) < 0){
            throw new ComissaoNegativaException();
        }
        this.comissao = comissao;
    }

    @Override
    public String getTipo() {
        return "comissionado";
    }

    @Override
    public String getAtributo(String atributo) throws AtributoNaoExisteException {
        switch (atributo) {
            case "comissao":
                return this.comissao.setScale(2, RoundingMode.HALF_UP).toPlainString().replace(".", ",");
            default:
                return super.getAtributo(atributo);
        }
    }
}
