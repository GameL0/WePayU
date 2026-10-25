package br.ufal.ic.p2.wepayu.models.folha;

import java.math.BigDecimal;

public class ContrachequeHorista extends Contracheque {
    private final BigDecimal horasNormais;
    private final BigDecimal horasExtras;

    public ContrachequeHorista(String nome, String metodoPagamento, BigDecimal bruto, BigDecimal horasNormais, BigDecimal horasExtras) {
        super(nome, metodoPagamento, bruto);
        this.horasNormais = horasNormais;
        this.horasExtras = horasExtras;
    }

    @Override
    public void adicionarEm(RelatorioFolha relatorio) {
        relatorio.adicionar(this);
    }

    public BigDecimal getHorasNormais() {
        return horasNormais;
    }

    public BigDecimal getHorasExtras() {
        return horasExtras;
    }
}
