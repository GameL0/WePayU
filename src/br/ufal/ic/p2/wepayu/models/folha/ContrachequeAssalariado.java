package br.ufal.ic.p2.wepayu.models.folha;

import java.math.BigDecimal;

public class ContrachequeAssalariado extends Contracheque {
    public ContrachequeAssalariado(String nome, String metodoPagamento, BigDecimal bruto) {
        super(nome, metodoPagamento, bruto);
    }

    @Override
    public void adicionarEm(RelatorioFolha relatorio) {
        relatorio.adicionar(this);
    }
}
