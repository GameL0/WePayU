package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.conversao.ConversorNumero;
import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.exception.naoencontrado.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.naoencontrado.NomeNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.nulo.ComissaoNulaException;
import br.ufal.ic.p2.wepayu.exception.nulo.IdentificacaoNulaException;
import br.ufal.ic.p2.wepayu.exception.nulo.SalarioNuloException;
import br.ufal.ic.p2.wepayu.exception.numerico.ComissaoNaoNumericoException;
import br.ufal.ic.p2.wepayu.exception.numerico.IndiceNaoNumericoException;
import br.ufal.ic.p2.wepayu.exception.numerico.SalarioNaoNumericoException;
import br.ufal.ic.p2.wepayu.models.Empregado;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class GerenciaEmpregado {
    private final Sistema sistema;

    public GerenciaEmpregado(Sistema sistema) {
        this.sistema = sistema;
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
        Empregado empregado = empregados().get(id);
        if (empregado == null) {
            throw new EmpregadoNaoExisteException();
        }
        return empregado;
    }

    public Optional<Empregado> buscarPorIdSindicato(String idSindicato) {
        return empregados().values().stream()
                .filter(empregado -> empregado.temIdSindicato(idSindicato))
                .findFirst();
    }

    public Collection<Empregado> listar() {
        return Collections.unmodifiableCollection(empregados().values());
    }

    public String contar() {
        return String.valueOf(empregados().size());
    }

    public void remover(String id) throws EmpregadoNaoExisteException, IdentificacaoNulaException {
        buscar(id);
        empregados().remove(id);
    }

    public void substituir(String id, Empregado empregado) {
        empregados().put(id, empregado);
    }

    public String getAtributoEmpregado(String id, String atributo) throws WePayUException {
        return buscar(id).getAtributo(atributo);
    }

    public String getEmpregadoPorNome(String nome, String indiceInformado) throws NomeNaoExisteException, IndiceNaoNumericoException {
        int indice = ConversorNumero.converterInteiro(indiceInformado, IndiceNaoNumericoException::new);
        int contador = 0;
        for (Map.Entry<String, Empregado> entrada : empregados().entrySet()) {
            if (entrada.getValue().temNome(nome)) {
                contador++;
                if (contador == indice) {
                    return entrada.getKey();
                }
            }
        }
        throw new NomeNaoExisteException();
    }

    private Map<String, Empregado> empregados() {
        return sistema.dados().getEmpregados();
    }

    private BigDecimal converterSalario(String salarioInformado) throws SalarioNuloException, SalarioNaoNumericoException {
        return ConversorNumero.converter(salarioInformado, SalarioNuloException::new, SalarioNaoNumericoException::new);
    }

    private String adicionar(Empregado empregado) {
        String id = sistema.dados().gerarId();
        empregados().put(id, empregado);
        return id;
    }
}
