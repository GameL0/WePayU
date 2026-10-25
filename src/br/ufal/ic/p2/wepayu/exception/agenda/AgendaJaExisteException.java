package br.ufal.ic.p2.wepayu.exception.agenda;

public class AgendaJaExisteException extends AgendaException {
    public AgendaJaExisteException() {
        super("Agenda de pagamentos ja existe");
    }
}
