package br.com.gabrielnunesds.servicex.ordemservico.domain;


import br.com.gabrielnunesds.servicex.pagamento.domain.Pagamento;
import br.com.gabrielnunesds.servicex.servico.domain.Servico;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;


@Data
@NoArgsConstructor
@Entity
@Table(name = "ORDEMSERVICOS")
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idOrdemServico;
    @Column(name = "DATA_SOLICITACAO")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date dataSolicitacao;
    @Column(name = "VALOR_ORDEMSERVICO")
    private Double valorOrdemServico;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "ordemServico")
    private Pagamento pagamento;
//    @ManyToOne
//    @JoinColumn(name = "ID_CLIENTE")
//    private Cliente cliente;
    @ManyToMany
    @JoinTable(name = "SERVICO_OS",
            joinColumns = @JoinColumn(name = "ID_OS"),
            inverseJoinColumns = @JoinColumn(name = "ID_SERVICO"))
    private Set<Servico> servicos;

}
