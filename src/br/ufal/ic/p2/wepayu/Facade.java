package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.services.GerenciaEmpregado;


public class Facade {
    private GerenciaEmpregado empregados = new GerenciaEmpregado();

    public void zerarSistema(){
        empregados.zerarSistema();
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salarioInformado) throws WePayUException {
        return empregados.criar(nome, endereco, tipo, salarioInformado);
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salarioInformado, String comissaoInformado) throws WePayUException {
        return empregados.criar(nome, endereco, tipo, salarioInformado, comissaoInformado);
    }

    public void removerEmpregado(String id) throws WePayUException {
        empregados.remover(id);
    }

    public String getAtributoEmpregado(String id, String atributo) throws WePayUException {
        return empregados.getAtributoEmpregado(id, atributo);
    }

    public void encerrarSistema(){
        empregados.encerrarSistema();
    }
}
