package br.com.gabrielnunesds.servicex.pagamento.domain;


import br.com.gabrielnunesds.servicex.ordemservico.domain.OrdemServico;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.Objects;

/**
 * No caso de InheritanceType.JOINED, cada classe concreta na hierarquia de herança tem sua própria tabela no banco de dados.
 * Isso significa que as tabelas no banco de dados estão relacionadas por meio de chaves estrangeiras para representar a relação de herança entre as classes Java.
 *
 *
 * @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type"):
 *
 * Esta anotação é usada para informar ao Jackson sobre a maneira como os tipos de objetos devem ser incluídos (ou não) na serialização/desserialização de JSON.
 * use = JsonTypeInfo.Id.NAME especifica que o tipo será incluído no JSON usando o nome da classe.
 * include = JsonTypeInfo.As.PROPERTY indica que o tipo será incluído como uma propriedade separada no JSON.
 * property = "@type" especifica o nome da propriedade que conterá o tipo do objeto.
 *
 * No exemplo fornecido, está informando ao Jackson que a classe PagamentoBoleto é um subtipo da classe abstrata Pagamento
 * e que, durante a desserialização, deve criar uma instância de PagamentoBoleto quando encontrar a propriedade @type com o valor "pagamentoBoleto" no JSON.
 * */


@Entity
@Table(name="PAGAMENTOS")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PagamentoBoleto.class, name = "pagamentoBoleto")
})
public abstract class Pagamento{

    @Id
    private Integer idPagamento;
    @Column(name = "STATUS_PAGAMENTO")
    private Integer statusPagamento;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "ID_ORDEMSERVICO")
    @MapsId
    private OrdemServico ordemServico;

    public Pagamento(Integer idPagamento, StatusPagamento statusPagmento, OrdemServico ordemServico) {
        super();
        this.idPagamento = idPagamento;
        this.statusPagamento = (statusPagamento == null) ? null : statusPagmento.getCod();
        this.ordemServico = ordemServico;
    }

    public Pagamento() {

    }

    public Integer getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(Integer idPagamento) {
        this.idPagamento = idPagamento;
    }

    public StatusPagamento getStatusPagamento() {
        return StatusPagamento.toEnum(statusPagamento);
    }

    public void setStatusPagamento(StatusPagamento statusPagamento) {
        this.statusPagamento = statusPagamento.getCod();
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Pagamento pagamento = (Pagamento) o;
        return idPagamento != null && Objects.equals(idPagamento, pagamento.idPagamento);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}

