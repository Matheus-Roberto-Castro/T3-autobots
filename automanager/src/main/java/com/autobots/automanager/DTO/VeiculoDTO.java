package com.autobots.automanager.DTO;

import com.autobots.automanager.enumeracoes.TipoVeiculo;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class VeiculoDTO extends RepresentationModel<VeiculoDTO> {
    private Long id;
    private String placa;
    private String modelo;
    private TipoVeiculo tipo;
    private Long proprietarioId;
}