package br.ufal.ic.p2.wepayu.utils;

import br.ufal.ic.p2.wepayu.models.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;

public class XMLHelper {

    public static void salvar(HashMap<String, Empregado> empregados, int identInt) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element raiz = doc.createElement("sistema");
            raiz.setAttribute("proximoId", String.valueOf(identInt));
            doc.appendChild(raiz);

            for (String id : empregados.keySet()) {
                Empregado emp = empregados.get(id);
                Element elemento = doc.createElement("empregado");
                elemento.setAttribute("id", id);
                elemento.setAttribute("nome", emp.getNome());
                elemento.setAttribute("endereco", emp.getEndereco());
                elemento.setAttribute("salario", emp.getSalario().toString());
                elemento.setAttribute("tipo", emp.getTipo());

                emp.adicionarDetalhesXML(doc, elemento);

                if (emp.isSindicalizado()) {
                    elemento.setAttribute("sindicalizado", "true");
                    elemento.setAttribute("idSindicato", emp.getMembroSindicato().getIdSindicato());
                    elemento.setAttribute("taxaSindical", emp.getMembroSindicato().getTaxaSindical().toString());
                    for (TaxaServico taxa : emp.getMembroSindicato().getTaxasServico()) {
                        Element elemTaxa = doc.createElement("taxa");
                        elemTaxa.setAttribute("data", taxa.getData().toString());
                        elemTaxa.setAttribute("valor", taxa.getValor().toString());
                        elemento.appendChild(elemTaxa);
                    }
                } else {
                    elemento.setAttribute("sindicalizado", "false");
                }

                raiz.appendChild(elemento);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(new File("dados.xml")));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String, Empregado> carregar() {
        HashMap<String, Empregado> empregados = new HashMap<>();
        File arquivo = new File("dados.xml");

        if (!arquivo.exists()) return empregados;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(arquivo);

            NodeList lista = doc.getElementsByTagName("empregado");

            for (int i = 0; i < lista.getLength(); i++) {
                Element elemento = (Element) lista.item(i);
                String id = elemento.getAttribute("id");
                String nome = elemento.getAttribute("nome");
                String endereco = elemento.getAttribute("endereco");
                BigDecimal salario = new BigDecimal(elemento.getAttribute("salario"));
                String tipo = elemento.getAttribute("tipo");

                Empregado emp;
                if (tipo.equals("comissionado")) {
                    BigDecimal comissao = new BigDecimal(elemento.getAttribute("comissao"));
                    emp = new Comissionado(nome, endereco, salario, comissao);
                } else if (tipo.equals("horista")) {
                    emp = new Horista(nome, endereco, salario);
                } else {
                    emp = new Assalariado(nome, endereco, salario);
                }

                emp.carregarDetalhesXML(elemento);

                if (elemento.getAttribute("sindicalizado").equals("true")) {
                    String idSindicato = elemento.getAttribute("idSindicato");
                    BigDecimal taxaSindical = new BigDecimal(elemento.getAttribute("taxaSindical"));
                    MembroSindicato membro = new MembroSindicato(idSindicato, taxaSindical);

                    NodeList taxas = elemento.getElementsByTagName("taxa");
                    for (int j = 0; j < taxas.getLength(); j++) {
                        Element elemTaxa = (Element) taxas.item(j);
                        java.time.LocalDate data = java.time.LocalDate.parse(elemTaxa.getAttribute("data"));
                        BigDecimal valor = new BigDecimal(elemTaxa.getAttribute("valor"));
                        membro.lancaTaxaServico(data, valor);
                    }
                    emp.setMembroSindicato(membro);
                }

                empregados.put(id, emp);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return empregados;
    }

    public static int carregarProximoId() {
        File arquivo = new File("dados.xml");
        if (!arquivo.exists()) return 1;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(arquivo);
            Element raiz = doc.getDocumentElement();
            return Integer.parseInt(raiz.getAttribute("proximoId"));
        } catch (Exception e) {
            return 1;
        }
    }

    public static String salvarParaString(HashMap<String, Empregado> empregados, int identInt) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element raiz = doc.createElement("sistema");
            raiz.setAttribute("proximoId", String.valueOf(identInt));
            doc.appendChild(raiz);

            for (String id : empregados.keySet()) {
                Empregado emp = empregados.get(id);
                Element elemento = doc.createElement("empregado");
                elemento.setAttribute("id", id);
                elemento.setAttribute("nome", emp.getNome());
                elemento.setAttribute("endereco", emp.getEndereco());
                elemento.setAttribute("salario", emp.getSalario().toString());
                elemento.setAttribute("tipo", emp.getTipo());

                emp.adicionarDetalhesXML(doc, elemento);

                if (emp.isSindicalizado()) {
                    elemento.setAttribute("sindicalizado", "true");
                    elemento.setAttribute("idSindicato", emp.getMembroSindicato().getIdSindicato());
                    elemento.setAttribute("taxaSindical", emp.getMembroSindicato().getTaxaSindical().toString());
                    for (TaxaServico taxa : emp.getMembroSindicato().getTaxasServico()) {
                        Element elemTaxa = doc.createElement("taxa");
                        elemTaxa.setAttribute("data", taxa.getData().toString());
                        elemTaxa.setAttribute("valor", taxa.getValor().toString());
                        elemento.appendChild(elemTaxa);
                    }
                } else {
                    elemento.setAttribute("sindicalizado", "false");
                }

                raiz.appendChild(elemento);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            java.io.StringWriter writer = new java.io.StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.getBuffer().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String, Empregado> carregarDeString(String xml) {
        HashMap<String, Empregado> empregados = new HashMap<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));

            NodeList lista = doc.getElementsByTagName("empregado");

            for (int i = 0; i < lista.getLength(); i++) {
                Element elemento = (Element) lista.item(i);
                String id = elemento.getAttribute("id");
                String nome = elemento.getAttribute("nome");
                String endereco = elemento.getAttribute("endereco");
                BigDecimal salario = new BigDecimal(elemento.getAttribute("salario"));
                String tipo = elemento.getAttribute("tipo");

                Empregado emp;
                if (tipo.equals("comissionado")) {
                    BigDecimal comissao = new BigDecimal(elemento.getAttribute("comissao"));
                    emp = new Comissionado(nome, endereco, salario, comissao);
                } else if (tipo.equals("horista")) {
                    emp = new Horista(nome, endereco, salario);
                } else {
                    emp = new Assalariado(nome, endereco, salario);
                }

                emp.carregarDetalhesXML(elemento);

                if (elemento.getAttribute("sindicalizado").equals("true")) {
                    String idSindicato = elemento.getAttribute("idSindicato");
                    BigDecimal taxaSindical = new BigDecimal(elemento.getAttribute("taxaSindical"));
                    MembroSindicato membro = new MembroSindicato(idSindicato, taxaSindical);

                    NodeList taxas = elemento.getElementsByTagName("taxa");
                    for (int j = 0; j < taxas.getLength(); j++) {
                        Element elemTaxa = (Element) taxas.item(j);
                        java.time.LocalDate data = java.time.LocalDate.parse(elemTaxa.getAttribute("data"));
                        BigDecimal valor = new BigDecimal(elemTaxa.getAttribute("valor"));
                        membro.lancaTaxaServico(data, valor);
                    }
                    emp.setMembroSindicato(membro);
                }

                empregados.put(id, emp);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return empregados;
    }

    public static int extrairProximoIdDeString(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(xml)));
            Element raiz = doc.getDocumentElement();
            return Integer.parseInt(raiz.getAttribute("proximoId"));
        } catch (Exception e) {
            return 1;
        }
    }
}