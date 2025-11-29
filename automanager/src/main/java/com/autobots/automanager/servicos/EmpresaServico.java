package com.autobots.automanager.servicos;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmpresaServico {

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    public Empresa salvar(Empresa empresa) {
        if (empresa.getCadastro() == null) {
            empresa.setCadastro(new Date());
        }

        return repositorioEmpresa.save(empresa);
    }

    public Optional<Empresa> buscarPorId(Long id) {
        return repositorioEmpresa.findById(id);
    }

    public List<Empresa> buscarTodos() {
        return repositorioEmpresa.findAll();
    }

    public Empresa atualizar(Long id, Empresa dadosEmpresa) {
        return repositorioEmpresa.findById(id).map(empresaExistente -> {

            if (dadosEmpresa.getRazaoSocial() != null)
                empresaExistente.setRazaoSocial(dadosEmpresa.getRazaoSocial());

            if (dadosEmpresa.getNomeFantasia() != null)
                empresaExistente.setNomeFantasia(dadosEmpresa.getNomeFantasia());

            if (dadosEmpresa.getEndereco() != null)
                empresaExistente.setEndereco(dadosEmpresa.getEndereco());

            if (dadosEmpresa.getTelefones() != null) {
                empresaExistente.getTelefones().clear();
                empresaExistente.getTelefones().addAll(dadosEmpresa.getTelefones());
            }

            if (dadosEmpresa.getUsuarios() != null) {
                empresaExistente.getUsuarios().clear();
                empresaExistente.getUsuarios().addAll(dadosEmpresa.getUsuarios());
            }

            if (dadosEmpresa.getMercadorias() != null) {
                empresaExistente.getMercadorias().clear();
                empresaExistente.getMercadorias().addAll(dadosEmpresa.getMercadorias());
            }

            if (dadosEmpresa.getServicos() != null) {
                empresaExistente.getServicos().clear();
                empresaExistente.getServicos().addAll(dadosEmpresa.getServicos());
            }

            if (dadosEmpresa.getVendas() != null) {
                empresaExistente.getVendas().clear();
                empresaExistente.getVendas().addAll(dadosEmpresa.getVendas());
            }

            return repositorioEmpresa.save(empresaExistente);

        }).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Empresa não encontrada com ID: " + id
        ));
    }

    public void deletar(Long id) {
        if (!repositorioEmpresa.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada com ID: " + id);
        }
        repositorioEmpresa.deleteById(id);
    }

    @Transactional
    public Empresa associarUsuario(Long idEmpresa, Long idUsuario) {

        Empresa empresa = repositorioEmpresa.findById(idEmpresa)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Empresa não encontrada."
                ));

        Usuario usuario = repositorioUsuario.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado."
                ));

        empresa.getUsuarios().add(usuario);

        return repositorioEmpresa.save(empresa);
    }

    @Transactional
    public Empresa desassociarUsuario(Long idEmpresa, Long idUsuario) {

        Empresa empresa = repositorioEmpresa.findById(idEmpresa)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Empresa não encontrada."
                ));

        Usuario usuario = repositorioUsuario.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado."
                ));

        if (!empresa.getUsuarios().contains(usuario)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O usuário não está associado a esta empresa."
            );
        }

        empresa.getUsuarios().remove(usuario);

        return repositorioEmpresa.save(empresa);
    }

}