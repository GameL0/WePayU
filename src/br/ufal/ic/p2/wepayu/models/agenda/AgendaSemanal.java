package br.ufal.ic.p2.wepayu.models.agenda;

import br.ufal.ic.p2.wepayu.models.Periodo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class AgendaSemanal implements AgendaPagamento {
    private static final LocalDate INICIO = LocalDate.of(2005, 1, 1);
    private static final BigDecimal MESES_POR_ANO = new BigDecimal(12);
    private static final BigDecimal SEMANAS_POR_ANO = new BigDecimal(52);

    private final int intervaloEmSemanas;
    private final DayOfWeek diaDaSemana;

    public AgendaSemanal(int intervaloEmSemanas, int diaDaSemana) {
        this.intervaloEmSemanas = intervaloEmSemanas;
        this.diaDaSemana = DayOfWeek.of(diaDaSemana);
    }

    @Override
    public String descricao() {
        String dia = String.valueOf(diaDaSemana.getValue());
        return intervaloEmSemanas == 1 ? "semanal " + dia : "semanal " + intervaloEmSemanas + " " + dia;
    }

    @Override
    public boolean ehDiaDePagamento(LocalDate data) {
        if (data.getDayOfWeek() != diaDaSemana || data.isBefore(INICIO)) {
            return false;
        }
        LocalDate primeiraOcorrencia = INICIO.with(TemporalAdjusters.nextOrSame(diaDaSemana));
        long ocorrencia = ChronoUnit.WEEKS.between(primeiraOcorrencia, data) + 1;
        return ocorrencia % intervaloEmSemanas == 0;
    }

    @Override
    public Periodo periodoAte(LocalDate diaDePagamento) {
        return Periodo.terminandoEm(diaDePagamento, diaDePagamento.minusWeeks(intervaloEmSemanas).plusDays(1));
    }

    @Override
    public BigDecimal salarioProporcional(BigDecimal salarioMensal) {
        return salarioMensal.multiply(MESES_POR_ANO)
                .multiply(new BigDecimal(intervaloEmSemanas))
                .divide(SEMANAS_POR_ANO, 2, RoundingMode.DOWN);
    }
}
