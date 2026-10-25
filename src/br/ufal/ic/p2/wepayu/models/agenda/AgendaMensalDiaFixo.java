package br.ufal.ic.p2.wepayu.models.agenda;

import br.ufal.ic.p2.wepayu.models.Periodo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AgendaMensalDiaFixo implements AgendaPagamento {
    private final int dia;

    public AgendaMensalDiaFixo(int dia) {
        this.dia = dia;
    }

    @Override
    public String descricao() {
        return "mensal " + dia;
    }

    @Override
    public boolean ehDiaDePagamento(LocalDate data) {
        return data.getDayOfMonth() == dia;
    }

    @Override
    public Periodo periodoAte(LocalDate diaDePagamento) {
        return Periodo.terminandoEm(diaDePagamento, diaDePagamento.minusMonths(1).plusDays(1));
    }

    @Override
    public BigDecimal salarioProporcional(BigDecimal salarioMensal) {
        return salarioMensal;
    }
}
