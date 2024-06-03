package br.com.gabrielnunesds.servicex.categoria.resources;

import br.com.gabrielnunesds.servicex.categoria.domain.Categoria;
import br.com.gabrielnunesds.servicex.categoria.domain.CategoriaDTO;
import br.com.gabrielnunesds.servicex.categoria.domain.CategoriaDTOResponse;
import br.com.gabrielnunesds.servicex.categoria.services.CategoriaService;
import br.com.gabrielnunesds.servicex.exceptios.NomeCategoriaJaExistenteException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/categorias")
@Tag(name = "Manter Categoria", description = "Funcionalidade das operações de Categoria")
public class CategoriaResource {

    @Autowired
    private CategoriaService categoriaService;

    @Operation(summary = "Cadastrar Categoria", description = "O recurso permite cadastrar, porém não pode repetir o mesmo nome.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cadastro com sucesso",
                    content = @Content(
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"idCategoria\": 5,\n" +
                                    "  \"nomeCategoria\": \"Marketing\",\n" +
                                    "  \"mensagem\": \"Cadastro realizado com sucesso\"\n" +
                                    "}")
                    )),
            @ApiResponse(responseCode = "400", description = "Já existe produto com esse nome",
                    content = @Content(
                            examples = @ExampleObject(value = "{\"message\": \"Já existe uma categoria com esse nome\"}")
                    )),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "403", description = "Rota exclusiva para administradores (administrador = true)")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> cadastrarCategoria(@Valid @RequestBody CategoriaDTOResponse responseDTO){
        try {
            Categoria categoria = categoriaService.fromDTOResponse(responseDTO);
            var novacategoria = categoriaService.criarCategoria(categoria);

            CategoriaDTO resposta = new CategoriaDTO();
            resposta.setMensagem("Cadastro realizado com sucesso");
            resposta.setIdCategoria(novacategoria.getIdCategoria());
            resposta.setNomeCategoria(novacategoria.getNomeCategoria());

            URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                    .buildAndExpand(categoria.getIdCategoria()).toUri();
            return  ResponseEntity.created(uri).body(resposta);
        } catch (NomeCategoriaJaExistenteException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\n" +
                            "  \"message\": \"Já existe uma categoria com esse nome\"\n" +
                            "}");
        }
    }


    @Operation(summary = "Listar Categoria", description = "O recurso listar todas as categorias.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso"),
            @ApiResponse(responseCode = "404", description = "Não encontrado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listarCategorias(){
        List<Categoria> listaCategorias = categoriaService.listarCategorias();
        List<CategoriaDTO> listaDTO = listaCategorias.stream().map(CategoriaDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listaDTO);
    }

    @Operation(summary = "Buscar Categoria", description = "O recurso permite buscar uma categoria pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso"),
            @ApiResponse(responseCode = "404", description = "Não encontrado")
    })
    @GetMapping("/{idCategoria}")
    public ResponseEntity<Categoria> buscarCategoriaPorId(@PathVariable Integer idCategoria) {
        Categoria categoria = categoriaService.buscarCategoriaPorId(idCategoria);
        return ResponseEntity.ok().body(categoria);
    }

    @Operation(summary = "Atualizar Categoria", description = "O recurso atualizat uma categoria pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso"),
            @ApiResponse(responseCode = "404", description = "Não encontrado")
    })
    @PutMapping("/{idCategoria}")
    public ResponseEntity<Categoria> atualizarCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO, @PathVariable Integer idCategoria) {
        Categoria categoria = categoriaService.fromDTO(categoriaDTO);
        categoria.setIdCategoria(idCategoria);
        categoria = categoriaService.atualizarCategoria(categoria);
        return ResponseEntity.ok().body(categoria);
    }

    @Operation(summary = "Deletar Categoria", description = "O recurso permite deletar uma categoria pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso"),
            @ApiResponse(responseCode = "404", description = "Não encontrado")
    })
    @DeleteMapping("/{idCategoria}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable Integer idCategoria) {
        categoriaService.deletarCategoria(idCategoria);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
