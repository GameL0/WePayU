package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.Exception.*;
import br.ufal.ic.p2.wepayu.models.*;
import br.ufal.ic.p2.wepayu.utils.XMLHelper;
import br.ufal.ic.p2.wepayu.services.CartaoDePontoService;
import br.ufal.ic.p2.wepayu.services.VendaService;
import br.ufal.ic.p2.wepayu.services.SindicatoService;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;

public class Facade {
    private HashMap<String, Empregado> empregados;
    private int ident_int = 1;
    private CartaoDePontoService cartaoDePontoService = new CartaoDePontoService();
    private VendaService vendaService = new VendaService();
    private SindicatoService sindicatoService = new SindicatoService();
    private br.ufal.ic.p2.wepayu.services.FolhaDePagamentoService folhaService = new br.ufal.ic.p2.wepayu.services.FolhaDePagamentoService();

    private java.util.Stack<String> undoStack = new java.util.Stack<>();
    private java.util.Stack<String> redoStack = new java.util.Stack<>();
    private String currentStateXml = null;
    private boolean isEncerrado = false;

    private void salvarEstadoParaUndo() {
        currentStateXml = XMLHelper.salvarParaString(empregados, ident_int);
    }

    private void confirmarEstadoParaUndo() {
        if (currentStateXml != null) {
            undoStack.push(currentStateXml);
            redoStack.clear();
            currentStateXml = null;
        }
    }

    public Facade(){
        empregados = XMLHelper.carregar();
        ident_int = XMLHelper.carregarProximoId();
    }

