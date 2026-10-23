package br.ufal.ic.p2.wepayu.persistencia;

import br.ufal.ic.p2.wepayu.models.Empregado;

import java.util.LinkedHashMap;
import java.util.Map;

public class DadosSistema {
    private Map<String, Empregado> empregados = new LinkedHashMap<>();
    private int proximoId = 1;
    public DadosSistema() {
    }

    public DadosSistema(Map<String, Empregado> empregados, int proximoId) {
        this.empregados = empregados;
        this.proximoId = proximoId;
    }

    public Map<String, Empregado> getEmpregados() { return empregados; }

    public void setEmpregados(Map<String, Empregado> empregados) {this.empregados = empregados;}

    public int getProximoId() { return proximoId; }
    public void setProximoId(int proximoId) { this.proximoId = proximoId; }
}
