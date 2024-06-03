package br.com.gabrielnunesds.servicex.pagamento.domain;

import br.com.gabrielnunesds.servicex.ordemservico.domain.OrdemServico;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@JsonTypeName("pagamentoBoleto")
public class PagamentoBoleto extends Pagamento{

    @Column(name = "DATA_VENCIMENTO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dataVencimento;
    @Column(name = "DATA_PAGAMENTO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dataPagamento;

    public PagamentoBoleto(){}

    public PagamentoBoleto(Integer idPagamento, StatusPagamento statusPagmento, OrdemServico ordemServico, Date dataVencimento, Date dataPagamento) {
        super(idPagamento, statusPagmento, ordemServico);
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
    }
    public PagamentoBoleto(Date dataVencimento, Date dataPagamento) {
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
    }
}
