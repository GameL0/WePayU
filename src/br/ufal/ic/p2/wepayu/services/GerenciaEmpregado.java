package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.conversao.ConversorBooleano;
import br.ufal.ic.p2.wepayu.conversao.ConversorNumero;
import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.exception.naoencontrado.AtributoNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.naoencontrado.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.naoencontrado.NomeNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.nulo.ComissaoNulaException;
import br.ufal.ic.p2.wepayu.exception.nulo.IdentificacaoNulaException;
import br.ufal.ic.p2.wepayu.exception.nulo.IdentificacaoSindicatoNulaException;
import br.ufal.ic.p2.wepayu.exception.nulo.SalarioNuloException;
import br.ufal.ic.p2.wepayu.exception.numerico.ComissaoNaoNumericoException;
import br.ufal.ic.p2.wepayu.exception.numerico.IndiceNaoNumericoException;
import br.ufal.ic.p2.wepayu.exception.numerico.SalarioNaoNumericoException;
import br.ufal.ic.p2.wepayu.exception.persistencia.CarregarDadosException;
import br.ufal.ic.p2.wepayu.exception.persistencia.SalvarDadosException;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.persistencia.DadosSistema;
import br.ufal.ic.p2.wepayu.persistencia.Persistencia;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public class GerenciaEmpregado {
    private final Persistencia persistencia = new Persistencia("dados.xml");
    private Map<String, Empregado> empregados;
    private int proximoId;

    public GerenciaEmpregado() throws CarregarDadosException {
        DadosSistema dados = persistencia.carregar();
        empregados = dados.getEmpregados();
        proximoId = dados.getProximoId();
    }

    public void zerarSistema() {
        empregados.clear();
        proximoId = 1;
    }

    public String criar(String nome, String endereco, String tipo, String salarioInformado) throws WePayUException {
        BigDecimal salario = converterSalario(salarioInformado);
        return adicionar(EmpregadoFactory.criar(nome, endereco, tipo, salario));
    }

    public String criar(String nome, String endereco, String tipo, String salarioInformado, String comissaoInformada) throws WePayUException {
        BigDecimal salario = converterSalario(salarioInformado);
        BigDecimal comissao = ConversorNumero.converter(comissaoInformada, ComissaoNulaException::new, ComissaoNaoNumericoException::new);
        return adicionar(EmpregadoFactory.criar(nome, endereco, tipo, salario, comissao));
    }

    public Empregado buscar(String id) throws EmpregadoNaoExisteException, IdentificacaoNulaException {
        if (id == null || id.isEmpty()) {
            throw new IdentificacaoNulaException();
        }
        Empregado empregado = empregados.get(id);
        if (empregado == null) {
            throw new EmpregadoNaoExisteException();
        }
        return empregado;
    }

    public Optional<Empregado> buscarPorIdSindicato(String idSindicato) {
        return empregados.values().stream()
                .filter(empregado -> empregado.temIdSindicato(idSindicato))
                .findFirst();
    }

    public void remover(String id) throws EmpregadoNaoExisteException, IdentificacaoNulaException {
        buscar(id);
        empregados.remove(id);
    }

    public String getAtributoEmpregado(String id, String atributo) throws WePayUException {
        return buscar(id).getAtributo(atributo);
    }

    public String getEmpregadoPorNome(String nome, String indiceInformado) throws NomeNaoExisteException, IndiceNaoNumericoException {
        int indice = ConversorNumero.converterInteiro(indiceInformado, IndiceNaoNumericoException::new);
        int contador = 0;
        for (Map.Entry<String, Empregado> entrada : empregados.entrySet()) {
            if (entrada.getValue().temNome(nome)) {
                contador++;
                if (contador == indice) {
                    return entrada.getKey();
                }
            }
        }
        throw new NomeNaoExisteException();
    }

    public void alterar(String id, String atributo, String valor) throws WePayUException {
        Empregado empregado = buscar(id);
        if (!"sindicalizado".equals(atributo)) {
            throw new AtributoNaoExisteException();
        }
        if (ConversorBooleano.converter(valor)) {
            throw new IdentificacaoSindicatoNulaException();
        }
        empregado.desfiliar();
    }

    public void encerrarSistema() throws SalvarDadosException {
        persistencia.salvar(new DadosSistema(empregados, proximoId));
    }

    private BigDecimal converterSalario(String salarioInformado) throws SalarioNuloException, SalarioNaoNumericoException {
        return ConversorNumero.converter(salarioInformado, SalarioNuloException::new, SalarioNaoNumericoException::new);
    }

    private String adicionar(Empregado empregado) {
        String id = String.valueOf(proximoId);
        proximoId++;
        empregados.put(id, empregado);
        return id;
    }
}
