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

import java.math.BigDecimal;
import java.util.Objects;

public abstract class Empregado {
    private String nome;
    private String endereco;
    private BigDecimal salario;
    private Filiacao filiacao = new NaoSindicalizado();

    protected Empregado() {
    }

    public Empregado(String nome, String endereco, BigDecimal salario) throws ValorNuloException, ValorNegativoException {
        if (nome == null || nome.isEmpty()) {
            throw new NomeNuloException();
        }
        if (endereco == null || endereco.isEmpty()) {
            throw new EnderecoNuloException();
        }
        if (salario.compareTo(BigDecimal.ZERO) < 0) {
            throw new SalarioNegativoException();
        }
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public abstract String getTipo();

    public String getAtributo(String atributo) throws AtributoNaoExisteException, EmpregadoNaoEhSindicalizadoException {
        switch (atributo) {
            case "nome": return nome;
            case "endereco": return endereco;
            case "tipo": return getTipo();
            case "salario": return ConversorNumero.formatarMoeda(salario);
            case "sindicalizado": return String.valueOf(filiacao.ehSindicalizado());
            case "idSindicato": return comoMembroSindicato().getIdSindicato();
            case "taxaSindical": return ConversorNumero.formatarMoeda(comoMembroSindicato().getTaxaSindical());
            default: throw new AtributoNaoExisteException();
        }
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

    public MembroSindicato comoMembroSindicato() throws EmpregadoNaoEhSindicalizadoException {
        return filiacao.comoMembro();
    }

    public Horista comoHorista() throws EmpregadoNaoEhHoristaException {
        throw new EmpregadoNaoEhHoristaException();
    }

    public Comissionado comoComissionado() throws EmpregadoNaoEhComissionadoException {
        throw new EmpregadoNaoEhComissionadoException();
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }

    public Filiacao getFiliacao() { return filiacao; }
    public void setFiliacao(Filiacao filiacao) { this.filiacao = filiacao; }
}
