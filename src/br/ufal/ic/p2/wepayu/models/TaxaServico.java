package br.ufal.ic.p2.wepayu.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TaxaServico {
    private LocalDate data;
    private BigDecimal valor;

    public TaxaServico(LocalDate data, BigDecimal valor) {
        this.data = data;
        this.valor = valor;
    }

    public LocalDate getData() { return data; }
    public BigDecimal getValor() { return valor; }
}