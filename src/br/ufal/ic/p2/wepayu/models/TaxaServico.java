package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.negativo.ValorNaoPositivoException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TaxaServico extends LancamentoMonetario {
    public TaxaServico() {
    }

    public TaxaServico(LocalDate data, BigDecimal valor) throws ValorNaoPositivoException {
        super(data, valor);
    }
}
