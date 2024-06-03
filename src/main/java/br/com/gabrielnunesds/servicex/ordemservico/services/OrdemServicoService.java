package br.com.gabrielnunesds.servicex.ordemservico.services;

import br.com.gabrielnunesds.servicex.ordemservico.domain.OrdemServico;
import br.com.gabrielnunesds.servicex.ordemservico.repositories.OrdemServicoRepository;
import br.com.gabrielnunesds.servicex.pagamento.domain.StatusPagamento;
import br.com.gabrielnunesds.servicex.pagamento.repositories.PagamentoRepository;
import br.com.gabrielnunesds.servicex.pagamento.services.BoletoService;
import br.com.gabrielnunesds.servicex.servico.domain.Servico;
import br.com.gabrielnunesds.servicex.servico.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrdemServicoService {
    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private BoletoService boletoService;
    @Autowired
    private ServicoRepository servicoRepository;
    @Autowired
    private PagamentoRepository pagamentoRepository;
    public OrdemServico criarOrdemServico(OrdemServico ordemServico) {
        try {
            ordemServico.setValorOrdemServico(0d);
            ordemServico.setIdOrdemServico(null);
            ordemServico.setDataSolicitacao(new Date());
            ordemServico.getPagamento().setStatusPagamento(StatusPagamento.PENDENTE);
            ordemServico.getPagamento().setOrdemServico(ordemServico);

            // Recuperar os serviços correspondentes com base nos IDs fornecidos
            Set<Servico> servicos = new HashSet<>();
            for (Servico se : ordemServico.getServicos()) {
                Optional<Servico> servicoOptional = servicoRepository.findById(se.getIdServico());
                servicoOptional.ifPresent(servicos::add);
            }

            // Calcular o valor total da ordem de serviço
            double valorTotal = servicos.stream().mapToDouble(Servico::getValor).sum();
            ordemServico.setValorOrdemServico(valorTotal);
            /**
             * servicos.stream(): Esta parte cria um fluxo (stream) a partir da lista servicos, que permite operações de processamento em cada elemento da lista.

             .mapToDouble(Servico::getValor): Esta parte aplica uma operação de mapeamento em cada elemento do fluxo.
             Aqui, estamos mapeando cada Servico para seu valor, utilizando o método getValor() da classe Servico. Isso resulta em um fluxo de valores double.

             .sum(): Esta parte calcula a soma de todos os elementos do fluxo de valores double resultante do passo anterior.
            * */

            // Salvar a ordem de serviço e os serviços associados no banco de dados
            ordemServico = ordemServicoRepository.save(ordemServico);
            for (Servico servico : servicos) {
                servico.getOrdemServicos().add(ordemServico);
                servicoRepository.save(servico);
            }

            // Salvar o pagamento no banco de dados
            pagamentoRepository.save(ordemServico.getPagamento());

            return ordemServico;
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Dados Inválidos! Verifique e informe os dados corretamente!");
        }
    }

    public Optional<OrdemServico> buscarOrdemServicoPorId(Integer idOrdemServico) {
        return ordemServicoRepository.findById(idOrdemServico);
    }

    public List<OrdemServico> listarOrdemServico() {
        return ordemServicoRepository.findAll();
    }

    public OrdemServico atualizarOrdemServico(OrdemServico ordemServico) {
        return ordemServicoRepository.save(ordemServico);
    }

    public void deletarOrdemServico(Integer idOrdemServico) {
        ordemServicoRepository.deleteById(idOrdemServico);
    }


}
