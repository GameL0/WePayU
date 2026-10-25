package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.naoencontrado.AtributoNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.nulo.AgenciaNulaException;
import br.ufal.ic.p2.wepayu.exception.nulo.BancoNuloException;
import br.ufal.ic.p2.wepayu.exception.nulo.ContaCorrenteNulaException;
import br.ufal.ic.p2.wepayu.exception.nulo.ValorNuloException;

public class Banco extends MetodoPagamento {
    private final String banco;
    private final String agencia;
    private final String contaCorrente;

    public Banco(String banco, String agencia, String contaCorrente) throws ValorNuloException {
        if (estaVazio(banco)) {
            throw new BancoNuloException();
        }
        if (estaVazio(agencia)) {
            throw new AgenciaNulaException();
        }
        if (estaVazio(contaCorrente)) {
            throw new ContaCorrenteNulaException();
        }
        this.banco = banco;
        this.agencia = agencia;
        this.contaCorrente = contaCorrente;
    }

    @Override
    public String descricao() {
        return "banco";
    }

    @Override
    public String descricaoNaFolha(String endereco) {
        return banco + ", Ag. " + agencia + " CC " + contaCorrente;
    }

    @Override
    public String getAtributo(String atributo) throws AtributoNaoExisteException {
        switch (atributo) {
            case "banco": return banco;
            case "agencia": return agencia;
            case "contaCorrente": return contaCorrente;
            default: throw new AtributoNaoExisteException();
        }
    }

    private static boolean estaVazio(String texto) {
        return texto == null || texto.isEmpty();
    }

    public String getBanco() {
        return banco;
    }

    public String getAgencia() {
        return agencia;
    }

    public String getContaCorrente() {
        return contaCorrente;
    }
}
