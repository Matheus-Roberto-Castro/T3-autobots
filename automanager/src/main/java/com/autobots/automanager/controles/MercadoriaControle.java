package com.autobots.automanager.controles;

import com.autobots.automanager.DTO.MercadoriaDTO;
import com.autobots.automanager.conversores.MercadoriaConversor;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.servicos.MercadoriaServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/mercadorias")
public class MercadoriaControle {

    @Autowired
    private MercadoriaServico servicoMercadoria;

    @Autowired
    private MercadoriaConversor conversor;

    @PostMapping
    public ResponseEntity<MercadoriaDTO> salvarMercadoria(@RequestBody MercadoriaDTO dto) {
        Mercadoria entidade = conversor.converterParaEntidade(dto);
        Mercadoria novaMercadoria = servicoMercadoria.salvar(entidade);

        MercadoriaDTO novoDto = conversor.converterParaDto(novaMercadoria);
        novoDto.add(linkTo(methodOn(MercadoriaControle.class).buscarMercadoriaPorId(novoDto.getId())).withSelfRel());

        return new ResponseEntity<>(novoDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MercadoriaDTO> buscarMercadoriaPorId(@PathVariable Long id) {
        return servicoMercadoria.buscarPorId(id)
                .map(entidade -> {
                    MercadoriaDTO dto = conversor.converterParaDto(entidade);

                    dto.add(linkTo(methodOn(MercadoriaControle.class).buscarMercadoriaPorId(id)).withSelfRel());
                    dto.add(linkTo(methodOn(MercadoriaControle.class).buscarTodasMercadorias()).withRel("mercadorias"));

                    return new ResponseEntity<>(dto, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<MercadoriaDTO>> buscarTodasMercadorias() {
        List<Mercadoria> entidades = servicoMercadoria.buscarTodos();

        List<MercadoriaDTO> dtosComLinks = entidades.stream()
                .map(conversor::converterParaDto)
                .map(dto -> {
                    dto.add(linkTo(methodOn(MercadoriaControle.class).buscarMercadoriaPorId(dto.getId())).withSelfRel());
                    return dto;
                })
                .collect(Collectors.toList());

        CollectionModel<MercadoriaDTO> collectionModel = CollectionModel.of(dtosComLinks);
        collectionModel.add(linkTo(methodOn(MercadoriaControle.class).buscarTodasMercadorias()).withSelfRel());

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MercadoriaDTO> atualizarMercadoria(@PathVariable Long id, @RequestBody MercadoriaDTO dto) {
        try {
            Mercadoria entidade = conversor.converterParaEntidade(dto);
            Mercadoria mercadoriaAtualizada = servicoMercadoria.atualizar(id, entidade);

            MercadoriaDTO dtoAtualizado = conversor.converterParaDto(mercadoriaAtualizada);
            dtoAtualizado.add(linkTo(methodOn(MercadoriaControle.class).buscarMercadoriaPorId(dtoAtualizado.getId())).withSelfRel());
            return new ResponseEntity<>(dtoAtualizado, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMercadoria(@PathVariable Long id) {
        try {
            servicoMercadoria.deletar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }
}
