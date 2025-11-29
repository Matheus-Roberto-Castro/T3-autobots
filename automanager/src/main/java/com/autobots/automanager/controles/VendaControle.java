package com.autobots.automanager.controles;

import com.autobots.automanager.DTO.VendaDTO;
import com.autobots.automanager.conversores.VendaConversor;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.servicos.VendaServico;
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
@RequestMapping("/vendas")
public class VendaControle {

    @Autowired
    private VendaServico servicoVenda;

    @Autowired
    private VendaConversor conversor;

    @PostMapping
    public ResponseEntity<VendaDTO> salvarVenda(@RequestBody VendaDTO dto) {
        Venda entidade = conversor.converterParaEntidade(dto);
        Venda novaVenda = servicoVenda.salvar(entidade);

        VendaDTO novoDto = conversor.converterParaDto(novaVenda);

        novoDto.add(linkTo(methodOn(VendaControle.class).buscarVendaPorId(novoDto.getId())).withSelfRel());

        return new ResponseEntity<>(novoDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaDTO> buscarVendaPorId(@PathVariable Long id) {
        return servicoVenda.buscarPorId(id)
                .map(entidade -> {
                    VendaDTO dto = conversor.converterParaDto(entidade);

                    dto.add(linkTo(methodOn(VendaControle.class).buscarVendaPorId(id)).withSelfRel());
                    dto.add(linkTo(methodOn(VendaControle.class).buscarTodasVendas()).withRel("vendas"));

                    if (dto.getClienteId() != null) {
                        dto.add(linkTo(methodOn(UsuarioControle.class).buscarUsuarioPorId(dto.getClienteId())).withRel("cliente"));
                    }
                    if (dto.getFuncionarioId() != null) {
                        dto.add(linkTo(methodOn(UsuarioControle.class).buscarUsuarioPorId(dto.getFuncionarioId())).withRel("funcionario"));
                    }
                    if (dto.getVeiculoId() != null) {
                        dto.add(linkTo(methodOn(VeiculoControle.class).buscarVeiculoPorId(dto.getVeiculoId())).withRel("veiculo"));
                    }

                    return new ResponseEntity<>(dto, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<VendaDTO>> buscarTodasVendas() {
        List<Venda> entidades = servicoVenda.buscarTodos();

        List<VendaDTO> dtosComLinks = entidades.stream()
                .map(conversor::converterParaDto)
                .map(dto -> {
                    dto.add(linkTo(methodOn(VendaControle.class).buscarVendaPorId(dto.getId())).withSelfRel());
                    return dto;
                })
                .collect(Collectors.toList());

        CollectionModel<VendaDTO> collectionModel = CollectionModel.of(dtosComLinks);
        collectionModel.add(linkTo(methodOn(VendaControle.class).buscarTodasVendas()).withSelfRel());

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendaDTO> atualizarVenda(@PathVariable Long id, @RequestBody VendaDTO dto) {
        try {
            Venda entidade = conversor.converterParaEntidade(dto);
            Venda vendaAtualizada = servicoVenda.atualizar(id, entidade);

            VendaDTO dtoAtualizado = conversor.converterParaDto(vendaAtualizada);
            dtoAtualizado.add(linkTo(methodOn(VendaControle.class).buscarVendaPorId(dtoAtualizado.getId())).withSelfRel());
            return new ResponseEntity<>(dtoAtualizado, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVenda(@PathVariable Long id) {
        try {
            servicoVenda.deletar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }
}
