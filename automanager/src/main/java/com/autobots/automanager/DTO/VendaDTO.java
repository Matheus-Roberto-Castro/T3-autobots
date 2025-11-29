package com.autobots.automanager.DTO;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import java.util.Date;
import java.util.List;

@Data
public class VendaDTO extends RepresentationModel<VendaDTO> {
    private Long id;
    private String identificacao;
    private Date cadastro;
    private Long clienteId;
    private Long funcionarioId;
    private Long veiculoId;
    private List<Long> mercadoriasIds;
    private List<Long> servicosIds;
}