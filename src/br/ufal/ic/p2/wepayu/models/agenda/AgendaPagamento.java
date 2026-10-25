package br.ufal.ic.p2.wepayu.models.agenda;

import br.ufal.ic.p2.wepayu.models.Periodo;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface AgendaPagamento {
    String descricao();

    boolean ehDiaDePagamento(LocalDate data);

    Periodo periodoAte(LocalDate diaDePagamento);

    BigDecimal salarioProporcional(BigDecimal salarioMensal);
}
