package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.conversao.ConversorData;
import br.ufal.ic.p2.wepayu.conversao.ConversorNumero;
import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.exception.data.DataInvalidaException;
import br.ufal.ic.p2.wepayu.exception.numerico.ValorLancamentoNaoNumericoException;
import br.ufal.ic.p2.wepayu.models.Comissionado;
import br.ufal.ic.p2.wepayu.models.Periodo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GerenciaVendas {
    private final GerenciaEmpregado empregados;

    public GerenciaVendas(GerenciaEmpregado empregados) {
        this.empregados = empregados;
    }

    public void lancaVenda(String id, String dataInformada, String valorInformado) throws WePayUException {
        Comissionado comissionado = empregados.buscar(id).comoComissionado();
        LocalDate data = ConversorData.converter(dataInformada, DataInvalidaException::new);
        BigDecimal valor = ConversorNumero.converter(valorInformado, ValorLancamentoNaoNumericoException::new);
        comissionado.lancaVenda(data, valor);
    }

    public String getVendasRealizadas(String id, String dataInicial, String dataFinal) throws WePayUException {
        Comissionado comissionado = empregados.buscar(id).comoComissionado();
        Periodo periodo = ConversorData.converterPeriodo(dataInicial, dataFinal);
        return ConversorNumero.formatarMoeda(comissionado.totalVendas(periodo));
    }
}
