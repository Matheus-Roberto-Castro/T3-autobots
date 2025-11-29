package com.autobots.automanager.controles;

import com.autobots.automanager.DTO.EmpresaDTO;
import com.autobots.automanager.conversores.EmpresaConversor;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.servicos.EmpresaServico;
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
@RequestMapping("/empresas")
public class EmpresaControle {

    @Autowired
    private EmpresaServico servicoEmpresa;

    @Autowired
    private EmpresaConversor conversor;

    @PostMapping
    public ResponseEntity<EmpresaDTO> salvarEmpresa(@RequestBody EmpresaDTO dto) {
        Empresa entidade = conversor.converterParaEntidade(dto);
        Empresa novaEmpresa = servicoEmpresa.salvar(entidade);

        EmpresaDTO novoDto = conversor.converterParaDto(novaEmpresa);
        novoDto.add(linkTo(methodOn(EmpresaControle.class).buscarEmpresaPorId(novoDto.getId())).withSelfRel());

        return new ResponseEntity<>(novoDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDTO> buscarEmpresaPorId(@PathVariable Long id) {
        return servicoEmpresa.buscarPorId(id)
                .map(entidade -> {
                    EmpresaDTO dto = conversor.converterParaDto(entidade);

                    dto.add(linkTo(methodOn(EmpresaControle.class).buscarEmpresaPorId(id)).withSelfRel());
                    dto.add(linkTo(methodOn(EmpresaControle.class).buscarTodasEmpresas()).withRel("empresas"));

                    dto.add(linkTo(methodOn(UsuarioControle.class).buscarTodosUsuarios()).withRel("usuarios"));
                    dto.add(linkTo(methodOn(MercadoriaControle.class).buscarTodasMercadorias()).withRel("mercadorias"));
                    dto.add(linkTo(methodOn(ServicoControle.class).buscarTodosServicos()).withRel("servicos"));
                    dto.add(linkTo(methodOn(VendaControle.class).buscarTodasVendas()).withRel("vendas"));

                    return new ResponseEntity<>(dto, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EmpresaDTO>> buscarTodasEmpresas() {
        List<Empresa> entidades = servicoEmpresa.buscarTodos();

        List<EmpresaDTO> dtosComLinks = entidades.stream()
                .map(conversor::converterParaDto)
                .map(dto -> {
                    dto.add(linkTo(methodOn(EmpresaControle.class).buscarEmpresaPorId(dto.getId())).withSelfRel());
                    return dto;
                })
                .collect(Collectors.toList());

        CollectionModel<EmpresaDTO> collectionModel = CollectionModel.of(dtosComLinks);
        collectionModel.add(linkTo(methodOn(EmpresaControle.class).buscarTodasEmpresas()).withSelfRel());

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDTO> atualizarEmpresa(@PathVariable Long id, @RequestBody EmpresaDTO dto) {
        try {
            Empresa entidade = conversor.converterParaEntidade(dto);
            Empresa empresaAtualizada = servicoEmpresa.atualizar(id, entidade);

            EmpresaDTO dtoAtualizado = conversor.converterParaDto(empresaAtualizada);
            dtoAtualizado.add(linkTo(methodOn(EmpresaControle.class).buscarEmpresaPorId(dtoAtualizado.getId())).withSelfRel());
            return new ResponseEntity<>(dtoAtualizado, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEmpresa(@PathVariable Long id) {
        try {
            servicoEmpresa.deletar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }

    @PostMapping("/{idEmpresa}/usuarios/{idUsuario}")
    public ResponseEntity<EmpresaDTO> associarUsuario(
            @PathVariable Long idEmpresa,
            @PathVariable Long idUsuario) {

        try {
            Empresa empresaAtualizada = servicoEmpresa.associarUsuario(idEmpresa, idUsuario);

            EmpresaDTO dto = conversor.converterParaDto(empresaAtualizada);
            dto.add(linkTo(methodOn(EmpresaControle.class).buscarEmpresaPorId(dto.getId())).withSelfRel());

            return new ResponseEntity<>(dto, HttpStatus.OK);

        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }

    @DeleteMapping("/{idEmpresa}/usuarios/{idUsuario}")
    public ResponseEntity<EmpresaDTO> desassociarUsuario(
            @PathVariable Long idEmpresa,
            @PathVariable Long idUsuario) {

        try {
            Empresa empresaAtualizada = servicoEmpresa.desassociarUsuario(idEmpresa, idUsuario);

            EmpresaDTO dto = conversor.converterParaDto(empresaAtualizada);
            dto.add(linkTo(methodOn(EmpresaControle.class).buscarEmpresaPorId(dto.getId())).withSelfRel());

            return new ResponseEntity<>(dto, HttpStatus.OK);

        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }

}