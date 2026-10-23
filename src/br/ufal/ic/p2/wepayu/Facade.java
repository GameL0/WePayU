package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.services.GerenciaCartaoDePonto;
import br.ufal.ic.p2.wepayu.services.GerenciaEmpregado;
import br.ufal.ic.p2.wepayu.services.GerenciaSindicato;
import br.ufal.ic.p2.wepayu.services.GerenciaVendas;

public class Facade {
    private final GerenciaEmpregado empregados;
    private final GerenciaCartaoDePonto cartoes;
    private final GerenciaVendas vendas;
    private final GerenciaSindicato sindicato;

    public Facade() throws WePayUException {
        empregados = new GerenciaEmpregado();
        cartoes = new GerenciaCartaoDePonto(empregados);
        vendas = new GerenciaVendas(empregados);
        sindicato = new GerenciaSindicato(empregados);
    }

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

    public String getEmpregadoPorNome(String nome, String indiceInformado) throws WePayUException {
        return empregados.getEmpregadoPorNome(nome, indiceInformado);
    }

    public void alteraEmpregado(String emp, String atributo, String valor) throws WePayUException {
        empregados.alterar(emp, atributo, valor);
    }

    public void alteraEmpregado(String emp, String atributo, String valor, String idSindicato, String taxaSindical) throws WePayUException {
        sindicato.alterarSindicalizacao(emp, atributo, valor, idSindicato, taxaSindical);
    }

    public void lancaCartao(String emp, String data, String horas) throws WePayUException {
        cartoes.lancaCartao(emp, data, horas);
    }

    public String getHorasNormaisTrabalhadas(String emp, String dataInicial, String dataFinal) throws WePayUException {
        return cartoes.getHorasNormaisTrabalhadas(emp, dataInicial, dataFinal);
    }

    public String getHorasExtrasTrabalhadas(String emp, String dataInicial, String dataFinal) throws WePayUException {
        return cartoes.getHorasExtrasTrabalhadas(emp, dataInicial, dataFinal);
    }

    public void lancaVenda(String emp, String data, String valor) throws WePayUException {
        vendas.lancaVenda(emp, data, valor);
    }

    public String getVendasRealizadas(String emp, String dataInicial, String dataFinal) throws WePayUException {
        return vendas.getVendasRealizadas(emp, dataInicial, dataFinal);
    }

    public void lancaTaxaServico(String membro, String data, String valor) throws WePayUException {
        sindicato.lancaTaxaServico(membro, data, valor);
    }

    public String getTaxasServico(String emp, String dataInicial, String dataFinal) throws WePayUException {
        return sindicato.getTaxasServico(emp, dataInicial, dataFinal);
    }

    public void encerrarSistema() throws WePayUException {
        empregados.encerrarSistema();
    }
}
