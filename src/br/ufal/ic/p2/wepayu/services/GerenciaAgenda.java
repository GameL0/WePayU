package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.exception.agenda.AgendaJaExisteException;
import br.ufal.ic.p2.wepayu.exception.agenda.AgendaNaoDisponivelException;
import br.ufal.ic.p2.wepayu.exception.agenda.DescricaoAgendaInvalidaException;
import br.ufal.ic.p2.wepayu.models.agenda.AgendaPagamento;
import br.ufal.ic.p2.wepayu.models.agenda.FabricaAgenda;

public class GerenciaAgenda {
    private final Sistema sistema;

    public GerenciaAgenda(Sistema sistema) {
        this.sistema = sistema;
    }

    public void criar(String descricao) throws DescricaoAgendaInvalidaException, AgendaJaExisteException {
        String descricaoPadronizada = FabricaAgenda.interpretar(descricao).descricao();
        if (sistema.dados().temAgenda(descricaoPadronizada)) {
            throw new AgendaJaExisteException();
        }
        sistema.dados().adicionarAgenda(descricaoPadronizada);
    }

    public AgendaPagamento buscarDisponivel(String descricao) throws AgendaNaoDisponivelException, DescricaoAgendaInvalidaException {
        if (!sistema.dados().temAgenda(descricao)) {
            throw new AgendaNaoDisponivelException();
        }
        return FabricaAgenda.interpretar(descricao);
    }
}
