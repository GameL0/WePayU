package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.conversao.ConversorNumero;
import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.exception.negativo.ComissaoNegativaException;
import br.ufal.ic.p2.wepayu.exception.negativo.ValorNaoPositivoException;
import br.ufal.ic.p2.wepayu.exception.negativo.ValorNegativoException;
import br.ufal.ic.p2.wepayu.exception.nulo.ValorNuloException;

import br.ufal.ic.p2.wepayu.models.agenda.AgendaPagamento;
import br.ufal.ic.p2.wepayu.models.agenda.AgendaSemanal;
import br.ufal.ic.p2.wepayu.models.folha.Contracheque;
import br.ufal.ic.p2.wepayu.models.folha.ContrachequeComissionado;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Comissionado extends Empregado {
    private BigDecimal comissao;

    public Comissionado(String nome, String endereco, BigDecimal salario, BigDecimal comissao) throws ValorNuloException, ValorNegativoException {
        super(nome, endereco, salario);
        alterarComissao(comissao);
    }

    @Override
    public String getTipo() {
        return "comissionado";
    }

    @Override
    protected AgendaPagamento agendaPadrao() {
        return new AgendaSemanal(2, 5);
    }

    @Override
    protected Contracheque criarContracheque(Periodo periodo) {
        BigDecimal fixo = agenda().salarioProporcional(getSalario());
        BigDecimal vendas = totalVendas(periodo);
        BigDecimal valorComissao = vendas.multiply(comissao).setScale(2, RoundingMode.DOWN);
        return new ContrachequeComissionado(getNome(), metodoPagamentoNaFolha(), fixo, vendas, valorComissao);
    }

    @Override
    public String getAtributo(String atributo) throws WePayUException {
        if ("comissao".equals(atributo)) {
            return ConversorNumero.formatarMoeda(comissao);
        }
        return super.getAtributo(atributo);
    }

    @Override
    public Comissionado comoComissionado() {
        return this;
    }

    public final void alterarComissao(BigDecimal comissao) throws ComissaoNegativaException {
        if (comissao.compareTo(BigDecimal.ZERO) < 0) {
            throw new ComissaoNegativaException();
        }
        this.comissao = comissao;
    }

    public void lancaVenda(LocalDate data, BigDecimal valor) throws ValorNaoPositivoException {
        getHistorico().lancaVenda(data, valor);
    }

    public BigDecimal totalVendas(Periodo periodo) {
        return getHistorico().totalVendas(periodo);
    }

    public BigDecimal getComissao() {
        return comissao;
    }
}
