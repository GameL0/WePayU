package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.negativo.HorasNaoPositivasException;
import br.ufal.ic.p2.wepayu.exception.negativo.ValorNegativoException;
import br.ufal.ic.p2.wepayu.exception.nulo.ValorNuloException;

import br.ufal.ic.p2.wepayu.models.agenda.AgendaPagamento;
import br.ufal.ic.p2.wepayu.models.agenda.AgendaSemanal;
import br.ufal.ic.p2.wepayu.models.folha.Contracheque;
import br.ufal.ic.p2.wepayu.models.folha.ContrachequeHorista;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Horista extends Empregado {
    private static final BigDecimal ADICIONAL_HORA_EXTRA = new BigDecimal("1.5");

    public Horista(String nome, String endereco, BigDecimal salario) throws ValorNuloException, ValorNegativoException {
        super(nome, endereco, salario);
    }

    @Override
    public String getTipo() {
        return "horista";
    }

    @Override
    protected AgendaPagamento agendaPadrao() {
        return new AgendaSemanal(1, 5);
    }

    @Override
    protected Contracheque criarContracheque(Periodo periodo) {
        BigDecimal normais = horasNormais(periodo);
        BigDecimal extras = horasExtras(periodo);
        BigDecimal bruto = normais.multiply(getSalario())
                .add(extras.multiply(getSalario()).multiply(ADICIONAL_HORA_EXTRA));
        return new ContrachequeHorista(getNome(), metodoPagamentoNaFolha(), bruto, normais, extras);
    }

    @Override
    public Horista comoHorista() {
        return this;
    }

    public void lancaCartaoDePonto(LocalDate data, BigDecimal horas) throws HorasNaoPositivasException {
        getHistorico().lancaCartao(data, horas);
    }

    public BigDecimal horasNormais(Periodo periodo) {
        return getHistorico().horasNormais(periodo);
    }

    public BigDecimal horasExtras(Periodo periodo) {
        return getHistorico().horasExtras(periodo);
    }
}
