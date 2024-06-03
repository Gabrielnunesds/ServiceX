package br.com.gabrielnunesds.servicex.ordemservico.resources;

import br.com.gabrielnunesds.servicex.ordemservico.domain.OrdemServico;
import br.com.gabrielnunesds.servicex.ordemservico.services.OrdemServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/ordemservicos")
public class OrdemServicoResource {
    @Autowired
    private OrdemServicoService ordemServicoService;

    @PostMapping
    public ResponseEntity<OrdemServico> criarOrdemServico(@RequestBody OrdemServico ordemServico){
       ordemServicoService.criarOrdemServico(ordemServico);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(ordemServico.getIdOrdemServico()).toUri();
        return ResponseEntity.created(uri).build();
       // return new ResponseEntity<>(os, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrdemServico>> listarOrdemServico() {
        List<OrdemServico> os = ordemServicoService.listarOrdemServico();
        return new ResponseEntity<>(os, HttpStatus.OK);
    }

    @GetMapping("/{idOrdemServico}")
    public ResponseEntity<OrdemServico> buscarOrdemServico(@PathVariable Integer idOrdemServico) {
        return ordemServicoService.buscarOrdemServicoPorId(idOrdemServico)
                .map(os -> new ResponseEntity<>(os, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{idOrdemServico}")
    public ResponseEntity<OrdemServico> atualizarOrdemServico(@PathVariable Integer idOrdemServico, @RequestBody OrdemServico os) {
        if (!ordemServicoService.buscarOrdemServicoPorId(idOrdemServico).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        os.setIdOrdemServico(idOrdemServico);
        OrdemServico osAtualizada = ordemServicoService.atualizarOrdemServico(os);
        return new ResponseEntity<>(osAtualizada, HttpStatus.OK);
    }

    @DeleteMapping("/{idOrdemServico}")
    public ResponseEntity<Void> deletarOrdemServico(@PathVariable Integer idCategoria) {
        ordemServicoService.deletarOrdemServico(idCategoria);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
