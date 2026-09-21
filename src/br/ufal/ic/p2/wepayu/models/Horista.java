package br.ufal.ic.p2.wepayu.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Horista extends Empregado {
    private List<CartaoDePonto> cartoes = new ArrayList<>();

    public Horista(String nome, String endereco, BigDecimal salario) {
        super(nome, endereco, salario);
    }

    @Override
    public String getTipo() {
        return "horista";
    }

    public void lancaCartao(LocalDate data, BigDecimal horas) {
        cartoes.add(new CartaoDePonto(data, horas));
    }

    public BigDecimal getHorasNormais(LocalDate dataInicial, LocalDate dataFinal) {
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal limite = new BigDecimal("8");

        for (CartaoDePonto cartao : cartoes) {
            if (!cartao.getData().isBefore(dataInicial) && cartao.getData().isBefore(dataFinal)) {
                total = total.add(cartao.getHoras().min(limite));
            }
        }
        return total;
    }

    public BigDecimal getHorasExtras(LocalDate dataInicial, LocalDate dataFinal) {
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal limite = new BigDecimal("8");

        for (CartaoDePonto cartao : cartoes) {
            if (!cartao.getData().isBefore(dataInicial) && cartao.getData().isBefore(dataFinal)) {
                BigDecimal extras = cartao.getHoras().subtract(limite);
                if (extras.compareTo(BigDecimal.ZERO) > 0) {
                    total = total.add(extras);
                }
            }
        }
        return total;
    }

    public List<CartaoDePonto> getCartoes() { return cartoes; }
}