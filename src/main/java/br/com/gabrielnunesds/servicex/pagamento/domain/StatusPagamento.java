package br.com.gabrielnunesds.servicex.pagamento.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Java é um tipo de dados especial que permite definir um conjunto fixo
 * de constantes nomeadas. Essas constantes são chamadas de "enumeradores"*/
@Getter
@AllArgsConstructor
public enum StatusPagamento {
    PENDENTE(1, "Pendente"),
    QUITADO(2, "Quitado"),
    CANCELADO(3, "Cancelado");

    private int cod;
    private String descricao;

    public static StatusPagamento toEnum(Integer cod){
        for (StatusPagamento pagamento : StatusPagamento.values()){
            if (cod.equals(pagamento.getCod())){
                return pagamento;
            }
        }

        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
