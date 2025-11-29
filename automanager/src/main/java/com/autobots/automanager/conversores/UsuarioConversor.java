package com.autobots.automanager.conversores;

import com.autobots.automanager.DTO.UsuarioDTO;
import com.autobots.automanager.entitades.*;
import com.autobots.automanager.repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class UsuarioConversor {

    @Autowired private RepositorioVeiculo repositorioVeiculo;
    @Autowired private RepositorioVenda repositorioVenda;
    @Autowired private RepositorioMercadoria repositorioMercadoria;

    public UsuarioDTO converterParaDto(Usuario entidade) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(entidade.getId());
        dto.setNome(entidade.getNome());
        dto.setNomeSocial(entidade.getNomeSocial());
        dto.setEndereco(entidade.getEndereco());

        dto.setTelefones(new HashSet<>(entidade.getTelefones() != null ? entidade.getTelefones() : Collections.emptySet()));
        dto.setEmails(new HashSet<>(entidade.getEmails() != null ? entidade.getEmails() : Collections.emptySet()));
        dto.setDocumentos(new HashSet<>(entidade.getDocumentos() != null ? entidade.getDocumentos() : Collections.emptySet()));
        dto.setCredenciais(new HashSet<>(entidade.getCredenciais() != null ? entidade.getCredenciais() : Collections.emptySet()));

        dto.setPerfis(entidade.getPerfis() != null ? entidade.getPerfis() : Collections.emptySet());

        dto.setVeiculosIds(entidade.getVeiculos() != null ?
                entidade.getVeiculos().stream().map(Veiculo::getId).collect(Collectors.toList()) :
                Collections.emptyList());

        dto.setVendasIds(entidade.getVendas() != null ?
                entidade.getVendas().stream().map(Venda::getId).collect(Collectors.toList()) :
                Collections.emptyList());

        dto.setMercadoriasFornecidasIds(entidade.getMercadorias() != null ?
                entidade.getMercadorias().stream().map(Mercadoria::getId).collect(Collectors.toList()) :
                Collections.emptyList());

        return dto;
    }

    public Usuario converterParaEntidade(UsuarioDTO dto) {
        Usuario entidade = new Usuario();

        entidade.setNome(dto.getNome());
        entidade.setNomeSocial(dto.getNomeSocial());
        entidade.setEndereco(dto.getEndereco());
        entidade.setTelefones(dto.getTelefones());
        entidade.setEmails(dto.getEmails());
        entidade.setDocumentos(dto.getDocumentos());
        entidade.setCredenciais(dto.getCredenciais());

        entidade.setPerfis(dto.getPerfis());

        return entidade;
    }
}
