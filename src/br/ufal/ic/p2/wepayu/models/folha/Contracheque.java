package br.ufal.ic.p2.wepayu.models.folha;

import java.math.BigDecimal;

public abstract class Contracheque {
    private final String nome;
    private final String metodoPagamento;
    private final BigDecimal bruto;
    private BigDecimal descontos = BigDecimal.ZERO;
    private BigDecimal dividaRestante = BigDecimal.ZERO;

    protected Contracheque(String nome, String metodoPagamento, BigDecimal bruto) {
        this.nome = nome;
        this.metodoPagamento = metodoPagamento;
        this.bruto = bruto;
    }

    public void descontar(BigDecimal devido) {
        if (devido.compareTo(bruto) > 0) {
            descontos = bruto;
            dividaRestante = devido.subtract(bruto);
        } else {
            descontos = devido;
            dividaRestante = BigDecimal.ZERO;
        }
    }

    public abstract void adicionarEm(RelatorioFolha relatorio);

    public String getNome() {
        return nome;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public BigDecimal getBruto() {
        return bruto;
    }

    public BigDecimal getDescontos() {
        return descontos;
    }

    public BigDecimal getLiquido() {
        return bruto.subtract(descontos);
    }

    public BigDecimal getDividaRestante() {
        return dividaRestante;
    }
}
