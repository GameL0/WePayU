package br.ufal.ic.p2.wepayu.persistencia;

import br.ufal.ic.p2.wepayu.models.Empregado;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DadosSistema {
    private Map<String, Empregado> empregados = new LinkedHashMap<>();
    private int proximoId = 1;
    private List<String> agendas = new ArrayList<>(Arrays.asList("semanal 5", "mensal $", "semanal 2 5"));

    public String gerarId() {
        String id = String.valueOf(proximoId);
        proximoId++;
        return id;
    }

    public boolean temAgenda(String descricao) {
        return agendas.contains(descricao);
    }

    public void adicionarAgenda(String descricao) {
        agendas.add(descricao);
    }

    public Map<String, Empregado> getEmpregados() {
        return empregados;
    }

    public void setEmpregados(Map<String, Empregado> empregados) {
        this.empregados = empregados;
    }

    public int getProximoId() {
        return proximoId;
    }

    public void setProximoId(int proximoId) {
        this.proximoId = proximoId;
    }

    public List<String> getAgendas() {
        return new ArrayList<>(agendas);
    }

    public void setAgendas(List<String> agendas) {
        this.agendas = new ArrayList<>(agendas);
    }
}
