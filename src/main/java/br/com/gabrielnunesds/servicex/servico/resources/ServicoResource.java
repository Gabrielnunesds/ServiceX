package br.com.gabrielnunesds.servicex.servico.resources;

import br.com.gabrielnunesds.servicex.servico.domain.Servico;
import br.com.gabrielnunesds.servicex.servico.domain.ServicoDTO;
import br.com.gabrielnunesds.servicex.servico.services.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/servicos")
public class ServicoResource {

    @Autowired
    private ServicoService servicoService;

    @PostMapping
    public ResponseEntity<Servico> criarServico(@RequestBody ServicoDTO servicoDTO) {
        Servico servico = servicoService.fromDTOService(servicoDTO);
        servico = servicoService.criarServico(servico);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(servico.getIdServico()).toUri();
        return  ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<Servico>> listarServicos() {
        List<Servico> servicos = servicoService.listarServico();
        return new ResponseEntity<>(servicos, HttpStatus.OK);
    }

    @GetMapping("/{idServico}")
    public ResponseEntity<Servico> buscarServico(@PathVariable Integer idServico) {
        Servico servico = servicoService.buscarServico(idServico);
        return ResponseEntity.ok().body(servico);
    }

    @PutMapping("/{idServico}")
    public ResponseEntity<Servico> atualizarServico(@PathVariable Integer idServico, @RequestBody ServicoDTO servicoDTO) {
        Servico servico = servicoService.fromDTOService(servicoDTO);
        servico.setIdServico(idServico);
        servicoService.atualizarServico(servico);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idServico}")
    public ResponseEntity<Void> deletarServico(@PathVariable Integer idServico) {
        servicoService.deletarServico(idServico);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
