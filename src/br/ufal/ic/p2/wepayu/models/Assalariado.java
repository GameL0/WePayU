package br.ufal.ic.p2.wepayu.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Assalariado extends Empregado{
    public Assalariado(String nome, String endereco, BigDecimal salario){
        super(nome, endereco, salario);
    }

    @Override
    public String getTipo(){
        return  "assalariado";
    }

    @Override
    public boolean isDiaDePagamento(LocalDate dataFolha) {
        LocalDate amanha = dataFolha.plusDays(1);
        boolean ultimoDiaDoMes = amanha.getMonth() != dataFolha.getMonth();
        
        if (dataFolha.getDayOfWeek().getValue() == 5) {
            boolean sabadoOutroMes = dataFolha.plusDays(1).getMonth() != dataFolha.getMonth();
            boolean domingoOutroMes = dataFolha.plusDays(2).getMonth() != dataFolha.getMonth();
            if (sabadoOutroMes || domingoOutroMes) {
                return true;
            }
        }
        
        if (ultimoDiaDoMes && dataFolha.getDayOfWeek().getValue() >= 1 && dataFolha.getDayOfWeek().getValue() <= 5) {
            return true;
        }
        
        return false;
    }

    @Override
    public LocalDate getDataContrato() {
        return LocalDate.of(2005, 1, 1);
    }
}
