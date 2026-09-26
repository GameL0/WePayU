package br.ufal.ic.p2.wepayu.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Horista extends Empregado {
    private List<CartaoDePonto> cartoes = new ArrayList<>();

    public Horista(String nome, String endereco, BigDecimal salario) {
        super(nome, endereco, salario);
    }

    @Override
    public String getTipo() {
        return "horista";
    }

    @Override
    public boolean isDiaDePagamento(LocalDate dataFolha) {
        return dataFolha.getDayOfWeek().getValue() == 5;
    }

    @Override
    public LocalDate getDataContrato() {
        if (cartoes.isEmpty()) return LocalDate.of(2005, 1, 1);
        LocalDate min = cartoes.get(0).getData();
        for (CartaoDePonto c : cartoes) {
            if (c.getData().isBefore(min)) min = c.getData();
        }
        return min;
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

    @Override
    public void adicionarDetalhesXML(Document doc, Element elemento) {
        for (CartaoDePonto cartao : cartoes) {
            Element elemCartao = doc.createElement("cartao");
            elemCartao.setAttribute("data", cartao.getData().toString());
            elemCartao.setAttribute("horas", cartao.getHoras().toString());
            elemento.appendChild(elemCartao);
        }
    }

    @Override
    public void carregarDetalhesXML(Element elemento) {
        org.w3c.dom.NodeList cartoesList = elemento.getElementsByTagName("cartao");
        for (int j = 0; j < cartoesList.getLength(); j++) {
            Element elemCartao = (Element) cartoesList.item(j);
            LocalDate data = LocalDate.parse(elemCartao.getAttribute("data"));
            BigDecimal horas = new BigDecimal(elemCartao.getAttribute("horas"));
            lancaCartao(data, horas);
        }
    }
}