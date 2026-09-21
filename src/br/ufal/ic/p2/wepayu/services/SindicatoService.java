package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.MembroSindicato;
import br.ufal.ic.p2.wepayu.Exception.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.HashMap;

public class SindicatoService {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("d/M/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    private LocalDate parsearData(String data, String mensagemErro) throws DataInvalidaException {
        try {
            return LocalDate.parse(data, FORMATO);
        } catch (Exception e) {
            throw new DataInvalidaException(mensagemErro);
        }
    }


    private MembroSindicato buscarMembro(HashMap<String, Empregado> empregados, String idMembro)
            throws CampoNuloException, MembroNaoExisteException {

        if (idMembro.isEmpty()) throw new CampoNuloException("Identificacao do membro nao pode ser nula.");

        for (Empregado emp : empregados.values()) {
            if (emp.isSindicalizado() && emp.getMembroSindicato().getIdSindicato().equals(idMembro)) {
                return emp.getMembroSindicato();
            }
        }

        throw new MembroNaoExisteException();
    }


    public void lancaTaxaServico(HashMap<String, Empregado> empregados, String membro, String data, String valor)
            throws CampoNuloException, MembroNaoExisteException, DataInvalidaException, ValorPositivoException {

        MembroSindicato membroSindicato = buscarMembro(empregados, membro);
        LocalDate dataConvertida = parsearData(data, "Data invalida.");

        BigDecimal valorDecimal = new BigDecimal(valor.replace(",", "."));
        if (valorDecimal.compareTo(BigDecimal.ZERO) <= 0) throw new ValorPositivoException();

        membroSindicato.lancaTaxaServico(dataConvertida, valorDecimal);
    }


    public String getTaxasServico(HashMap<String, Empregado> empregados, String emp,
                                  String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, EmpregadoNaoESindicalizadoException,
            DataInvalidaException, CampoNuloException {

        if (emp.isEmpty()) throw new CampoNuloException("Identificacao do empregado nao pode ser nula.");
        Empregado empregado = empregados.get(emp);
        if (empregado == null) throw new EmpregadoNaoExisteException();
        if (!empregado.isSindicalizado()) throw new EmpregadoNaoESindicalizadoException();

        LocalDate inicio = parsearData(dataInicial, "Data inicial invalida.");
        LocalDate fim = parsearData(dataFinal, "Data final invalida.");

        if (inicio.isAfter(fim))
            throw new DataInvalidaException("Data inicial nao pode ser posterior aa data final.");

        BigDecimal resultado = empregado.getMembroSindicato().getTotalTaxasServico(inicio, fim);
        return resultado.setScale(2).toString().replace(".", ",");
    }
}