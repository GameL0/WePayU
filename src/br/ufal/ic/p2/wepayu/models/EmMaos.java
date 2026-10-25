package br.ufal.ic.p2.wepayu.models;

public class EmMaos extends MetodoPagamento {
    @Override
    public String descricao() {
        return "emMaos";
    }

    @Override
    public String descricaoNaFolha(String endereco) {
        return "Em maos";
    }
}
