package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.conversao.ConversorData;
import br.ufal.ic.p2.wepayu.conversao.ConversorNumero;
import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.exception.data.DataInvalidaException;
import br.ufal.ic.p2.wepayu.exception.numerico.HorasNaoNumericasException;
import br.ufal.ic.p2.wepayu.models.Horista;
import br.ufal.ic.p2.wepayu.models.Periodo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.BiFunction;

public class GerenciaCartaoDePonto {
    private final GerenciaEmpregado empregados;

    public GerenciaCartaoDePonto(GerenciaEmpregado empregados) {
        this.empregados = empregados;
    }

    public void lancaCartao(String id, String dataInformada, String horasInformadas) throws WePayUException {
        Horista horista = empregados.buscar(id).comoHorista();
        LocalDate data = ConversorData.converter(dataInformada, DataInvalidaException::new);
        BigDecimal horas = ConversorNumero.converter(horasInformadas, HorasNaoNumericasException::new);
        horista.lancaCartaoDePonto(data, horas);
    }

    public String getHorasNormaisTrabalhadas(String id, String dataInicial, String dataFinal) throws WePayUException {
        return calcularHoras(id, dataInicial, dataFinal, Horista::horasNormais);
    }

    public String getHorasExtrasTrabalhadas(String id, String dataInicial, String dataFinal) throws WePayUException {
        return calcularHoras(id, dataInicial, dataFinal, Horista::horasExtras);
    }

    private String calcularHoras(String id, String dataInicial, String dataFinal, BiFunction<Horista, Periodo, BigDecimal> calculo) throws WePayUException {
        Horista horista = empregados.buscar(id).comoHorista();
        Periodo periodo = ConversorData.converterPeriodo(dataInicial, dataFinal);
        return ConversorNumero.formatarHoras(calculo.apply(horista, periodo));
    }
}
