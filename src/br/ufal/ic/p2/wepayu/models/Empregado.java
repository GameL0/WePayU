package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.conversao.ConversorNumero;
import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.exception.naoencontrado.AtributoNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.negativo.SalarioNegativoException;
import br.ufal.ic.p2.wepayu.exception.negativo.ValorNegativoException;
import br.ufal.ic.p2.wepayu.exception.nulo.EnderecoNuloException;
import br.ufal.ic.p2.wepayu.exception.nulo.NomeNuloException;
import br.ufal.ic.p2.wepayu.exception.nulo.ValorNuloException;
import br.ufal.ic.p2.wepayu.exception.sindicato.EmpregadoNaoEhSindicalizadoException;
import br.ufal.ic.p2.wepayu.exception.tipo.EmpregadoNaoEhComissionadoException;
import br.ufal.ic.p2.wepayu.exception.tipo.EmpregadoNaoEhHoristaException;
import br.ufal.ic.p2.wepayu.models.agenda.AgendaPagamento;
import br.ufal.ic.p2.wepayu.models.folha.Contracheque;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public abstract class Empregado {
    private String nome;
    private String endereco;
    private BigDecimal salario;
    private Filiacao filiacao = new NaoSindicalizado();
    private MetodoPagamento metodoPagamento = new EmMaos();
    private Historico historico = new Historico();
    private AgendaPagamento agendaPagamento = agendaPadrao();

    public Empregado(String nome, String endereco, BigDecimal salario) throws ValorNuloException, ValorNegativoException {
        alterarNome(nome);
        alterarEndereco(endereco);
        alterarSalario(salario);
    }

    public abstract String getTipo();

    protected abstract AgendaPagamento agendaPadrao();

    protected AgendaPagamento agenda() {
        return agendaPagamento;
    }

    protected abstract Contracheque criarContracheque(Periodo periodo);

    public boolean ehDiaDePagamento(LocalDate data) {
        return agenda().ehDiaDePagamento(data);
    }

    public final Contracheque gerarContracheque(LocalDate data) {
        Periodo periodo = agenda().periodoAte(data);
        Contracheque contracheque = criarContracheque(periodo);
        contracheque.descontar(filiacao.descontos(periodo).add(historico.dividaPara(data)));
        return contracheque;
    }

    public void registrarPagamento(LocalDate data, Contracheque contracheque) {
        historico.registrarFolha(data, contracheque.getDividaRestante());
    }

    protected String metodoPagamentoNaFolha() {
        return metodoPagamento.descricaoNaFolha(endereco);
    }

    public String getAtributo(String atributo) throws WePayUException {
        switch (atributo) {
            case "nome": return nome;
            case "endereco": return endereco;
            case "tipo": return getTipo();
            case "salario": return ConversorNumero.formatarMoeda(salario);
            case "comissao": throw new EmpregadoNaoEhComissionadoException();
            case "metodoPagamento": return metodoPagamento.descricao();
            case "agendaPagamento": return agendaPagamento.descricao();
            case "banco":
            case "agencia":
            case "contaCorrente": return metodoPagamento.getAtributo(atributo);
            case "sindicalizado": return String.valueOf(filiacao.ehSindicalizado());
            case "idSindicato":
            case "taxaSindical": return filiacao.getAtributo(atributo);
            default: throw new AtributoNaoExisteException();
        }
    }

    public final void alterarNome(String nome) throws NomeNuloException {
        if (nome == null || nome.isEmpty()) {
            throw new NomeNuloException();
        }
        this.nome = nome;
    }

    public final void alterarEndereco(String endereco) throws EnderecoNuloException {
        if (endereco == null || endereco.isEmpty()) {
            throw new EnderecoNuloException();
        }
        this.endereco = endereco;
    }

    public final void alterarSalario(BigDecimal salario) throws SalarioNegativoException {
        if (salario.compareTo(BigDecimal.ZERO) < 0) {
            throw new SalarioNegativoException();
        }
        this.salario = salario;
    }

    public void herdarDadosDe(Empregado anterior) {
        filiacao = anterior.filiacao;
        metodoPagamento = anterior.metodoPagamento;
        historico = anterior.historico;
    }

    public boolean temNome(String nome) {
        return Objects.equals(nome, this.nome);
    }

    public boolean temIdSindicato(String idSindicato) {
        return filiacao.temIdSindicato(idSindicato);
    }

    public void sindicalizar(String idSindicato, BigDecimal taxaSindical) throws WePayUException {
        filiacao = filiacao.sindicalizar(idSindicato, taxaSindical);
    }

    public void desfiliar() {
        filiacao = new NaoSindicalizado();
    }

    public void lancaTaxaServico(LocalDate data, BigDecimal valor) throws WePayUException {
        filiacao.lancaTaxaServico(data, valor);
    }

    public BigDecimal totalTaxasServico(Periodo periodo) throws EmpregadoNaoEhSindicalizadoException {
        return filiacao.totalTaxasServico(periodo);
    }

    public Horista comoHorista() throws EmpregadoNaoEhHoristaException {
        throw new EmpregadoNaoEhHoristaException();
    }

    public Comissionado comoComissionado() throws EmpregadoNaoEhComissionadoException {
        throw new EmpregadoNaoEhComissionadoException();
    }

    public String getNome() { return nome; }

    public String getEndereco() { return endereco; }

    public BigDecimal getSalario() { return salario; }

    public Filiacao getFiliacao() { return filiacao; }
    public void setFiliacao(Filiacao filiacao) { this.filiacao = filiacao; }

    public MetodoPagamento getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(MetodoPagamento metodoPagamento) { this.metodoPagamento = metodoPagamento; }

    public Historico getHistorico() { return historico; }
    public void setHistorico(Historico historico) { this.historico = historico; }

    public AgendaPagamento getAgendaPagamento() { return agendaPagamento; }
    public void setAgendaPagamento(AgendaPagamento agendaPagamento) { this.agendaPagamento = agendaPagamento; }
}
