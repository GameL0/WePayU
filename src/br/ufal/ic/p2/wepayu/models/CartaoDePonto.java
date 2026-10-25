package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.negativo.HorasNaoPositivasException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class CartaoDePonto extends Lancamento {
    private static final BigDecimal JORNADA = new BigDecimal(8);

    private final BigDecimal horas;

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

    @Override
    public boolean equals(Object outro) {
        return super.equals(outro) && horas.equals(((CartaoDePonto) outro).horas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), horas);
    }
}
