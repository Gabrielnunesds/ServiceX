package br.com.gabrielnunesds.servicex.pagamento.domain;

import br.com.gabrielnunesds.servicex.ordemservico.domain.OrdemServico;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Getter
@Setter
@Entity
@JsonTypeName("pagamentoPix")
public class PagamentoPix extends Pagamento{
    private static final long serialVersionUID = 1L;

    @Column(name = "CHAVE_PIX")
    private String chavePix;

    public PagamentoPix(){}

    public PagamentoPix(Integer idPagamento, StatusPagamento statusPagmento, OrdemServico ordemServico, String chavePix) {
        super(idPagamento, statusPagmento, ordemServico);
        this.chavePix = chavePix;
    }

    public PagamentoPix(String chavePix) {
        this.chavePix = chavePix;
    }
}
