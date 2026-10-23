package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.conversao.ConversorBooleano;
import br.ufal.ic.p2.wepayu.conversao.ConversorData;
import br.ufal.ic.p2.wepayu.conversao.ConversorNumero;
import br.ufal.ic.p2.wepayu.exception.WePayUException;
import br.ufal.ic.p2.wepayu.exception.data.DataInvalidaException;
import br.ufal.ic.p2.wepayu.exception.naoencontrado.AtributoNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.naoencontrado.MembroNaoExisteException;
import br.ufal.ic.p2.wepayu.exception.nulo.IdentificacaoMembroNulaException;
import br.ufal.ic.p2.wepayu.exception.nulo.IdentificacaoSindicatoNulaException;
import br.ufal.ic.p2.wepayu.exception.nulo.TaxaSindicalNulaException;
import br.ufal.ic.p2.wepayu.exception.numerico.TaxaSindicalNaoNumericaException;
import br.ufal.ic.p2.wepayu.exception.numerico.ValorLancamentoNaoNumericoException;
import br.ufal.ic.p2.wepayu.exception.sindicato.IdSindicatoDuplicadoException;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.MembroSindicato;
import br.ufal.ic.p2.wepayu.models.Periodo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GerenciaSindicato {
    private final GerenciaEmpregado empregados;

    public GerenciaSindicato(GerenciaEmpregado empregados) {
        this.empregados = empregados;
    }

    public void alterarSindicalizacao(String id, String atributo, String valor, String idSindicato, String taxaInformada) throws WePayUException {
        Empregado empregado = empregados.buscar(id);
        if (!"sindicalizado".equals(atributo)) {
            throw new AtributoNaoExisteException();
        }
        if (!ConversorBooleano.converter(valor)) {
            empregado.desfiliar();
            return;
        }
        if (idSindicato == null || idSindicato.isEmpty()) {
            throw new IdentificacaoSindicatoNulaException();
        }
        BigDecimal taxaSindical = ConversorNumero.converter(taxaInformada, TaxaSindicalNulaException::new, TaxaSindicalNaoNumericaException::new);
        if (pertenceAOutro(idSindicato, empregado)) {
            throw new IdSindicatoDuplicadoException();
        }
        empregado.sindicalizar(idSindicato, taxaSindical);
    }

    public void lancaTaxaServico(String idMembro, String dataInformada, String valorInformado) throws WePayUException {
        if (idMembro == null || idMembro.isEmpty()) {
            throw new IdentificacaoMembroNulaException();
        }
        MembroSindicato membro = empregados.buscarPorIdSindicato(idMembro)
                .orElseThrow(MembroNaoExisteException::new)
                .comoMembroSindicato();
        LocalDate data = ConversorData.converter(dataInformada, DataInvalidaException::new);
        BigDecimal valor = ConversorNumero.converter(valorInformado, ValorLancamentoNaoNumericoException::new);
        membro.lancaTaxaServico(data, valor);
    }

    public String getTaxasServico(String id, String dataInicial, String dataFinal) throws WePayUException {
        MembroSindicato membro = empregados.buscar(id).comoMembroSindicato();
        Periodo periodo = ConversorData.converterPeriodo(dataInicial, dataFinal);
        return ConversorNumero.formatarMoeda(membro.totalTaxasServico(periodo));
    }

    private boolean pertenceAOutro(String idSindicato, Empregado empregado) {
        return empregados.buscarPorIdSindicato(idSindicato)
                .filter(dono -> dono != empregado)
                .isPresent();
    }
}
