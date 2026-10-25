package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.exception.persistencia.CarregarDadosException;
import br.ufal.ic.p2.wepayu.exception.sistema.NaoHaComandoADesfazerException;
import br.ufal.ic.p2.wepayu.exception.sistema.NaoHaComandoARefazerException;
import br.ufal.ic.p2.wepayu.exception.sistema.SistemaEncerradoException;
import br.ufal.ic.p2.wepayu.persistencia.DadosSistema;
import br.ufal.ic.p2.wepayu.persistencia.Persistencia;

import java.util.ArrayDeque;
import java.util.Deque;

public class Sistema {
    private final Persistencia persistencia;
    private final Deque<byte[]> desfazer = new ArrayDeque<>();
    private final Deque<byte[]> refazer = new ArrayDeque<>();
    private DadosSistema dados;
    private boolean encerrado;

    public Sistema(Persistencia persistencia) throws CarregarDadosException {
        this.persistencia = persistencia;
        this.dados = persistencia.carregar();
    }

    public DadosSistema dados() {
        return dados;
    }

    public void executar(Comando comando) throws WePayUException {
        executarComRetorno(() -> {
            comando.executar();
            return null;
        });
    }

    public <T> T executarComRetorno(ComandoComRetorno<T> comando) throws WePayUException {
        verificarAtivo();
        byte[] estadoAnterior = persistencia.serializar(dados);
        try {
            T resultado = comando.executar();
            desfazer.push(estadoAnterior);
            refazer.clear();
            return resultado;
        } catch (WePayUException e) {
            dados = persistencia.desserializar(estadoAnterior);
            throw e;
        }
    }

    public void zerar() throws WePayUException {
        executar(() -> dados = new DadosSistema());
    }

    public void desfazer() throws WePayUException {
        verificarAtivo();
        if (desfazer.isEmpty()) {
            throw new NaoHaComandoADesfazerException();
        }
        refazer.push(persistencia.serializar(dados));
        dados = persistencia.desserializar(desfazer.pop());
    }

    public void refazer() throws WePayUException {
        verificarAtivo();
        if (refazer.isEmpty()) {
            throw new NaoHaComandoARefazerException();
        }
        desfazer.push(persistencia.serializar(dados));
        dados = persistencia.desserializar(refazer.pop());
    }

    public void encerrar() throws WePayUException {
        verificarAtivo();
        persistencia.salvar(dados);
        encerrado = true;
    }

    private void verificarAtivo() throws SistemaEncerradoException {
        if (encerrado) {
            throw new SistemaEncerradoException();
        }
    }
}
