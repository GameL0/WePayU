package br.ufal.ic.p2.wepayu.models.agenda;

import br.ufal.ic.p2.wepayu.models.Periodo;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

public class AgendaMensal implements AgendaPagamento {
    @Override
    public String descricao() {
        return "mensal $";
    }

    @Override
    public boolean ehDiaDePagamento(LocalDate data) {
        return data.equals(ultimoDiaUtil(YearMonth.from(data)));
    }

    @Override
    public Periodo periodoAte(LocalDate diaDePagamento) {
        LocalDate pagamentoAnterior = ultimoDiaUtil(YearMonth.from(diaDePagamento).minusMonths(1));
        return Periodo.terminandoEm(diaDePagamento, pagamentoAnterior.plusDays(1));
    }

    @Override
    public BigDecimal salarioProporcional(BigDecimal salarioMensal) {
        return salarioMensal;
    }

    private LocalDate ultimoDiaUtil(YearMonth mes) {
        LocalDate dia = mes.atEndOfMonth();
        while (dia.getDayOfWeek() == DayOfWeek.SATURDAY || dia.getDayOfWeek() == DayOfWeek.SUNDAY) {
            dia = dia.minusDays(1);
        }
        return dia;
    }
}
