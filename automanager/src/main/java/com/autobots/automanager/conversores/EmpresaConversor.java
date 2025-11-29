package com.autobots.automanager.conversores;

import com.autobots.automanager.DTO.EmpresaDTO;
import com.autobots.automanager.DTO.UsuarioDTO;
import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.entitades.Empresa;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EmpresaConversor extends RepresentationModel<EmpresaConversor> {

    public EmpresaDTO converterParaDto(Empresa entidade) {
        EmpresaDTO dto = new EmpresaDTO();
        dto.setId(entidade.getId());
        dto.setRazaoSocial(entidade.getRazaoSocial());
        dto.setNomeFantasia(entidade.getNomeFantasia());
        dto.setCadastro(entidade.getCadastro());
        dto.setEndereco(entidade.getEndereco());
        dto.setTelefones(entidade.getTelefones());
        Set<UsuarioDTO> usuariosDTO = entidade.getUsuarios()
                .stream()
                .map(usuario -> {
                    UsuarioDTO udto = new UsuarioDTO();
                    udto.setId(usuario.getId());
                    udto.setNome(usuario.getNome());
                    udto.setNomeSocial(usuario.getNomeSocial());
                    udto.add(linkTo(methodOn(UsuarioControle.class).buscarUsuarioPorId(usuario.getId())).withSelfRel());
                    return udto;
                })
                .collect(Collectors.toSet());

        dto.setUsuarios(usuariosDTO);
        return dto;
    }

    public Empresa converterParaEntidade(EmpresaDTO dto) {
        Empresa entidade = new Empresa();
        entidade.setRazaoSocial(dto.getRazaoSocial());
        entidade.setNomeFantasia(dto.getNomeFantasia());
        entidade.setCadastro(dto.getCadastro());
        entidade.setEndereco(dto.getEndereco());
        entidade.setTelefones(dto.getTelefones());

        return entidade;
    }

}
