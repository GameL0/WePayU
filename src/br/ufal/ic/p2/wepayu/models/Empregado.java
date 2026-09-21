package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;

import java.math.BigDecimal;

public abstract class Empregado {
    private String nome;
    private String endereco;
    private BigDecimal salario;
    private MembroSindicato membroSindicato;


    public Empregado(String nome, String endereco, BigDecimal salario){
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public String getNome() {return nome;}
    public String getEndereco() {return endereco;}
    public BigDecimal getSalario() {return salario;}
    public MembroSindicato getMembroSindicato() {return membroSindicato;}
    public void setMembroSindicato(MembroSindicato membroSindicato) {this.membroSindicato = membroSindicato;}

    public boolean isSindicalizado(){ return membroSindicato != null;}

    public abstract String getTipo();

    public String getAtributo(String atributo){
        switch (atributo){
            case "nome": return getNome();
            case "endereco": return getEndereco();
            case "salario": return getSalario().setScale(2).toString().replace(".", ",");
            case "tipo": return getTipo();
            case "sindicalizado": return "false";
            case "idSindicato":
                if (isSindicalizado()) return membroSindicato.getIdSindicato();
                return null;
            case "taxaSindical":
                if (isSindicalizado()) return membroSindicato.getTaxaSindical().setScale(2).toString().replace(".", ",");
                return null;
            default: return null;
        }
    }

}
