package com.autobots.automanager.controles;

import com.autobots.automanager.DTO.ServicoDTO;
import com.autobots.automanager.conversores.ServicoConversor;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.servicos.ServicoServico;
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
@RequestMapping("/servicos")
public class ServicoControle {

    @Autowired
    private ServicoServico servicoServico;

    @Autowired
    private ServicoConversor conversor;

    @PostMapping
    public ResponseEntity<ServicoDTO> salvarServico(@RequestBody ServicoDTO dto) {
        Servico entidade = conversor.converterParaEntidade(dto);
        Servico novoServico = servicoServico.salvar(entidade);

        ServicoDTO novoDto = conversor.converterParaDto(novoServico);
        novoDto.add(linkTo(methodOn(ServicoControle.class).buscarServicoPorId(novoDto.getId())).withSelfRel());

        return new ResponseEntity<>(novoDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoDTO> buscarServicoPorId(@PathVariable Long id) {
        return servicoServico.buscarPorId(id)
                .map(entidade -> {
                    ServicoDTO dto = conversor.converterParaDto(entidade);

                    dto.add(linkTo(methodOn(ServicoControle.class).buscarServicoPorId(id)).withSelfRel());
                    dto.add(linkTo(methodOn(ServicoControle.class).buscarTodosServicos()).withRel("servicos"));

                    return new ResponseEntity<>(dto, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<ServicoDTO>> buscarTodosServicos() {
        List<Servico> entidades = servicoServico.buscarTodos();

        List<ServicoDTO> dtosComLinks = entidades.stream()
                .map(conversor::converterParaDto)
                .map(dto -> {
                    dto.add(linkTo(methodOn(ServicoControle.class).buscarServicoPorId(dto.getId())).withSelfRel());
                    return dto;
                })
                .collect(Collectors.toList());

        CollectionModel<ServicoDTO> collectionModel = CollectionModel.of(dtosComLinks);
        collectionModel.add(linkTo(methodOn(ServicoControle.class).buscarTodosServicos()).withSelfRel());

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoDTO> atualizarServico(@PathVariable Long id, @RequestBody ServicoDTO dto) {
        try {
            Servico entidade = conversor.converterParaEntidade(dto);
            Servico servicoAtualizado = servicoServico.atualizar(id, entidade);

            ServicoDTO dtoAtualizado = conversor.converterParaDto(servicoAtualizado);
            dtoAtualizado.add(linkTo(methodOn(ServicoControle.class).buscarServicoPorId(dtoAtualizado.getId())).withSelfRel());
            return new ResponseEntity<>(dtoAtualizado, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id) {
        try {
            servicoServico.deletar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }
}