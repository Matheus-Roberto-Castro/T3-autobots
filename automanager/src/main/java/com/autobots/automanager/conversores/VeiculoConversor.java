package com.autobots.automanager.conversores;

import com.autobots.automanager.DTO.VeiculoDTO;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class VeiculoConversor {

    @Autowired private RepositorioUsuario repositorioUsuario;

    public VeiculoDTO converterParaDto(Veiculo entidade) {
        VeiculoDTO dto = new VeiculoDTO();
        dto.setId(entidade.getId());
        dto.setPlaca(entidade.getPlaca());
        dto.setModelo(entidade.getModelo());
        dto.setTipo(entidade.getTipo());

        if (entidade.getProprietario() != null) {
            dto.setProprietarioId(entidade.getProprietario().getId());
        }
        return dto;
    }

    public Veiculo converterParaEntidade(VeiculoDTO dto) {
        Veiculo entidade = new Veiculo();
        entidade.setPlaca(dto.getPlaca());
        entidade.setModelo(dto.getModelo());
        entidade.setTipo(dto.getTipo());

        if (dto.getProprietarioId() != null) {
            Usuario proprietario = repositorioUsuario.findById(dto.getProprietarioId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Proprietário não encontrado."));
            entidade.setProprietario(proprietario);
        }
        return entidade;
    }
}
