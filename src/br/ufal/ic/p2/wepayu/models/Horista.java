package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.negativo.HorasNaoPositivasException;
import br.ufal.ic.p2.wepayu.exception.negativo.ValorNegativoException;
import br.ufal.ic.p2.wepayu.exception.nulo.ValorNuloException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Horista extends Empregado {
    private List<CartaoDePonto> cartoes = new ArrayList<>();

    public Horista() {
    }

    public Horista(String nome, String endereco, BigDecimal salario) throws ValorNuloException, ValorNegativoException {
        super(nome, endereco, salario);
    }

    @Override
    public String getTipo() {
        return "horista";
    }

    @Override
    public Horista comoHorista() {
        return this;
    }

    public void lancaCartaoDePonto(LocalDate data, BigDecimal horas) throws HorasNaoPositivasException {
        cartoes.add(new CartaoDePonto(data, horas));
    }

    public BigDecimal horasNormais(Periodo periodo) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartaoDePonto cartao : cartoes) {
            if (cartao.estaEm(periodo)) {
                total = total.add(cartao.horasNormais());
            }
        }
        return total;
    }

    public BigDecimal horasExtras(Periodo periodo) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartaoDePonto cartao : cartoes) {
            if (cartao.estaEm(periodo)) {
                total = total.add(cartao.horasExtras());
            }
        }
        return total;
    }

    public List<CartaoDePonto> getCartoes() {
        return new ArrayList<>(cartoes);
    }

    public void setCartoes(List<CartaoDePonto> cartoes) {
        this.cartoes = new ArrayList<>(cartoes);
    }
}
