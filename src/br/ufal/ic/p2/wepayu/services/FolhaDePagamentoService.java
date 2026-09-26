package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.models.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.io.PrintWriter;
import java.io.File;

public class FolhaDePagamentoService {

    private String ultimaDataFolha = null;
    private String ultimaSaidaFolha = null;

    private String formatarDinheiro(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.DOWN).toString().replace(".", ",");
    }

    private String getMetodoString(Empregado emp) {
        if (emp.getMetodoPagamento().equals("emMaos")) return "Em maos";
        if (emp.getMetodoPagamento().equals("correios")) return "Correios, " + emp.getEndereco();
        if (emp.getMetodoPagamento().equals("banco")) {
            return emp.getBanco() + ", Ag. " + emp.getAgencia() + " CC " + emp.getContaCorrente();
        }
        return "";
    }

    private String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    private String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }

    public String totalFolha(HashMap<String, Empregado> empregados, String dataStr) {
        LocalDate dataFolha = LocalDate.parse(dataStr, java.time.format.DateTimeFormatter.ofPattern("d/M/yyyy"));
        BigDecimal totalGeral = BigDecimal.ZERO;

        for (Empregado emp : empregados.values()) {
            if (emp.isDiaDePagamento(dataFolha)) {
                totalGeral = totalGeral.add(calcularSalarioBruto(emp, dataFolha));
            }
        }
        return formatarDinheiro(totalGeral);
    }

    public void rodaFolha(HashMap<String, Empregado> empregados, String dataStr, String saida) throws Exception {
        if (dataStr.equals(ultimaDataFolha)) {
            PrintWriter writer = new PrintWriter(new File(saida), "UTF-8");
            writer.print(ultimaSaidaFolha);
            writer.close();
            return;
        }

        LocalDate dataFolha = LocalDate.parse(dataStr, java.time.format.DateTimeFormatter.ofPattern("d/M/yyyy"));
        
        List<Empregado> horistas = new ArrayList<>();
        List<Empregado> assalariados = new ArrayList<>();
        List<Empregado> comissionados = new ArrayList<>();

        BigDecimal totalFolha = BigDecimal.ZERO;

        for (Empregado emp : empregados.values()) {
            if (emp.isDiaDePagamento(dataFolha)) {
                if (emp instanceof Horista) horistas.add(emp);
                if (emp instanceof Assalariado) assalariados.add(emp);
                if (emp instanceof Comissionado) comissionados.add(emp);
            }
        }

        horistas.sort(Comparator.comparing(Empregado::getNome));
        assalariados.sort(Comparator.comparing(Empregado::getNome));
        comissionados.sort(Comparator.comparing(Empregado::getNome));

        StringBuilder sb = new StringBuilder();
        sb.append("FOLHA DE PAGAMENTO DO DIA ").append(dataFolha.toString()).append("\n");
        sb.append("====================================\n\n");

        sb.append("===============================================================================================================================\n");
        sb.append("===================== HORISTAS ================================================================================================\n");
        sb.append("===============================================================================================================================\n");
        sb.append("Nome                                 Horas Extra Salario Bruto Descontos Salario Liquido Metodo\n");
        sb.append("==================================== ===== ===== ============= ========= =============== ======================================\n");
        
        BigDecimal totalH_horas = BigDecimal.ZERO;
        BigDecimal totalH_extra = BigDecimal.ZERO;
        BigDecimal totalH_bruto = BigDecimal.ZERO;
        BigDecimal totalH_desc = BigDecimal.ZERO;
        BigDecimal totalH_liq = BigDecimal.ZERO;

        for (Empregado emp : horistas) {
            Horista h = (Horista) emp;
            LocalDate inicio = h.getDataUltimoPagamento() != null ? h.getDataUltimoPagamento() : h.getDataContrato();
            LocalDate fim = dataFolha.plusDays(1);
            BigDecimal horas = h.getHorasNormais(inicio, fim);
            BigDecimal extras = h.getHorasExtras(inicio, fim);
            BigDecimal bruto = calcularSalarioBruto(h, dataFolha);
            BigDecimal descTotal = calcularDescontos(h, dataFolha);
            
            BigDecimal descontoAplicado = descTotal.min(bruto);
            h.setDividaSindical(descTotal.subtract(descontoAplicado));
            BigDecimal liq = bruto.subtract(descontoAplicado);

            totalH_horas = totalH_horas.add(horas);
            totalH_extra = totalH_extra.add(extras);
            totalH_bruto = totalH_bruto.add(bruto);
            totalH_desc = totalH_desc.add(descontoAplicado);
            totalH_liq = totalH_liq.add(liq);

            sb.append(padRight(emp.getNome(), 36)).append(" ")
              .append(padLeft(horas.setScale(0, RoundingMode.DOWN).toString(), 5)).append(" ")
              .append(padLeft(extras.setScale(0, RoundingMode.DOWN).toString(), 5)).append(" ")
              .append(padLeft(formatarDinheiro(bruto), 13)).append(" ")
              .append(padLeft(formatarDinheiro(descontoAplicado), 9)).append(" ")
              .append(padLeft(formatarDinheiro(liq), 15)).append(" ")
              .append(getMetodoString(emp)).append("\n");
              
            h.setDataUltimoPagamento(dataFolha);
        }
        sb.append("\n").append(padRight("TOTAL HORISTAS", 36)).append(" ")
          .append(padLeft(totalH_horas.setScale(0, RoundingMode.DOWN).toString(), 5)).append(" ")
          .append(padLeft(totalH_extra.setScale(0, RoundingMode.DOWN).toString(), 5)).append(" ")
          .append(padLeft(formatarDinheiro(totalH_bruto), 13)).append(" ")
          .append(padLeft(formatarDinheiro(totalH_desc), 9)).append(" ")
          .append(padLeft(formatarDinheiro(totalH_liq), 15)).append("\n\n");

        sb.append("===============================================================================================================================\n");
        sb.append("===================== ASSALARIADOS ============================================================================================\n");
        sb.append("===============================================================================================================================\n");
        sb.append("Nome                                             Salario Bruto Descontos Salario Liquido Metodo\n");
        sb.append("================================================ ============= ========= =============== ======================================\n");
        
        BigDecimal totalA_bruto = BigDecimal.ZERO;
        BigDecimal totalA_desc = BigDecimal.ZERO;
        BigDecimal totalA_liq = BigDecimal.ZERO;

        for (Empregado emp : assalariados) {
            BigDecimal bruto = calcularSalarioBruto(emp, dataFolha);
            BigDecimal descTotal = calcularDescontos(emp, dataFolha);
            
            BigDecimal descontoAplicado = descTotal.min(bruto);
            emp.setDividaSindical(descTotal.subtract(descontoAplicado));
            BigDecimal liq = bruto.subtract(descontoAplicado);

            totalA_bruto = totalA_bruto.add(bruto);
            totalA_desc = totalA_desc.add(descontoAplicado);
            totalA_liq = totalA_liq.add(liq);

            sb.append(padRight(emp.getNome(), 48)).append(" ")
              .append(padLeft(formatarDinheiro(bruto), 13)).append(" ")
              .append(padLeft(formatarDinheiro(descontoAplicado), 9)).append(" ")
              .append(padLeft(formatarDinheiro(liq), 15)).append(" ")
              .append(getMetodoString(emp)).append("\n");
              
            emp.setDataUltimoPagamento(dataFolha);
        }
        sb.append("\n").append(padRight("TOTAL ASSALARIADOS", 48)).append(" ")
          .append(padLeft(formatarDinheiro(totalA_bruto), 13)).append(" ")
          .append(padLeft(formatarDinheiro(totalA_desc), 9)).append(" ")
          .append(padLeft(formatarDinheiro(totalA_liq), 15)).append("\n\n");

        sb.append("===============================================================================================================================\n");
        sb.append("===================== COMISSIONADOS ===========================================================================================\n");
        sb.append("===============================================================================================================================\n");
        sb.append("Nome                  Fixo     Vendas   Comissao Salario Bruto Descontos Salario Liquido Metodo\n");
        sb.append("===================== ======== ======== ======== ============= ========= =============== ======================================\n");
        
        BigDecimal totalC_fixo = BigDecimal.ZERO;
        BigDecimal totalC_vendas = BigDecimal.ZERO;
        BigDecimal totalC_comissao = BigDecimal.ZERO;
        BigDecimal totalC_bruto = BigDecimal.ZERO;
        BigDecimal totalC_desc = BigDecimal.ZERO;
        BigDecimal totalC_liq = BigDecimal.ZERO;

        for (Empregado emp : comissionados) {
            Comissionado c = (Comissionado) emp;
            LocalDate inicio = c.getDataUltimoPagamento() != null ? c.getDataUltimoPagamento() : c.getDataContrato();
            LocalDate fim = dataFolha.plusDays(1);
            BigDecimal fixo = c.getSalario().multiply(new BigDecimal("12")).divide(new BigDecimal("26"), 2, RoundingMode.DOWN);
            BigDecimal vendas = c.getVendasRealizadas(inicio, fim);
            BigDecimal comissaoReal = vendas.multiply(c.getComissao()).setScale(2, RoundingMode.DOWN);
            BigDecimal bruto = calcularSalarioBruto(c, dataFolha);
            BigDecimal descTotal = calcularDescontos(c, dataFolha);
            
            BigDecimal descontoAplicado = descTotal.min(bruto);
            c.setDividaSindical(descTotal.subtract(descontoAplicado));
            BigDecimal liq = bruto.subtract(descontoAplicado);

            totalC_fixo = totalC_fixo.add(fixo);
            totalC_vendas = totalC_vendas.add(vendas);
            totalC_comissao = totalC_comissao.add(comissaoReal);
            totalC_bruto = totalC_bruto.add(bruto);
            totalC_desc = totalC_desc.add(descontoAplicado);
            totalC_liq = totalC_liq.add(liq);

            sb.append(padRight(emp.getNome(), 21)).append(" ")
              .append(padLeft(formatarDinheiro(fixo), 8)).append(" ")
              .append(padLeft(formatarDinheiro(vendas), 8)).append(" ")
              .append(padLeft(formatarDinheiro(comissaoReal), 8)).append(" ")
              .append(padLeft(formatarDinheiro(bruto), 13)).append(" ")
              .append(padLeft(formatarDinheiro(descontoAplicado), 9)).append(" ")
              .append(padLeft(formatarDinheiro(liq), 15)).append(" ")
              .append(getMetodoString(emp)).append("\n");
              
            c.setDataUltimoPagamento(dataFolha);
        }
        sb.append("\n").append(padRight("TOTAL COMISSIONADOS", 21)).append(" ")
          .append(padLeft(formatarDinheiro(totalC_fixo), 8)).append(" ")
          .append(padLeft(formatarDinheiro(totalC_vendas), 8)).append(" ")
          .append(padLeft(formatarDinheiro(totalC_comissao), 8)).append(" ")
          .append(padLeft(formatarDinheiro(totalC_bruto), 13)).append(" ")
          .append(padLeft(formatarDinheiro(totalC_desc), 9)).append(" ")
          .append(padLeft(formatarDinheiro(totalC_liq), 15)).append("\n\n");

        totalFolha = totalH_bruto.add(totalA_bruto).add(totalC_bruto);
        sb.append("TOTAL FOLHA: ").append(formatarDinheiro(totalFolha)).append("\n");

        ultimaDataFolha = dataStr;
        ultimaSaidaFolha = sb.toString();

        PrintWriter writer = new PrintWriter(new File(saida), "UTF-8");
        writer.print(sb.toString());
        writer.close();
    }

    private BigDecimal calcularSalarioBruto(Empregado emp, LocalDate dataFolha) {
        LocalDate inicio = emp.getDataUltimoPagamento() != null ? emp.getDataUltimoPagamento() : emp.getDataContrato();
        LocalDate fim = dataFolha.plusDays(1);

        if (emp instanceof Horista) {
            Horista h = (Horista) emp;
            BigDecimal horas = h.getHorasNormais(inicio, fim);
            BigDecimal extras = h.getHorasExtras(inicio, fim);
            return (horas.multiply(h.getSalario())).add(extras.multiply(h.getSalario()).multiply(new BigDecimal("1.5")));
        } else if (emp instanceof Comissionado) {
            Comissionado c = (Comissionado) emp;
            BigDecimal fixo = c.getSalario().multiply(new BigDecimal("12")).divide(new BigDecimal("26"), 2, RoundingMode.DOWN);
            BigDecimal comissaoVal = c.getVendasRealizadas(inicio, fim).multiply(c.getComissao()).setScale(2, RoundingMode.DOWN);
            return fixo.add(comissaoVal);
        } else {
            return emp.getSalario();
        }
    }

    private BigDecimal calcularDescontos(Empregado emp, LocalDate dataFolha) {
        BigDecimal total = emp.getDividaSindical();
        
        if (!emp.isSindicalizado()) return total;
        
        long dias = emp.getDiasParaPagamentoSindical(dataFolha);
        BigDecimal base = emp.getMembroSindicato().getTaxaSindical().multiply(new BigDecimal(dias));
        
        LocalDate inicio = emp.getDataUltimoPagamento() != null ? emp.getDataUltimoPagamento() : emp.getDataContrato();
        LocalDate fim = dataFolha.plusDays(1);
        BigDecimal extra = BigDecimal.ZERO;

        for (TaxaServico taxa : emp.getMembroSindicato().getTaxasServico()) {
            if (!taxa.getData().isBefore(inicio) && taxa.getData().isBefore(fim)) {
                extra = extra.add(taxa.getValor());
            }
        }
        return total.add(base).add(extra);
    }

    private BigDecimal calcularSalarioLiquido(Empregado emp, LocalDate dataFolha) {
        BigDecimal bruto = calcularSalarioBruto(emp, dataFolha);
        BigDecimal desc = calcularDescontos(emp, dataFolha);
        if (desc.compareTo(bruto) > 0) desc = bruto;
        return bruto.subtract(desc);
    }
}
