package br.ufal.ic.p2.wepayu.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Comissionado extends Empregado {
    private BigDecimal comissao;
    private List<ResultadoVenda> vendas = new ArrayList<>();

    public Comissionado(String nome, String endereco, BigDecimal salario, BigDecimal comissao){
        super(nome, endereco, salario);
        this.comissao = comissao;
    }

    public BigDecimal getComissao() {return comissao;}

    public void lancaVenda(LocalDate data, BigDecimal valor){
        vendas.add(new ResultadoVenda(data, valor));
    }

    public BigDecimal getVendasRealizadas(LocalDate dataInicial, LocalDate dataFinal){
        BigDecimal total = BigDecimal.ZERO;
        for (ResultadoVenda venda : vendas){
            if(!venda.getData().isBefore(dataInicial) && venda.getData().isBefore(dataFinal)){
                total = total.add(venda.getValor());
            }
        }

        return total;
    }

    public List<ResultadoVenda> getVendas() {return vendas;}

    @Override
    public String getTipo(){
        return "comissionado";
    }

    @Override
    public String getAtributo(String atributo){
        if(atributo.equals("comissao")){
            return getComissao().setScale(2).toString().replace(".", ",");
        }
        return super.getAtributo(atributo);
    }


}
