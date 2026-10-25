package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.conversao.ConversorData;
import br.ufal.ic.p2.wepayu.conversao.ConversorNumero;
import br.ufal.ic.p2.wepayu.exception.data.DataInvalidaException;
import br.ufal.ic.p2.wepayu.exception.persistencia.GravarFolhaException;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.folha.Contracheque;
import br.ufal.ic.p2.wepayu.models.folha.RelatorioFolha;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class GerenciaFolha {
    private final GerenciaEmpregado empregados;

    public GerenciaFolha(GerenciaEmpregado empregados) {
        this.empregados = empregados;
    }

    public String totalFolha(String dataInformada) throws DataInvalidaException {
        LocalDate data = ConversorData.converter(dataInformada, DataInvalidaException::new);
        return ConversorNumero.formatarMoeda(montarRelatorio(data, calcular(data)).totalBruto());
    }

    public void rodaFolha(String dataInformada, String saida) throws DataInvalidaException, GravarFolhaException {
        LocalDate data = ConversorData.converter(dataInformada, DataInvalidaException::new);
        Map<Empregado, Contracheque> contracheques = calcular(data);
        String relatorio = montarRelatorio(data, contracheques).gerar();
        try {
            Files.write(Paths.get(saida), relatorio.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new GravarFolhaException(e);
        }
        contracheques.forEach((empregado, contracheque) -> empregado.registrarPagamento(data, contracheque));
    }

    private Map<Empregado, Contracheque> calcular(LocalDate data) {
        Map<Empregado, Contracheque> contracheques = new LinkedHashMap<>();
        for (Empregado empregado : empregados.listar()) {
            if (empregado.ehDiaDePagamento(data)) {
                contracheques.put(empregado, empregado.gerarContracheque(data));
            }
        }
        return contracheques;
    }

    private RelatorioFolha montarRelatorio(LocalDate data, Map<Empregado, Contracheque> contracheques) {
        RelatorioFolha relatorio = new RelatorioFolha(data);
        contracheques.values().forEach(contracheque -> contracheque.adicionarEm(relatorio));
        return relatorio;
    }
}
