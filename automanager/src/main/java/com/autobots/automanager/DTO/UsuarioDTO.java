package com.autobots.automanager.DTO;

import com.autobots.automanager.entitades.Credencial;
import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import java.util.List;
import java.util.Set;

@Data
public class UsuarioDTO extends RepresentationModel<UsuarioDTO> {
    private Long id;
    private String nome;
    private String nomeSocial;

    private Endereco endereco;

    private Set<Telefone> telefones;
    private Set<Email> emails;
    private Set<Documento> documentos;
    private Set<Credencial> credenciais;

    private Set<PerfilUsuario> perfis;
    private List<Long> veiculosIds;
    private List<Long> vendasIds;
    private List<Long> mercadoriasFornecidasIds;
}