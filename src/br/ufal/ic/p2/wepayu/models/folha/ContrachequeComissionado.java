package br.ufal.ic.p2.wepayu.models.folha;

import java.math.BigDecimal;

public class ContrachequeComissionado extends Contracheque {
    private final BigDecimal fixo;
    private final BigDecimal vendas;
    private final BigDecimal comissao;

    public ContrachequeComissionado(String nome, String metodoPagamento, BigDecimal fixo, BigDecimal vendas, BigDecimal comissao) {
        super(nome, metodoPagamento, fixo.add(comissao));
        this.fixo = fixo;
        this.vendas = vendas;
        this.comissao = comissao;
    }

    @Override
    public void adicionarEm(RelatorioFolha relatorio) {
        relatorio.adicionar(this);
    }

    public BigDecimal getFixo() {
        return fixo;
    }

    public BigDecimal getVendas() {
        return vendas;
    }

    public BigDecimal getComissao() {
        return comissao;
    }
}
