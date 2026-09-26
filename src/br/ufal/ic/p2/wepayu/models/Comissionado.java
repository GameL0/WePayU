package br.ufal.ic.p2.wepayu.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Comissionado extends Empregado {
    private BigDecimal comissao;
    private List<ResultadoVenda> vendas = new ArrayList<>();

    public Comissionado(String nome, String endereco, BigDecimal salario, BigDecimal comissao){
        super(nome, endereco, salario);
        this.comissao = comissao;
    }

    public BigDecimal getComissao() {return comissao;}

    public void lancaVenda(LocalDate data, BigDecimal valor){
        vendas.add(new ResultadoVenda(data, valor));
    }

    public BigDecimal getVendasRealizadas(LocalDate dataInicial, LocalDate dataFinal){
        BigDecimal total = BigDecimal.ZERO;
        for (ResultadoVenda venda : vendas){
            if(!venda.getData().isBefore(dataInicial) && venda.getData().isBefore(dataFinal)){
                total = total.add(venda.getValor());
            }
        }

        return total;
    }

    public List<ResultadoVenda> getVendas() {return vendas;}

    @Override
    public String getTipo(){
        return "comissionado";
    }

    @Override
    public boolean isDiaDePagamento(LocalDate dataFolha) {
        if (dataFolha.getDayOfWeek().getValue() != 5) return false;
        LocalDate inicio = LocalDate.of(2005, 1, 1);
        long semanas = java.time.temporal.ChronoUnit.WEEKS.between(inicio, dataFolha);
        return (semanas % 2 != 0);
    }

    @Override
    public LocalDate getDataContrato() {
        return LocalDate.of(2005, 1, 1);
    }

    @Override
    public String getAtributo(String atributo) throws Exception {
        if (atributo.equals("comissao")) {
            return getComissao().setScale(2).toString().replace(".", ",");
        }
        return super.getAtributo(atributo);
    }

    public void setComissao(BigDecimal comissao) { this.comissao = comissao; }

    @Override
    public void adicionarDetalhesXML(Document doc, Element elemento) {
        elemento.setAttribute("comissao", getComissao().toString());
        for (ResultadoVenda venda : vendas) {
            Element elemVenda = doc.createElement("venda");
            elemVenda.setAttribute("data", venda.getData().toString());
            elemVenda.setAttribute("valor", venda.getValor().toString());
            elemento.appendChild(elemVenda);
        }
    }

    @Override
    public void carregarDetalhesXML(Element elemento) {
        org.w3c.dom.NodeList vendasList = elemento.getElementsByTagName("venda");
        for (int j = 0; j < vendasList.getLength(); j++) {
            Element elemVenda = (Element) vendasList.item(j);
            LocalDate data = LocalDate.parse(elemVenda.getAttribute("data"));
            BigDecimal valor = new BigDecimal(elemVenda.getAttribute("valor"));
            lancaVenda(data, valor);
        }
    }
}
