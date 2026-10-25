package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.persistencia.Persistencia;
import br.ufal.ic.p2.wepayu.services.GerenciaAgenda;
import br.ufal.ic.p2.wepayu.services.GerenciaAlteracao;
import br.ufal.ic.p2.wepayu.services.GerenciaCartaoDePonto;
import br.ufal.ic.p2.wepayu.services.GerenciaEmpregado;
import br.ufal.ic.p2.wepayu.services.GerenciaFolha;
import br.ufal.ic.p2.wepayu.services.GerenciaSindicato;
import br.ufal.ic.p2.wepayu.services.GerenciaVendas;
import br.ufal.ic.p2.wepayu.services.Sistema;

public class Facade {
    private final Sistema sistema;
    private final GerenciaEmpregado empregados;
    private final GerenciaCartaoDePonto cartoes;
    private final GerenciaVendas vendas;
    private final GerenciaSindicato sindicato;
    private final GerenciaAgenda agendas;
    private final GerenciaAlteracao alteracoes;
    private final GerenciaFolha folha;

    public Facade() throws WePayUException {
        sistema = new Sistema(new Persistencia("dados.xml"));
        empregados = new GerenciaEmpregado(sistema);
        cartoes = new GerenciaCartaoDePonto(empregados);
        vendas = new GerenciaVendas(empregados);
        sindicato = new GerenciaSindicato(empregados);
        agendas = new GerenciaAgenda(sistema);
        alteracoes = new GerenciaAlteracao(empregados, agendas);
        folha = new GerenciaFolha(empregados);
    }

    public void zerarSistema() throws WePayUException {
        sistema.zerar();
    }

    public void undo() throws WePayUException {
        sistema.desfazer();
    }

    public void redo() throws WePayUException {
        sistema.refazer();
    }

    public void encerrarSistema() throws WePayUException {
        sistema.encerrar();
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario) throws WePayUException {
        return sistema.executarComRetorno(() -> empregados.criar(nome, endereco, tipo, salario));
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) throws WePayUException {
        return sistema.executarComRetorno(() -> empregados.criar(nome, endereco, tipo, salario, comissao));
    }

    public void removerEmpregado(String emp) throws WePayUException {
        sistema.executar(() -> empregados.remover(emp));
    }

    public String getAtributoEmpregado(String emp, String atributo) throws WePayUException {
        return empregados.getAtributoEmpregado(emp, atributo);
    }

    public String getEmpregadoPorNome(String nome, String indice) throws WePayUException {
        return empregados.getEmpregadoPorNome(nome, indice);
    }

    public String getNumeroDeEmpregados() {
        return empregados.contar();
    }

    public void alteraEmpregado(String emp, String atributo, String valor) throws WePayUException {
        sistema.executar(() -> alteracoes.alterar(emp, atributo, valor));
    }

    public void alteraEmpregado(String emp, String atributo, String valor, String valorExtra) throws WePayUException {
        sistema.executar(() -> alteracoes.alterar(emp, atributo, valor, valorExtra));
    }

    public void alteraEmpregado(String emp, String atributo, String valor, String idSindicato, String taxaSindical) throws WePayUException {
        sistema.executar(() -> sindicato.alterarSindicalizacao(emp, atributo, valor, idSindicato, taxaSindical));
    }

    public void alteraEmpregado(String emp, String atributo, String valor, String banco, String agencia, String contaCorrente) throws WePayUException {
        sistema.executar(() -> alteracoes.alterar(emp, atributo, valor, banco, agencia, contaCorrente));
    }

    public void lancaCartao(String emp, String data, String horas) throws WePayUException {
        sistema.executar(() -> cartoes.lancaCartao(emp, data, horas));
    }

    public String getHorasNormaisTrabalhadas(String emp, String dataInicial, String dataFinal) throws WePayUException {
        return cartoes.getHorasNormaisTrabalhadas(emp, dataInicial, dataFinal);
    }

    public String getHorasExtrasTrabalhadas(String emp, String dataInicial, String dataFinal) throws WePayUException {
        return cartoes.getHorasExtrasTrabalhadas(emp, dataInicial, dataFinal);
    }

    public void lancaVenda(String emp, String data, String valor) throws WePayUException {
        sistema.executar(() -> vendas.lancaVenda(emp, data, valor));
    }

    public String getVendasRealizadas(String emp, String dataInicial, String dataFinal) throws WePayUException {
        return vendas.getVendasRealizadas(emp, dataInicial, dataFinal);
    }

    public void lancaTaxaServico(String membro, String data, String valor) throws WePayUException {
        sistema.executar(() -> sindicato.lancaTaxaServico(membro, data, valor));
    }

    public String getTaxasServico(String emp, String dataInicial, String dataFinal) throws WePayUException {
        return sindicato.getTaxasServico(emp, dataInicial, dataFinal);
    }

    public void criarAgendaDePagamentos(String descricao) throws WePayUException {
        sistema.executar(() -> agendas.criar(descricao));
    }

    public String totalFolha(String data) throws WePayUException {
        return folha.totalFolha(data);
    }

    public void rodaFolha(String data, String saida) throws WePayUException {
        sistema.executar(() -> folha.rodaFolha(data, saida));
    }
}
