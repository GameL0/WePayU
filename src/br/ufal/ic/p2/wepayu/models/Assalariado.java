package br.ufal.ic.p2.wepayu.models;

import java.math.BigDecimal;

public class Assalariado extends Empregado{
    public Assalariado(String nome, String endereco, BigDecimal salario){
        super(nome, endereco, salario);
    }

    @Override
    public String getTipo(){
        return  "assalariado";
    }
}
