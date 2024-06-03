package br.com.gabrielnunesds.servicex.servico.services;

import br.com.gabrielnunesds.servicex.categoria.repositories.CategoriaRepository;
import br.com.gabrielnunesds.servicex.exceptios.ExceptionDataIntegrityViolation;
import br.com.gabrielnunesds.servicex.exceptios.ObjectNotFoundException;
import br.com.gabrielnunesds.servicex.servico.domain.Servico;
import br.com.gabrielnunesds.servicex.servico.domain.ServicoDTO;
import br.com.gabrielnunesds.servicex.servico.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {
    @Autowired
    private ServicoRepository servicoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    public Servico criarServico(Servico servico) {
        try {
            servico.setIdServico(null);
            servico =  servicoRepository.save(servico);
            servico.setCategoria(categoriaRepository.findByServicos_IdServico(servico.getIdServico()));
            return servico;
        }
        catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Categoria informada inexistete!");
        }
    }

    public Servico buscarServico(Integer idServico) {
        Optional<Servico> obj = servicoRepository.findById(idServico);

        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrato! ID: " + idServico +
                ", Tipo: " + Servico.class.getName()));
    }

    public List<Servico> listarServico() {
        return servicoRepository.findAll();
    }

    public Servico atualizarServico(Servico servico) {

        Servico novoServico = buscarServico(servico.getIdServico());
        updateData(novoServico, servico);
        return servicoRepository.save(novoServico);
    }

    public void deletarServico(Integer id) {
        buscarServico(id);
        try {
            servicoRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e){
            throw new ExceptionDataIntegrityViolation("Existe uma associação entre categorias e serviços." +
                    " Este procedimento não pode ser concluído!");
        }

    }
    public Servico fromDTOService(ServicoDTO servicoDTO){
        return new Servico(servicoDTO.getIdServico(), servicoDTO.getNome(), servicoDTO.getValor(), servicoDTO.getCategoria());

    }
    private void updateData(Servico novoServico, Servico servico){
        novoServico.setNome(servico.getNome());
        novoServico.setValor(servico.getValor());
        novoServico.setCategoria(servico.getCategoria());
    }

}
