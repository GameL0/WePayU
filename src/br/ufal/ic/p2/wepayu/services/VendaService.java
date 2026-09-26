package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.Exception.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.HashMap;


public class VendaService {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("d/M/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    private Empregado buscarEmpregado(HashMap<String, Empregado> empregados, String emp)
            throws CampoNuloException, EmpregadoNaoExisteException {
        if(emp.isEmpty()) throw new CampoNuloException("Identificacao do empregado nao pode ser nula.");
        Empregado empregado = empregados.get(emp);
        if (empregado == null) throw new EmpregadoNaoExisteException();
        return empregado;
    }

    private LocalDate parsearData(String data, String mensagemErro) throws DataInvalidaException{
        try {
            return LocalDate.parse(data, FORMATO);
        } catch (Exception e){
            throw new DataInvalidaException(mensagemErro);
        }
    }

    public void lancaVenda(HashMap<String, Empregado> empregados, String emp, String data, String valor)
            throws CampoNuloException, EmpregadoNaoExisteException, EmpregadoNaoEComissionadoException, DataInvalidaException,
            ValorPositivoException{

        Empregado empregado = buscarEmpregado(empregados, emp);
        LocalDate dataConvertida = parsearData(data, "Data invalida.");

        BigDecimal valorDecimal = new BigDecimal(valor.replace(",", "."));
        if (valorDecimal.compareTo(BigDecimal.ZERO) <= 0) throw new ValorPositivoException();

        empregado.lancaVenda(dataConvertida, valorDecimal);
    }

    public String getVendasRealizadas(HashMap<String, Empregado> empregados, String emp,
                                      String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, EmpregadoNaoEComissionadoException,
            DataInvalidaException, CampoNuloException {

        Empregado empregado = buscarEmpregado(empregados, emp);
        LocalDate inicio = parsearData(dataInicial, "Data inicial invalida.");
        LocalDate fim = parsearData(dataFinal, "Data final invalida.");

        if (inicio.isAfter(fim))
            throw new DataInvalidaException("Data inicial nao pode ser posterior aa data final.");

        BigDecimal resultado = empregado.getVendasRealizadas(inicio, fim);
        return resultado.setScale(2).toString().replace(".", ",");
    }
}