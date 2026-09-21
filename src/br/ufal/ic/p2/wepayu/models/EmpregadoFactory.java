package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.Horista;
import br.ufal.ic.p2.wepayu.models.Assalariado;
import br.ufal.ic.p2.wepayu.models.Comissionado;
import br.ufal.ic.p2.wepayu.Exception.TipoInvalidoException;
import java.math.BigDecimal;

public class EmpregadoFactory {
    public static Empregado criarEmpregado(String tipo, String nome, String endereco, BigDecimal salario,
                                           BigDecimal comissao) throws TipoInvalidoException{
        switch (tipo){
            case "horista":
                return new Horista(nome, endereco, salario);
            case "assalariado":
                return new Assalariado(nome, endereco, salario);
            case "comissionado":
                return new Comissionado(nome, endereco, salario, comissao);
            default:
                throw new TipoInvalidoException();
        }
    }
}
