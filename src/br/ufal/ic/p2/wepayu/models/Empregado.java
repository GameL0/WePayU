package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.naoencontrado.AtributoNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.nulo.EnderecoNuloException;
import br.ufal.ic.p2.wepayu.exception.nulo.NomeNuloException;
import br.ufal.ic.p2.wepayu.exception.negativo.SalarioNegativoException;
import br.ufal.ic.p2.wepayu.exception.negativo.ValorNegativoException;
import br.ufal.ic.p2.wepayu.exception.nulo.ValorNuloException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Empregado {
    private String nome;
    private String endereco;
    private BigDecimal salario;

    public Empregado(String nome, String endereco, BigDecimal salario) throws ValorNuloException, ValorNegativoException {
        if(nome == null || nome.isEmpty()){
            throw new NomeNuloException();
        }
        if(endereco == null || endereco.isEmpty()){
            throw new EnderecoNuloException();
        }
        if(salario.compareTo(BigDecimal.ZERO) < 0){
            throw new SalarioNegativoException();
        }
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }
    public abstract String getTipo();


    public String getAtributo(String atributo) throws AtributoNaoExisteException {
        switch (atributo){
            case "nome": return this.nome;
            case "endereco": return this.endereco;
            case "tipo": return getTipo();
            case "salario": return this.salario.setScale(2, RoundingMode.HALF_UP).toPlainString().replace(".",",");
            case "sindicalizado": return "false";
            default: throw new AtributoNaoExisteException();
        }
    }
}