    public void zerarSistema(){
        salvarEstadoParaUndo();
        empregados.clear();
        ident_int = 1;
        new File("dados.xml").delete();
        confirmarEstadoParaUndo();
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario_str)
            throws TipoInvalidoException, TipoNaoAplicavelException, CampoNuloException,
            ValorNumericoException, ValorNegativoException {

        salvarEstadoParaUndo();
        if (nome.isEmpty()) throw new CampoNuloException("Nome nao pode ser nulo.");
        if (endereco.isEmpty()) throw new CampoNuloException("Endereco nao pode ser nulo.");
        if (!tipo.equals("horista") && !tipo.equals("assalariado") && !tipo.equals("comissionado"))
            throw new TipoInvalidoException();
        if (tipo.equals("comissionado")) throw new TipoNaoAplicavelException();
        if (salario_str.isEmpty()) throw new CampoNuloException("Salario nao pode ser nulo.");

        BigDecimal salario;
        try {
            salario = new BigDecimal(salario_str.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new ValorNumericoException("Salario deve ser numerico.");
        }

        if (salario.compareTo(BigDecimal.ZERO) < 0)
            throw new ValorNegativoException("Salario deve ser nao-negativo.");

        String ident_str = String.valueOf(ident_int++);
        Empregado empregado = EmpregadoFactory.criarEmpregado(tipo, nome, endereco, salario, null);
        empregados.put(ident_str, empregado);
        confirmarEstadoParaUndo();
        return ident_str;
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario_str, String comissao_str)
            throws TipoInvalidoException, TipoNaoAplicavelException, CampoNuloException,
            ValorNumericoException, ValorNegativoException {

        salvarEstadoParaUndo();
        if (nome.isEmpty()) throw new CampoNuloException("Nome nao pode ser nulo.");
        if (endereco.isEmpty()) throw new CampoNuloException("Endereco nao pode ser nulo.");
        if (!tipo.equals("horista") && !tipo.equals("assalariado") && !tipo.equals("comissionado"))
            throw new TipoInvalidoException();
        if (!tipo.equals("comissionado")) throw new TipoNaoAplicavelException();
        if (salario_str.isEmpty()) throw new CampoNuloException("Salario nao pode ser nulo.");

        BigDecimal salario;
        try {
            salario = new BigDecimal(salario_str.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new ValorNumericoException("Salario deve ser numerico.");
        }

        if (salario.compareTo(BigDecimal.ZERO) < 0)
            throw new ValorNegativoException("Salario deve ser nao-negativo.");

        if (comissao_str.isEmpty()) throw new CampoNuloException("Comissao nao pode ser nula.");

        BigDecimal comissao;
        try {
            comissao = new BigDecimal(comissao_str.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new ValorNumericoException("Comissao deve ser numerica.");
        }

        if (comissao.compareTo(BigDecimal.ZERO) < 0)
            throw new ValorNegativoException("Comissao deve ser nao-negativa.");

        String ident_str = String.valueOf(ident_int++);
        Empregado empregado = EmpregadoFactory.criarEmpregado(tipo, nome, endereco, salario, comissao);
        empregados.put(ident_str, empregado);
        confirmarEstadoParaUndo();
        return ident_str;
    }

    public String getAtributoEmpregado(String emp, String atributo) throws Exception {

        if (emp.isEmpty()) throw new CampoNuloException("Identificacao do empregado nao pode ser nula.");

        Empregado empregado = empregados.get(emp);

        if (empregado == null) throw new EmpregadoNaoExisteException();

        String resultado = empregado.getAtributo(atributo);

        if (resultado == null) throw new AtributoNaoExisteException();

        return resultado;
    }

    public String getEmpregadoPorNome(String nome, int indice) throws EmpregadoNomeNaoExisteException {
        int contador = 0;
        for (String id : empregados.keySet()) {
            Empregado emp = empregados.get(id);
            if (emp.getNome().equals(nome)) {
                contador++;
                if (contador == indice) {
                    return id;
                }
            }
        }
        throw new EmpregadoNomeNaoExisteException();
    }

    public void removerEmpregado(String emp) throws CampoNuloException, EmpregadoNaoExisteException {
        salvarEstadoParaUndo();
        if (emp.isEmpty()) throw new CampoNuloException("Identificacao do empregado nao pode ser nula.");
        Empregado empregado = empregados.get(emp);
        if (empregado == null) throw new EmpregadoNaoExisteException();
        empregados.remove(emp);
        confirmarEstadoParaUndo();
    }

    public void lancaCartao(String emp, String data, String horas)
            throws CampoNuloException, EmpregadoNaoExisteException,
                   EmpregadoNaoEHoristaException, DataInvalidaException, HorasPositivasException {
        salvarEstadoParaUndo();
        cartaoDePontoService.lancaCartao(empregados, emp, data, horas);
        confirmarEstadoParaUndo();
    }

    public String getHorasNormaisTrabalhadas(String emp, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, EmpregadoNaoEHoristaException,
                   DataInvalidaException, CampoNuloException {
        return cartaoDePontoService.getHorasNormais(empregados, emp, dataInicial, dataFinal);
    }

    public String getHorasExtrasTrabalhadas(String emp, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, EmpregadoNaoEHoristaException,
                   DataInvalidaException, CampoNuloException {
        return cartaoDePontoService.getHorasExtras(empregados, emp, dataInicial, dataFinal);
    }

    public void lancaVenda(String emp, String data, String valor)
            throws CampoNuloException, EmpregadoNaoExisteException,
                   EmpregadoNaoEComissionadoException, DataInvalidaException, ValorPositivoException {
        salvarEstadoParaUndo();
        vendaService.lancaVenda(empregados, emp, data, valor);
        confirmarEstadoParaUndo();
    }

    public String getVendasRealizadas(String emp, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, EmpregadoNaoEComissionadoException,
                   DataInvalidaException, CampoNuloException {
        return vendaService.getVendasRealizadas(empregados, emp, dataInicial, dataFinal);
    }

    // --- MÉTODOS NOVOS DA US 6 ---

    private void mudarTipoEmpregado(String emp, Empregado empregadoBase, String novoTipo, BigDecimal novoSalario, BigDecimal novaComissao) throws Exception {
        Empregado novoEmpregado = EmpregadoFactory.criarEmpregado(novoTipo, empregadoBase.getNome(), empregadoBase.getEndereco(), novoSalario, novaComissao);

        // Transfere o estado do antigo para o novo
        novoEmpregado.setMetodoPagamento(empregadoBase.getMetodoPagamento());
        novoEmpregado.setBanco(empregadoBase.getBanco());
        novoEmpregado.setAgencia(empregadoBase.getAgencia());
        novoEmpregado.setContaCorrente(empregadoBase.getContaCorrente());
        novoEmpregado.setMembroSindicato(empregadoBase.getMembroSindicato());

        // Substitui silenciosamente no mapa mantendo a mesma ID
        empregados.put(emp, novoEmpregado);
    }

    public void alteraEmpregado(String emp, String atributo, String valor, String ext) throws Exception {
        salvarEstadoParaUndo();
        if (emp.isEmpty()) throw new CampoNuloException("Identificacao do empregado nao pode ser nula.");
        Empregado empregadoBase = empregados.get(emp);
        if (empregadoBase == null) throw new EmpregadoNaoExisteException();

        if (atributo.equals("tipo")) {
            if (valor.equals("comissionado")) {
                if (ext.isEmpty()) throw new CampoNuloException("Comissao nao pode ser nula.");
                BigDecimal comissao;
                try {
                    comissao = new BigDecimal(ext.replace(",", "."));
                } catch (Exception e) {
                    throw new ValorNumericoException("Comissao deve ser numerica.");
                }
                if (comissao.compareTo(BigDecimal.ZERO) < 0) throw new ValorNegativoException("Comissao deve ser nao-negativa.");
                mudarTipoEmpregado(emp, empregadoBase, valor, empregadoBase.getSalario(), comissao);
            } else if (valor.equals("horista") || valor.equals("assalariado")) {
                if (ext.isEmpty()) throw new CampoNuloException("Salario nao pode ser nulo.");
                BigDecimal salario;
                try {
                    salario = new BigDecimal(ext.replace(",", "."));
                } catch (Exception e) {
                    throw new ValorNumericoException("Salario deve ser numerico.");
                }
                if (salario.compareTo(BigDecimal.ZERO) < 0) throw new ValorNegativoException("Salario deve ser nao-negativo.");
                mudarTipoEmpregado(emp, empregadoBase, valor, salario, null);
            }
        }
        confirmarEstadoParaUndo();
    }

    public void alteraEmpregado(String emp, String atributo, String valor, String banco, String agencia, String contaCorrente) throws Exception {
        salvarEstadoParaUndo();
        if (emp.isEmpty()) throw new CampoNuloException("Identificacao do empregado nao pode ser nula.");
        Empregado empregado = empregados.get(emp);
        if (empregado == null) throw new EmpregadoNaoExisteException();

        if (atributo.equals("metodoPagamento") && valor.equals("banco")) {
            if (banco.isEmpty()) throw new CampoNuloException("Banco nao pode ser nulo.");
            if (agencia.isEmpty()) throw new CampoNuloException("Agencia nao pode ser nulo.");
            if (contaCorrente.isEmpty()) throw new CampoNuloException("Conta corrente nao pode ser nulo.");

            empregado.setMetodoPagamento(valor);
            empregado.setBanco(banco);
            empregado.setAgencia(agencia);
            empregado.setContaCorrente(contaCorrente);
        }
        confirmarEstadoParaUndo();
    }

    public void alteraEmpregado(String emp, String atributo, String valor) throws Exception {
        salvarEstadoParaUndo();
        if (emp.isEmpty()) throw new CampoNuloException("Identificacao do empregado nao pode ser nula.");
        Empregado empregado = empregados.get(emp);
        if (empregado == null) throw new EmpregadoNaoExisteException();

        switch (atributo) {
            case "nome":
                if (valor.isEmpty()) throw new CampoNuloException("Nome nao pode ser nulo.");
                empregado.setNome(valor);
                break;
            case "endereco":
                if (valor.isEmpty()) throw new CampoNuloException("Endereco nao pode ser nulo.");
                empregado.setEndereco(valor);
                break;
            case "salario":
                if (valor.isEmpty()) throw new CampoNuloException("Salario nao pode ser nulo.");
                BigDecimal salario;
                try {
                    salario = new BigDecimal(valor.replace(",", "."));
                } catch (Exception e) {
                    throw new ValorNumericoException("Salario deve ser numerico.");
                }
                if (salario.compareTo(BigDecimal.ZERO) < 0) throw new ValorNegativoException("Salario deve ser nao-negativo.");
                empregado.setSalario(salario);
                break;
            case "comissao":
                if (valor.isEmpty()) throw new CampoNuloException("Comissao nao pode ser nula.");
                BigDecimal comissao;
                try {
                    comissao = new BigDecimal(valor.replace(",", "."));
                } catch (Exception e) {
                    throw new ValorNumericoException("Comissao deve ser numerica.");
                }
                if (comissao.compareTo(BigDecimal.ZERO) < 0) throw new ValorNegativoException("Comissao deve ser nao-negativa.");
                empregado.setComissao(comissao);
                break;
            case "metodoPagamento":
                if (!valor.equals("correios") && !valor.equals("emMaos") && !valor.equals("banco")) {
                    throw new Exception("Metodo de pagamento invalido.");
                }
                empregado.setMetodoPagamento(valor);
                break;
            case "sindicalizado":
                if (!valor.equals("true") && !valor.equals("false")) {
                    throw new Exception("Valor deve ser true ou false.");
                }
                if (valor.equals("false")) {
                    empregado.setMembroSindicato(null);
                }
                break;
            case "tipo":
                if (!valor.equals("horista") && !valor.equals("assalariado") && !valor.equals("comissionado")) {
                    throw new TipoInvalidoException();
                }
                mudarTipoEmpregado(emp, empregado, valor, empregado.getSalario(), null);
                break;
            default:
                throw new AtributoNaoExisteException();
        }
        confirmarEstadoParaUndo();
    }

    public void alteraEmpregado(String emp, String atributo, String valor, String idSindicato, String taxaSindical) throws Exception {
        salvarEstadoParaUndo();
        Empregado empregado = empregados.get(emp);
        if (empregado == null) throw new EmpregadoNaoExisteException();

        if (atributo.equals("sindicalizado") && valor.equals("true")) {
            if (idSindicato.isEmpty()) throw new CampoNuloException("Identificacao do sindicato nao pode ser nula.");
            if (taxaSindical.isEmpty()) throw new CampoNuloException("Taxa sindical nao pode ser nula.");
            
            BigDecimal taxa;
            try {
                taxa = new BigDecimal(taxaSindical.replace(",", "."));
            } catch (Exception e) {
                throw new ValorNumericoException("Taxa sindical deve ser numerica.");
            }
            if (taxa.compareTo(BigDecimal.ZERO) < 0) throw new ValorNegativoException("Taxa sindical deve ser nao-negativa.");

            // Verificar duplicidade de ID de Sindicato
            for (Empregado e : empregados.values()) {
                if (e.isSindicalizado() && e.getMembroSindicato().getIdSindicato().equals(idSindicato)) {
                    throw new IdSindicatoDuplicadoException();
                }
            }
            empregado.setMembroSindicato(new MembroSindicato(idSindicato, taxa));
        }
        confirmarEstadoParaUndo();
    }

    public void lancaTaxaServico(String membro, String data, String valor) throws Exception {
        salvarEstadoParaUndo();
        sindicatoService.lancaTaxaServico(empregados, membro, data, valor);
        confirmarEstadoParaUndo();
    }

    public String getTaxasServico(String emp, String dataInicial, String dataFinal) throws Exception {
        return sindicatoService.getTaxasServico(empregados, emp, dataInicial, dataFinal);
    }



    public String totalFolha(String data) throws Exception {
        return folhaService.totalFolha(empregados, data);
    }

    public void rodaFolha(String data, String saida) throws Exception {
        salvarEstadoParaUndo();
        folhaService.rodaFolha(empregados, data, saida);
        confirmarEstadoParaUndo();
    }

    public void encerrarSistema() {
        XMLHelper.salvar(empregados, ident_int);
        isEncerrado = true;
    }

    public String getNumeroDeEmpregados() {
        return String.valueOf(empregados.size());
    }

    public void undo() throws Exception {
        if (isEncerrado) throw new Exception("Nao pode dar comandos depois de encerrarSistema.");
        if (undoStack.isEmpty()) throw new Exception("Nao ha comando a desfazer.");
        redoStack.push(XMLHelper.salvarParaString(empregados, ident_int));
        String xml = undoStack.pop();
        empregados = XMLHelper.carregarDeString(xml);
        ident_int = XMLHelper.extrairProximoIdDeString(xml);
    }

    public void redo() throws Exception {
        if (isEncerrado) throw new Exception("Nao pode dar comandos depois de encerrarSistema.");
        if (redoStack.isEmpty()) throw new Exception("Nao ha comando a refazer.");
        undoStack.push(XMLHelper.salvarParaString(empregados, ident_int));
        String xml = redoStack.pop();
        empregados = XMLHelper.carregarDeString(xml);
        ident_int = XMLHelper.extrairProximoIdDeString(xml);
    }
}
