package com.autobots.automanager.DTO;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class ServicoDTO extends RepresentationModel<ServicoDTO> {
    private Long id;
    private String nome;
    private double valor;
    private String descricao;
}