package com.autobots.automanager.controles;

import com.autobots.automanager.DTO.VeiculoDTO;
import com.autobots.automanager.conversores.VeiculoConversor;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.servicos.VeiculoServico;
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
@RequestMapping("/veiculos")
public class VeiculoControle {

    @Autowired
    private VeiculoServico servicoVeiculo;

    @Autowired
    private VeiculoConversor conversor;

    @PostMapping
    public ResponseEntity<VeiculoDTO> salvarVeiculo(@RequestBody VeiculoDTO dto) {
        Veiculo entidade = conversor.converterParaEntidade(dto);
        Veiculo novoVeiculo = servicoVeiculo.salvar(entidade);

        VeiculoDTO novoDto = conversor.converterParaDto(novoVeiculo);
        novoDto.add(linkTo(methodOn(VeiculoControle.class).buscarVeiculoPorId(novoDto.getId())).withSelfRel());

        return new ResponseEntity<>(novoDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoDTO> buscarVeiculoPorId(@PathVariable Long id) {
        return servicoVeiculo.buscarPorId(id)
                .map(entidade -> {
                    VeiculoDTO dto = conversor.converterParaDto(entidade);

                    dto.add(linkTo(methodOn(VeiculoControle.class).buscarVeiculoPorId(id)).withSelfRel());
                    dto.add(linkTo(methodOn(VeiculoControle.class).buscarTodosVeiculos()).withRel("veiculos"));

                    if (dto.getProprietarioId() != null) {
                        dto.add(linkTo(methodOn(UsuarioControle.class).buscarUsuarioPorId(dto.getProprietarioId())).withRel("proprietario"));
                    }

                    return new ResponseEntity<>(dto, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<VeiculoDTO>> buscarTodosVeiculos() {
        List<Veiculo> entidades = servicoVeiculo.buscarTodos();

        List<VeiculoDTO> dtosComLinks = entidades.stream()
                .map(conversor::converterParaDto)
                .map(dto -> {
                    dto.add(linkTo(methodOn(VeiculoControle.class).buscarVeiculoPorId(dto.getId())).withSelfRel());
                    return dto;
                })
                .collect(Collectors.toList());

        CollectionModel<VeiculoDTO> collectionModel = CollectionModel.of(dtosComLinks);
        collectionModel.add(linkTo(methodOn(VeiculoControle.class).buscarTodosVeiculos()).withSelfRel());

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoDTO> atualizarVeiculo(@PathVariable Long id, @RequestBody VeiculoDTO dto) {
        try {
            Veiculo entidade = conversor.converterParaEntidade(dto);
            Veiculo veiculoAtualizado = servicoVeiculo.atualizar(id, entidade);

            VeiculoDTO dtoAtualizado = conversor.converterParaDto(veiculoAtualizado);
            dtoAtualizado.add(linkTo(methodOn(VeiculoControle.class).buscarVeiculoPorId(dtoAtualizado.getId())).withSelfRel());
            return new ResponseEntity<>(dtoAtualizado, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVeiculo(@PathVariable Long id) {
        try {
            servicoVeiculo.deletar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }
}