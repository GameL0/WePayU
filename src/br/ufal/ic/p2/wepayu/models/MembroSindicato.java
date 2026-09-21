package br.ufal.ic.p2.wepayu.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MembroSindicato {
    private String idSindicato;
    private BigDecimal taxaSindical;
    private List<TaxaServico> taxasServico = new ArrayList<>();

    public MembroSindicato(String idSindicato, BigDecimal taxaSindical) {
        this.idSindicato = idSindicato;
        this.taxaSindical = taxaSindical;
    }

    public String getIdSindicato() {return idSindicato;}
    public BigDecimal getTaxaSindical() { return taxaSindical; }
    public List<TaxaServico> getTaxasServico() { return taxasServico; }
    public void lancaTaxaServico(LocalDate data, BigDecimal valor) {
        taxasServico.add(new TaxaServico(data, valor));
    }

    public BigDecimal getTotalTaxasServico(LocalDate dataInicial, LocalDate dataFinal) {
        BigDecimal total = BigDecimal.ZERO;
        for (TaxaServico taxa : taxasServico) {
            if (!taxa.getData().isBefore(dataInicial) && taxa.getData().isBefore(dataFinal)) {
                total = total.add(taxa.getValor());
            }
        }
        return total;
    }
}
