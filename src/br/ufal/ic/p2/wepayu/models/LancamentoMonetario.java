package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.negativo.ValorNaoPositivoException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public abstract class LancamentoMonetario extends Lancamento {
    private BigDecimal valor;

    protected LancamentoMonetario() {
    }

    protected LancamentoMonetario(LocalDate data, BigDecimal valor) throws ValorNaoPositivoException {
        super(data);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorNaoPositivoException();
        }
        this.valor = valor;
    }

    public static BigDecimal somar(List<? extends LancamentoMonetario> lancamentos, Periodo periodo) {
        BigDecimal total = BigDecimal.ZERO;
        for (LancamentoMonetario lancamento : lancamentos) {
            if (lancamento.estaEm(periodo)) {
                total = total.add(lancamento.getValor());
            }
        }
        return total;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
