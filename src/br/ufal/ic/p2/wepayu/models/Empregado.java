package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEComissionadoException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoESindicalizadoException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoRecebeEmBancoException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEHoristaException;
import java.time.LocalDate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.math.BigDecimal;


public abstract class Empregado {
    private String nome;
    private String endereco;
    private BigDecimal salario;
    private MembroSindicato membroSindicato;
    private String metodoPagamento = "emMaos";
    private String banco;
    private String agencia;
    private String contaCorrente;


    public Empregado(String nome, String endereco, BigDecimal salario){
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public String getNome() {return nome;}
    public String getEndereco() {return endereco;}
    public BigDecimal getSalario() {return salario;}
    public MembroSindicato getMembroSindicato() {return membroSindicato;}
    public void setMembroSindicato(MembroSindicato membroSindicato) {this.membroSindicato = membroSindicato;}

    public boolean isSindicalizado(){ return membroSindicato != null;}

    public abstract String getTipo();

    public abstract boolean isDiaDePagamento(LocalDate dataFolha);

    private LocalDate dataUltimoPagamento;

    public LocalDate getDataUltimoPagamento() { return dataUltimoPagamento; }
    public void setDataUltimoPagamento(LocalDate dataUltimoPagamento) { this.dataUltimoPagamento = dataUltimoPagamento; }

    private BigDecimal dividaSindical = BigDecimal.ZERO;
    public BigDecimal getDividaSindical() { return dividaSindical; }
    public void setDividaSindical(BigDecimal dividaSindical) { this.dividaSindical = dividaSindical; }

    public abstract LocalDate getDataContrato();

    public long getDiasParaPagamentoSindical(LocalDate dataFolha) {
        LocalDate referencia = dataUltimoPagamento;
        if (referencia == null) {
            referencia = getDataContrato().minusDays(1);
        }
        return java.time.temporal.ChronoUnit.DAYS.between(referencia, dataFolha);
    }

    public String getAtributo(String atributo) throws Exception {
        switch (atributo){
            case "nome": return getNome();
            case "endereco": return getEndereco();
            case "salario": return getSalario().setScale(2).toString().replace(".", ",");

            case "tipo": return getTipo();
            case "sindicalizado": return String.valueOf(isSindicalizado());
            case "idSindicato":
                if (isSindicalizado()) return membroSindicato.getIdSindicato();
                throw new EmpregadoNaoESindicalizadoException();
            case "taxaSindical":
                if (isSindicalizado()) return membroSindicato.getTaxaSindical().setScale(2).toString().replace(".", ",");
                throw new EmpregadoNaoESindicalizadoException();
            case "metodoPagamento": return getMetodoPagamento();
            case "banco":
                if (!metodoPagamento.equals("banco")) throw new EmpregadoNaoRecebeEmBancoException();
                return getBanco();
            case "agencia":
                if (!metodoPagamento.equals("banco")) throw new EmpregadoNaoRecebeEmBancoException();
                return getAgencia();
            case "contaCorrente":
                if (!metodoPagamento.equals("banco")) throw new EmpregadoNaoRecebeEmBancoException();
                return getContaCorrente();
            case"comissao":
                throw new EmpregadoNaoEComissionadoException();
            default: return null;
        }
    }

    public void setNome(String nome) { this.nome = nome; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }


    public void setMetodoPagamento(String metodoPagamento) { this.metodoPagamento = metodoPagamento; }
    public void setBanco(String banco) { this.banco = banco; }
    public void setAgencia(String agencia) { this.agencia = agencia; }
    public void setContaCorrente(String contaCorrente) { this.contaCorrente = contaCorrente; }

    public String getMetodoPagamento() { return  metodoPagamento; }
    public String getBanco() { return banco; }
    public String getAgencia() { return agencia; }
    public String getContaCorrente() { return contaCorrente; }


    public void adicionarDetalhesXML(Document doc, Element elemento) {
    }

    public void carregarDetalhesXML(Element elemento) {
    }

    public void lancaCartao(LocalDate data, BigDecimal horas) throws EmpregadoNaoEHoristaException {
        throw new EmpregadoNaoEHoristaException();
    }

    public BigDecimal getHorasNormais(LocalDate inicio, LocalDate fim) throws EmpregadoNaoEHoristaException {
        throw new EmpregadoNaoEHoristaException();
    }

    public BigDecimal getHorasExtras(LocalDate inicio, LocalDate fim) throws EmpregadoNaoEHoristaException {
        throw new EmpregadoNaoEHoristaException();
    }

    public void lancaVenda(LocalDate data, BigDecimal valor) throws EmpregadoNaoEComissionadoException {
        throw new EmpregadoNaoEComissionadoException();
    }

    public BigDecimal getVendasRealizadas(LocalDate inicio, LocalDate fim) throws EmpregadoNaoEComissionadoException {
        throw new EmpregadoNaoEComissionadoException();
    }

    public void setComissao(BigDecimal comissao) throws EmpregadoNaoEComissionadoException {
        throw new EmpregadoNaoEComissionadoException();
    }
}
