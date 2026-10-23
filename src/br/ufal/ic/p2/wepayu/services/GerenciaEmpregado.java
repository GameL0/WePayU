package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.exception.numerico.ComissaoNaoNumericoException;
import br.ufal.ic.p2.wepayu.exception.nulo.ComissaoNulaException;
import br.ufal.ic.p2.wepayu.exception.naoencontrado.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.nulo.IdentificacaoNulaException;
import br.ufal.ic.p2.wepayu.exception.numerico.SalarioNaoNumericoException;
import br.ufal.ic.p2.wepayu.exception.nulo.SalarioNuloException;
import br.ufal.ic.p2.wepayu.exception.numerico.ValorNaoNumericoException;
import br.ufal.ic.p2.wepayu.exception.nulo.ValorNuloException;
import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.models.Empregado;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class GerenciaEmpregado {
    private Map<String, Empregado> empregados =  new HashMap<>();
    private int proximoId = 1;

    public void zerarSistema(){
        empregados.clear();
        proximoId = 1;
    }

    private boolean ehNumero(String valor){
        try{
            new BigDecimal(valor.replace(",", "."));
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    private BigDecimal converterSalario(String salarioInformado) throws ValorNuloException, ValorNaoNumericoException {
        if(salarioInformado == null || salarioInformado.isEmpty()){
            throw new SalarioNuloException();
        }
        if(!ehNumero(salarioInformado)){
            throw new SalarioNaoNumericoException();
        }
        return new BigDecimal(salarioInformado.replace(",", "."));
    }

    private BigDecimal converterComissao(String comissaoInformado) throws ValorNuloException, ValorNaoNumericoException {
        if(comissaoInformado == null || comissaoInformado.isEmpty()){
            throw new ComissaoNulaException();
        }
        if(!ehNumero(comissaoInformado)){
            throw new ComissaoNaoNumericoException();
        }
        return new BigDecimal(comissaoInformado.replace(",", "."));
    }

    public String criar(String nome, String endereco, String tipo, String salarioInformado) throws WePayUException {
        String id = String.valueOf(proximoId);
        BigDecimal salario = converterSalario(salarioInformado);
        Empregado empregado = EmpregadoFactory.criar(nome, endereco, tipo, salario);
        proximoId++;
        empregados.put(id, empregado);
        return id;
    }

    public String criar(String nome, String endereco, String tipo, String salarioInformado, String comissaoInformado) throws WePayUException {
        String id = String.valueOf(proximoId);
        BigDecimal salario = converterSalario(salarioInformado);
        BigDecimal comissao = converterComissao(comissaoInformado);
        Empregado empregado = EmpregadoFactory.criar(nome, endereco, tipo, salario, comissao);
        proximoId++;
        empregados.put(id, empregado);
        return id;
    }

    public Empregado buscar(String id) throws EmpregadoNaoExisteException {
        Empregado emp =  empregados.get(id);
        if(emp == null) throw new EmpregadoNaoExisteException();
        return emp;
    }

    public void remover(String id) throws EmpregadoNaoExisteException {
        buscar(id);
        empregados.remove(id);
    }

    public String getAtributoEmpregado(String id, String atributo) throws WePayUException {
        if(id == null || id.isEmpty()) throw new IdentificacaoNulaException();
        Empregado emp = buscar(id);
        return emp.getAtributo(atributo);
    }

    public void encerrarSistema(){

    }


}
