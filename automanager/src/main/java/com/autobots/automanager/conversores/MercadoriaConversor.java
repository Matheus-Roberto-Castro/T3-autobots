package com.autobots.automanager.conversores;

import com.autobots.automanager.DTO.MercadoriaDTO;
import com.autobots.automanager.entitades.Mercadoria;
import org.springframework.stereotype.Component;

@Component
public class MercadoriaConversor {

    public MercadoriaDTO converterParaDto(Mercadoria entidade) {
        MercadoriaDTO dto = new MercadoriaDTO();
        dto.setId(entidade.getId());
        dto.setCadastro(entidade.getCadastro());
        dto.setFabricao(entidade.getFabricao());
        dto.setValidade(entidade.getValidade());
        dto.setNome(entidade.getNome());
        dto.setQuantidade(entidade.getQuantidade()); // Agora aceita long
        dto.setValor(entidade.getValor());
        dto.setDescricao(entidade.getDescricao());
        return dto;
    }

    public Mercadoria converterParaEntidade(MercadoriaDTO dto) {
        Mercadoria entidade = new Mercadoria();
        entidade.setCadastro(dto.getCadastro());
        entidade.setFabricao(dto.getFabricao());
        entidade.setValidade(dto.getValidade());
        entidade.setNome(dto.getNome());
        entidade.setQuantidade(dto.getQuantidade()); // Agora aceita long
        entidade.setValor(dto.getValor());
        entidade.setDescricao(dto.getDescricao());
        return entidade;
    }
}
