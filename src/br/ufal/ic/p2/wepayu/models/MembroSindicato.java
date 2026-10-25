package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.conversao.ConversorNumero;
import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.exception.naoencontrado.AtributoNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.negativo.TaxaSindicalNegativaException;
import br.ufal.ic.p2.wepayu.exception.negativo.ValorNaoPositivoException;
import br.ufal.ic.p2.wepayu.exception.nulo.IdentificacaoSindicatoNulaException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MembroSindicato extends Filiacao {
    private final String idSindicato;
    private final BigDecimal taxaSindical;
    private final List<TaxaServico> taxasServico;

    public MembroSindicato(String idSindicato, BigDecimal taxaSindical) throws WePayUException {
        this(idSindicato, taxaSindical, new ArrayList<>());
    }

    public MembroSindicato(String idSindicato, BigDecimal taxaSindical, List<TaxaServico> taxasServico) throws WePayUException {
        if (idSindicato == null || idSindicato.isEmpty()) {
            throw new IdentificacaoSindicatoNulaException();
        }
        if (taxaSindical.compareTo(BigDecimal.ZERO) < 0) {
            throw new TaxaSindicalNegativaException();
        }
        this.idSindicato = idSindicato;
        this.taxaSindical = taxaSindical;
        this.taxasServico = taxasServico;
    }

    @Override
    public boolean ehSindicalizado() {
        return true;
    }

    @Override
    public Filiacao sindicalizar(String idSindicato, BigDecimal taxaSindical) throws WePayUException {
        return new MembroSindicato(idSindicato, taxaSindical, new ArrayList<>(taxasServico));
    }

    @Override
    public BigDecimal descontos(Periodo periodo) {
        BigDecimal taxaDoPeriodo = taxaSindical.multiply(new BigDecimal(periodo.dias()));
        return taxaDoPeriodo.add(totalTaxasServico(periodo));
    }

    @Override
    public boolean temIdSindicato(String idSindicato) {
        return this.idSindicato.equals(idSindicato);
    }

    @Override
    public String getAtributo(String atributo) throws AtributoNaoExisteException {
        switch (atributo) {
            case "idSindicato": return idSindicato;
            case "taxaSindical": return ConversorNumero.formatarMoeda(taxaSindical);
            default: throw new AtributoNaoExisteException();
        }
    }

    @Override
    public void lancaTaxaServico(LocalDate data, BigDecimal valor) throws ValorNaoPositivoException {
        taxasServico.add(new TaxaServico(data, valor));
    }

    @Override
    public BigDecimal totalTaxasServico(Periodo periodo) {
        return LancamentoMonetario.somar(taxasServico, periodo);
    }

    public String getIdSindicato() {
        return idSindicato;
    }

    public BigDecimal getTaxaSindical() {
        return taxaSindical;
    }

    public List<TaxaServico> getTaxasServico() {
        return new ArrayList<>(taxasServico);
    }
}
