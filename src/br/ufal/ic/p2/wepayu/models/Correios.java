package br.ufal.ic.p2.wepayu.models;

public class Correios extends MetodoPagamento {
    @Override
    public String descricao() {
        return "correios";
    }

    @Override
    public String descricaoNaFolha(String endereco) {
        return "Correios, " + endereco;
    }
}
