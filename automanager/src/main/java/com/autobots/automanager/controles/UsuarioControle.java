package com.autobots.automanager.controles;

import com.autobots.automanager.DTO.UsuarioDTO;
import com.autobots.automanager.conversores.UsuarioConversor;
import com.autobots.automanager.entitades.Credencial;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.servicos.UsuarioServico;
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
@RequestMapping("/usuarios")
public class UsuarioControle {

    @Autowired
    private UsuarioServico servicoUsuario;

    @Autowired
    private UsuarioConversor conversor;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvarUsuario(@RequestBody UsuarioDTO dto) {
        Usuario entidade = conversor.converterParaEntidade(dto);
        Usuario novoUsuario = servicoUsuario.salvar(entidade);

        UsuarioDTO novoDto = conversor.converterParaDto(novoUsuario);
        novoDto.add(linkTo(methodOn(UsuarioControle.class).buscarUsuarioPorId(novoDto.getId())).withSelfRel());

        return new ResponseEntity<>(novoDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorId(@PathVariable Long id) {
        return servicoUsuario.buscarPorId(id)
                .map(entidade -> {
                    UsuarioDTO dto = conversor.converterParaDto(entidade);

                    dto.add(linkTo(methodOn(UsuarioControle.class).buscarUsuarioPorId(id)).withSelfRel());
                    dto.add(linkTo(methodOn(UsuarioControle.class).buscarTodosUsuarios()).withRel("usuarios"));
                    dto.add(linkTo(methodOn(VeiculoControle.class).buscarTodosVeiculos()).withRel("veiculos"));
                    dto.add(linkTo(methodOn(VendaControle.class).buscarTodasVendas()).withRel("vendas"));

                    return new ResponseEntity<>(dto, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<UsuarioDTO>> buscarTodosUsuarios() {
        List<Usuario> entidades = servicoUsuario.buscarTodos();

        List<UsuarioDTO> dtosComLinks = entidades.stream()
                .map(conversor::converterParaDto)
                .map(dto -> {
                    dto.add(linkTo(methodOn(UsuarioControle.class).buscarUsuarioPorId(dto.getId())).withSelfRel());
                    return dto;
                })
                .collect(Collectors.toList());

        CollectionModel<UsuarioDTO> collectionModel = CollectionModel.of(dtosComLinks);
        collectionModel.add(linkTo(methodOn(UsuarioControle.class).buscarTodosUsuarios()).withSelfRel());

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        try {
            Usuario entidade = conversor.converterParaEntidade(dto);
            Usuario usuarioAtualizado = servicoUsuario.atualizar(id, entidade);

            UsuarioDTO dtoAtualizado = conversor.converterParaDto(usuarioAtualizado);
            dtoAtualizado.add(linkTo(methodOn(UsuarioControle.class).buscarUsuarioPorId(dtoAtualizado.getId())).withSelfRel());
            return new ResponseEntity<>(dtoAtualizado, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        try {
            servicoUsuario.deletar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }

    @PostMapping("/{id}/credenciais")
    public ResponseEntity<UsuarioDTO> adicionarCredencial(@PathVariable Long id, @RequestBody CredencialUsuarioSenha novaCredencial) {
        try {
            Usuario usuarioAtualizado = servicoUsuario.adicionarCredencial(id, novaCredencial);

            UsuarioDTO dtoAtualizado = conversor.converterParaDto(usuarioAtualizado);
            dtoAtualizado.add(linkTo(methodOn(UsuarioControle.class).buscarUsuarioPorId(dtoAtualizado.getId())).withSelfRel());
            return new ResponseEntity<>(dtoAtualizado, HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }

    @DeleteMapping("/{id}/credenciais/{credencialId}")
    public ResponseEntity<Void> removerCredencial(@PathVariable Long id, @PathVariable Long credencialId) {
        try {
            servicoUsuario.removerCredencial(id, credencialId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }
}