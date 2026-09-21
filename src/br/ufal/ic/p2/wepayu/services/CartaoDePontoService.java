package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.Horista;
import br.ufal.ic.p2.wepayu.Exception.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.time.format.ResolverStyle;

public class CartaoDePontoService {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("d/M/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    private Horista buscarHorista(HashMap<String, Empregado> empregados, String emp)
            throws CampoNuloException, EmpregadoNaoExisteException, EmpregadoNaoEHoristaException {

        if (emp.isEmpty()) throw new CampoNuloException("Identificacao do empregado nao pode ser nula.");
        Empregado empregado = empregados.get(emp);
        if (empregado == null) throw new EmpregadoNaoExisteException();
        if (!(empregado instanceof Horista)) throw new EmpregadoNaoEHoristaException();
        return (Horista) empregado;
    }

    private LocalDate parsearData(String data, String mensagemErro) throws DataInvalidaException {
        try {
            return LocalDate.parse(data, FORMATO);
        } catch (Exception e) {
            throw new DataInvalidaException(mensagemErro);
        }
    }

    public void lancaCartao(HashMap<String, Empregado> empregados, String emp, String data, String horas)
            throws CampoNuloException, EmpregadoNaoExisteException,
            EmpregadoNaoEHoristaException, DataInvalidaException, HorasPositivasException {

        Horista horista = buscarHorista(empregados, emp);
        LocalDate dataConvertida = parsearData(data, "Data invalida.");

        BigDecimal horasDecimal = new BigDecimal(horas.replace(",", "."));
        if (horasDecimal.compareTo(BigDecimal.ZERO) <= 0) throw new HorasPositivasException();

        horista.lancaCartao(dataConvertida, horasDecimal);
    }

    public String getHorasNormais(HashMap<String, Empregado> empregados, String emp,
                                  String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, EmpregadoNaoEHoristaException,
            DataInvalidaException, CampoNuloException {

        Horista horista = buscarHorista(empregados, emp);
        LocalDate inicio = parsearData(dataInicial, "Data inicial invalida.");
        LocalDate fim = parsearData(dataFinal, "Data final invalida.");

        if (inicio.isAfter(fim))
            throw new DataInvalidaException("Data inicial nao pode ser posterior aa data final.");

        BigDecimal resultado = horista.getHorasNormais(inicio, fim);
        return resultado.stripTrailingZeros().toPlainString().replace(".", ",");
    }

    public String getHorasExtras(HashMap<String, Empregado> empregados, String emp,
                                 String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, EmpregadoNaoEHoristaException,
            DataInvalidaException, CampoNuloException {

        Horista horista = buscarHorista(empregados, emp);
        LocalDate inicio = parsearData(dataInicial, "Data inicial invalida.");
        LocalDate fim = parsearData(dataFinal, "Data final invalida.");

        if (inicio.isAfter(fim))
            throw new DataInvalidaException("Data inicial nao pode ser posterior aa data final.");

        BigDecimal resultado = horista.getHorasExtras(inicio, fim);
        return resultado.stripTrailingZeros().toPlainString().replace(".", ",");
    }
}