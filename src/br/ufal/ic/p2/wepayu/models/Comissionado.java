package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.conversao.ConversorNumero;
import br.ufal.ic.p2.wepayu.exception.naoencontrado.AtributoNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.negativo.ComissaoNegativaException;
import br.ufal.ic.p2.wepayu.exception.negativo.ValorNaoPositivoException;
import br.ufal.ic.p2.wepayu.exception.negativo.ValorNegativoException;
import br.ufal.ic.p2.wepayu.exception.nulo.ValorNuloException;
import br.ufal.ic.p2.wepayu.exception.sindicato.EmpregadoNaoEhSindicalizadoException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Comissionado extends Empregado {
    private BigDecimal comissao;
    private List<Venda> vendas = new ArrayList<>();

    public Comissionado() {
    }

    public Comissionado(String nome, String endereco, BigDecimal salario, BigDecimal comissao) throws ValorNuloException, ValorNegativoException {
        super(nome, endereco, salario);
        if (comissao.compareTo(BigDecimal.ZERO) < 0) {
            throw new ComissaoNegativaException();
        }
        this.comissao = comissao;
    }

    @Override
    public String getTipo() {
        return "comissionado";
    }

    @Override
    public Comissionado comoComissionado() {
        return this;
    }

    @Override
    public String getAtributo(String atributo) throws AtributoNaoExisteException, EmpregadoNaoEhSindicalizadoException {
        if ("comissao".equals(atributo)) {
            return ConversorNumero.formatarMoeda(comissao);
        }
        return super.getAtributo(atributo);
    }

    public void lancaVenda(LocalDate data, BigDecimal valor) throws ValorNaoPositivoException {
        vendas.add(new Venda(data, valor));
    }

    public BigDecimal totalVendas(Periodo periodo) {
        return LancamentoMonetario.somar(vendas, periodo);
    }

    public BigDecimal getComissao() { return comissao; }
    public void setComissao(BigDecimal comissao) { this.comissao = comissao; }

    public List<Venda> getVendas() {
        return new ArrayList<>(vendas);
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = new ArrayList<>(vendas);
    }
}
