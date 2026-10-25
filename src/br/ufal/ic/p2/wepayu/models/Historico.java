package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.exception.negativo.HorasNaoPositivasException;
import br.ufal.ic.p2.wepayu.exception.negativo.ValorNaoPositivoException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Historico {
    private final List<CartaoDePonto> cartoes;
    private final List<Venda> vendas;
    private BigDecimal divida;
    private BigDecimal dividaAntesDaUltimaFolha;
    private LocalDate dataUltimaFolha;

    public Historico() {
        this(new ArrayList<>(), new ArrayList<>(), BigDecimal.ZERO, BigDecimal.ZERO, null);
    }

    public Historico(List<CartaoDePonto> cartoes, List<Venda> vendas, BigDecimal divida,
                     BigDecimal dividaAntesDaUltimaFolha, LocalDate dataUltimaFolha) {
        this.cartoes = cartoes;
        this.vendas = vendas;
        this.divida = divida;
        this.dividaAntesDaUltimaFolha = dividaAntesDaUltimaFolha;
        this.dataUltimaFolha = dataUltimaFolha;
    }

    public void lancaCartao(LocalDate data, BigDecimal horas) throws HorasNaoPositivasException {
        cartoes.add(new CartaoDePonto(data, horas));
    }

    public void lancaVenda(LocalDate data, BigDecimal valor) throws ValorNaoPositivoException {
        vendas.add(new Venda(data, valor));
    }

    public BigDecimal horasNormais(Periodo periodo) {
        return somarHoras(periodo, CartaoDePonto::horasNormais);
    }

    public BigDecimal horasExtras(Periodo periodo) {
        return somarHoras(periodo, CartaoDePonto::horasExtras);
    }

    public BigDecimal totalVendas(Periodo periodo) {
        return LancamentoMonetario.somar(vendas, periodo);
    }

    public BigDecimal dividaPara(LocalDate dataFolha) {
        return dataFolha.equals(dataUltimaFolha) ? dividaAntesDaUltimaFolha : divida;
    }

    public void registrarFolha(LocalDate dataFolha, BigDecimal dividaRestante) {
        if (!dataFolha.equals(dataUltimaFolha)) {
            dividaAntesDaUltimaFolha = divida;
            dataUltimaFolha = dataFolha;
        }
        divida = dividaRestante;
    }

    private BigDecimal somarHoras(Periodo periodo, Function<CartaoDePonto, BigDecimal> horas) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartaoDePonto cartao : cartoes) {
            if (cartao.estaEm(periodo)) {
                total = total.add(horas.apply(cartao));
            }
        }
        return total;
    }

    public List<CartaoDePonto> getCartoes() {
        return new ArrayList<>(cartoes);
    }

    public List<Venda> getVendas() {
        return new ArrayList<>(vendas);
    }

    public BigDecimal getDivida() {
        return divida;
    }

    public BigDecimal getDividaAntesDaUltimaFolha() {
        return dividaAntesDaUltimaFolha;
    }

    public LocalDate getDataUltimaFolha() {
        return dataUltimaFolha;
    }
}
