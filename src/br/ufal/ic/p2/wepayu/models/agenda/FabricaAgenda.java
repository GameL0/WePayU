package br.ufal.ic.p2.wepayu.models.agenda;

import br.ufal.ic.p2.wepayu.exception.agenda.DescricaoAgendaInvalidaException;

public final class FabricaAgenda {
    private static final int ULTIMO_DIA_MENSAL = 28;
    private static final int MAXIMO_SEMANAS = 52;
    private static final int DIAS_DA_SEMANA = 7;

    private FabricaAgenda() {
    }

    public static AgendaPagamento interpretar(String descricao) throws DescricaoAgendaInvalidaException {
        String[] partes = descricao == null ? new String[0] : descricao.trim().split("\\s+");
        if (partes.length == 2 && partes[0].equals("mensal")) {
            return mensal(partes[1]);
        }
        if (partes.length == 2 && partes[0].equals("semanal")) {
            return new AgendaSemanal(1, numeroEntre(partes[1], 1, DIAS_DA_SEMANA));
        }
        if (partes.length == 3 && partes[0].equals("semanal")) {
            return new AgendaSemanal(numeroEntre(partes[1], 1, MAXIMO_SEMANAS), numeroEntre(partes[2], 1, DIAS_DA_SEMANA));
        }
        throw new DescricaoAgendaInvalidaException();
    }

    private static AgendaPagamento mensal(String dia) throws DescricaoAgendaInvalidaException {
        if (dia.equals("$")) {
            return new AgendaMensal();
        }
        return new AgendaMensalDiaFixo(numeroEntre(dia, 1, ULTIMO_DIA_MENSAL));
    }

    private static int numeroEntre(String texto, int minimo, int maximo) throws DescricaoAgendaInvalidaException {
        try {
            int numero = Integer.parseInt(texto);
            if (numero < minimo || numero > maximo) {
                throw new DescricaoAgendaInvalidaException();
            }
            return numero;
        } catch (NumberFormatException e) {
            throw new DescricaoAgendaInvalidaException();
        }
    }
}
