package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.exception.negativo.TaxaSindicalNegativaException;
import br.ufal.ic.p2.wepayu.exception.negativo.ValorNaoPositivoException;
import br.ufal.ic.p2.wepayu.exception.nulo.IdentificacaoSindicatoNulaException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MembroSindicato extends Filiacao {
    private String idSindicato;
    private BigDecimal taxaSindical;
    private List<TaxaServico> taxasServico = new ArrayList<>();

    public MembroSindicato() {
    }

    public MembroSindicato(String idSindicato, BigDecimal taxaSindical) throws WePayUException {
        if (idSindicato == null || idSindicato.isEmpty()) {
            throw new IdentificacaoSindicatoNulaException();
        }
        if (taxaSindical.compareTo(BigDecimal.ZERO) < 0) {
            throw new TaxaSindicalNegativaException();
        }
        this.idSindicato = idSindicato;
        this.taxaSindical = taxaSindical;
    }

    @Override
    public boolean ehSindicalizado() {
        return true;
    }

    @Override
    public Filiacao sindicalizar(String idSindicato, BigDecimal taxaSindical) throws WePayUException {
        MembroSindicato atualizado = new MembroSindicato(idSindicato, taxaSindical);
        atualizado.taxasServico = new ArrayList<>(taxasServico);
        return atualizado;
    }

    @Override
    public boolean temIdSindicato(String idSindicato) {
        return this.idSindicato.equals(idSindicato);
    }

    @Override
    public MembroSindicato comoMembro() {
        return this;
    }

    public void lancaTaxaServico(LocalDate data, BigDecimal valor) throws ValorNaoPositivoException {
        taxasServico.add(new TaxaServico(data, valor));
    }

    public BigDecimal totalTaxasServico(Periodo periodo) {
        return LancamentoMonetario.somar(taxasServico, periodo);
    }

    public String getIdSindicato() {
        return idSindicato;
    }

    public void setIdSindicato(String idSindicato) {
        this.idSindicato = idSindicato;
    }

    public BigDecimal getTaxaSindical() {
        return taxaSindical;
    }

    public void setTaxaSindical(BigDecimal taxaSindical) {
        this.taxaSindical = taxaSindical;
    }

    public List<TaxaServico> getTaxasServico() {
        return new ArrayList<>(taxasServico);
    }

    public void setTaxasServico(List<TaxaServico> taxasServico) {
        this.taxasServico = new ArrayList<>(taxasServico);
    }
}
