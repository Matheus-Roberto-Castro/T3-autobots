package com.autobots.automanager.conversores;

import com.autobots.automanager.DTO.ServicoDTO;
import com.autobots.automanager.entitades.Servico;
import org.springframework.stereotype.Component;

@Component
public class ServicoConversor {

    public ServicoDTO converterParaDto(Servico entidade) {
        ServicoDTO dto = new ServicoDTO();
        dto.setId(entidade.getId());
        dto.setNome(entidade.getNome());
        dto.setValor(entidade.getValor());
        dto.setDescricao(entidade.getDescricao());
        return dto;
    }

    public Servico converterParaEntidade(ServicoDTO dto) {
        Servico entidade = new Servico();
        entidade.setNome(dto.getNome());
        entidade.setValor(dto.getValor());
        entidade.setDescricao(dto.getDescricao());
        return entidade;
    }
}
