package com.autobots.automanager.DTO;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import java.util.Date;

@Data
public class MercadoriaDTO extends RepresentationModel<MercadoriaDTO> {
    private Long id;
    private Date cadastro;
    private Date fabricao;
    private Date validade;
    private String nome;
    private long quantidade;
    private double valor;
    private String descricao;
}
