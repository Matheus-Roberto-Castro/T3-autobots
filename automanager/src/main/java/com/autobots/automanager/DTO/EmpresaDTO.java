package com.autobots.automanager.DTO;

import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.entitades.Telefone;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class EmpresaDTO extends RepresentationModel<EmpresaDTO> {
    private Long id;
    private String razaoSocial;
    private String nomeFantasia;
    private Date cadastro;
    private Endereco endereco;
    private Set<Telefone> telefones;
    private Set<UsuarioDTO> usuarios;
}
