package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.negativo.ValorNegativoException;
import br.ufal.ic.p2.wepayu.exception.nulo.ValorNuloException;

import br.ufal.ic.p2.wepayu.models.agenda.AgendaMensal;
import br.ufal.ic.p2.wepayu.models.agenda.AgendaPagamento;
import br.ufal.ic.p2.wepayu.models.folha.Contracheque;
import br.ufal.ic.p2.wepayu.models.folha.ContrachequeAssalariado;

import java.math.BigDecimal;

public class Assalariado extends Empregado {
    public Assalariado(String nome, String endereco, BigDecimal salario) throws ValorNuloException, ValorNegativoException {
        super(nome, endereco, salario);
    }

    @Override
    public String getTipo() {
        return "assalariado";
    }

    @Override
    protected AgendaPagamento agendaPadrao() {
        return new AgendaMensal();
    }

    @Override
    protected Contracheque criarContracheque(Periodo periodo) {
        BigDecimal bruto = agenda().salarioProporcional(getSalario());
        return new ContrachequeAssalariado(getNome(), metodoPagamentoNaFolha(), bruto);
    }
}
