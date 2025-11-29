package com.autobots.automanager.servicos;

import com.autobots.automanager.entitades.Credencial;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServico {

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        return repositorioUsuario.save(usuario);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return repositorioUsuario.findById(id);
    }

    public List<Usuario> buscarTodos() {
        return repositorioUsuario.findAll();
    }

    @Transactional
    public Usuario atualizar(Long id, Usuario dadosUsuario) {
        return repositorioUsuario.findById(id).map(usuarioExistente -> {

            usuarioExistente.setNome(dadosUsuario.getNome());
            usuarioExistente.setNomeSocial(dadosUsuario.getNomeSocial());
            usuarioExistente.setPerfis(dadosUsuario.getPerfis());
            return repositorioUsuario.save(usuarioExistente);
        }).orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
    }

    public void deletar(Long id) {
        if (!repositorioUsuario.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
        repositorioUsuario.deleteById(id);
    }

    public Usuario adicionarCredencial(Long idUsuario, Credencial novaCredencial) {
        Usuario usuario = repositorioUsuario.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        if (novaCredencial == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credencial inválida.");
        }
        usuario.getCredenciais().add(novaCredencial);

        return repositorioUsuario.save(usuario);
    }

    public void removerCredencial(Long idUsuario, Long idCredencial) {
        Usuario usuario = repositorioUsuario.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        boolean removido = usuario.getCredenciais().removeIf(
                credencial -> credencial.getId() != null && credencial.getId().equals(idCredencial)
        );

        if (!removido) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Credencial não encontrada para este usuário.");
        }
        repositorioUsuario.save(usuario);
    }
}