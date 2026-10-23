package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.negativo.HorasNaoPositivasException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CartaoDePonto extends Lancamento {
    private static final BigDecimal JORNADA = new BigDecimal(8);

    private BigDecimal horas;

    public CartaoDePonto() {
    }

    public CartaoDePonto(LocalDate data, BigDecimal horas) throws HorasNaoPositivasException {
        super(data);
        if (horas.compareTo(BigDecimal.ZERO) <= 0) {
            throw new HorasNaoPositivasException();
        }
        this.horas = horas;
    }

    public BigDecimal horasNormais() {
        return horas.min(JORNADA);
    }

    public BigDecimal horasExtras() {
        return horas.subtract(JORNADA).max(BigDecimal.ZERO);
    }

    public BigDecimal getHoras() {
        return horas;
    }

    public void setHoras(BigDecimal horas) {
        this.horas = horas;
    }
}
