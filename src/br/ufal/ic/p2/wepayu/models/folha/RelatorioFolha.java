package br.ufal.ic.p2.wepayu.models.folha;

import br.ufal.ic.p2.wepayu.conversao.ConversorNumero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class RelatorioFolha {
    private static final String LINHA = "===============================================================================================================================";
    private static final String FORMATO_HORISTA = "%-36s %5s %5s %13s %9s %15s %s";
    private static final String FORMATO_ASSALARIADO = "%-48s %13s %9s %15s %s";
    private static final String FORMATO_COMISSIONADO = "%-21s %8s %8s %8s %13s %9s %15s %s";

    private final LocalDate data;
    private final List<ContrachequeHorista> horistas = new ArrayList<>();
    private final List<ContrachequeAssalariado> assalariados = new ArrayList<>();
    private final List<ContrachequeComissionado> comissionados = new ArrayList<>();
    private final StringBuilder texto = new StringBuilder();

    public RelatorioFolha(LocalDate data) {
        this.data = data;
    }

    public void adicionar(ContrachequeHorista contracheque) {
        horistas.add(contracheque);
    }

    public void adicionar(ContrachequeAssalariado contracheque) {
        assalariados.add(contracheque);
    }

    public void adicionar(ContrachequeComissionado contracheque) {
        comissionados.add(contracheque);
    }

    public String gerar() {
        texto.setLength(0);
        linha("FOLHA DE PAGAMENTO DO DIA " + data);
        linha("====================================");
        linha("");
        escreverHoristas();
        escreverAssalariados();
        escreverComissionados();
        linha("TOTAL FOLHA: " + moeda(totalBruto()));
        return texto.toString();
    }

    public BigDecimal totalBruto() {
        return somar(horistas, Contracheque::getBruto)
                .add(somar(assalariados, Contracheque::getBruto))
                .add(somar(comissionados, Contracheque::getBruto));
    }

    private void escreverHoristas() {
        cabecalho("===================== HORISTAS ================================================================================================",
                "Nome                                 Horas Extra Salario Bruto Descontos Salario Liquido Metodo",
                "==================================== ===== ===== ============= ========= =============== ======================================");
        for (ContrachequeHorista c : ordenados(horistas)) {
            linha(String.format(FORMATO_HORISTA, c.getNome(), horas(c.getHorasNormais()), horas(c.getHorasExtras()),
                    moeda(c.getBruto()), moeda(c.getDescontos()), moeda(c.getLiquido()), c.getMetodoPagamento()));
        }
        linha("");
        linha(String.format(FORMATO_HORISTA, "TOTAL HORISTAS",
                horas(somar(horistas, ContrachequeHorista::getHorasNormais)), horas(somar(horistas, ContrachequeHorista::getHorasExtras)),
                moeda(somar(horistas, Contracheque::getBruto)), moeda(somar(horistas, Contracheque::getDescontos)),
                moeda(somar(horistas, Contracheque::getLiquido)), "").stripTrailing());
        linha("");
    }

    private void escreverAssalariados() {
        cabecalho("===================== ASSALARIADOS ============================================================================================",
                "Nome                                             Salario Bruto Descontos Salario Liquido Metodo",
                "================================================ ============= ========= =============== ======================================");
        for (ContrachequeAssalariado c : ordenados(assalariados)) {
            linha(String.format(FORMATO_ASSALARIADO, c.getNome(), moeda(c.getBruto()), moeda(c.getDescontos()),
                    moeda(c.getLiquido()), c.getMetodoPagamento()));
        }
        linha("");
        linha(String.format(FORMATO_ASSALARIADO, "TOTAL ASSALARIADOS",
                moeda(somar(assalariados, Contracheque::getBruto)), moeda(somar(assalariados, Contracheque::getDescontos)),
                moeda(somar(assalariados, Contracheque::getLiquido)), "").stripTrailing());
        linha("");
    }

    private void escreverComissionados() {
        cabecalho("===================== COMISSIONADOS ===========================================================================================",
                "Nome                  Fixo     Vendas   Comissao Salario Bruto Descontos Salario Liquido Metodo",
                "===================== ======== ======== ======== ============= ========= =============== ======================================");
        for (ContrachequeComissionado c : ordenados(comissionados)) {
            linha(String.format(FORMATO_COMISSIONADO, c.getNome(), moeda(c.getFixo()), moeda(c.getVendas()), moeda(c.getComissao()),
                    moeda(c.getBruto()), moeda(c.getDescontos()), moeda(c.getLiquido()), c.getMetodoPagamento()));
        }
        linha("");
        linha(String.format(FORMATO_COMISSIONADO, "TOTAL COMISSIONADOS",
                moeda(somar(comissionados, ContrachequeComissionado::getFixo)), moeda(somar(comissionados, ContrachequeComissionado::getVendas)),
                moeda(somar(comissionados, ContrachequeComissionado::getComissao)), moeda(somar(comissionados, Contracheque::getBruto)),
                moeda(somar(comissionados, Contracheque::getDescontos)), moeda(somar(comissionados, Contracheque::getLiquido)), "").stripTrailing());
        linha("");
    }

    private void cabecalho(String titulo, String colunas, String sublinhado) {
        linha(LINHA);
        linha(titulo);
        linha(LINHA);
        linha(colunas);
        linha(sublinhado);
    }

    private void linha(String conteudo) {
        texto.append(conteudo).append(System.lineSeparator());
    }

    private static <T extends Contracheque> List<T> ordenados(List<T> contracheques) {
        List<T> copia = new ArrayList<>(contracheques);
        copia.sort(Comparator.comparing(Contracheque::getNome));
        return copia;
    }

    private static <T> BigDecimal somar(List<T> itens, Function<T, BigDecimal> valor) {
        BigDecimal total = BigDecimal.ZERO;
        for (T item : itens) {
            total = total.add(valor.apply(item));
        }
        return total;
    }

    private static String moeda(BigDecimal valor) {
        return ConversorNumero.formatarMoeda(valor);
    }

    private static String horas(BigDecimal valor) {
        return ConversorNumero.formatarHoras(valor);
    }
}
