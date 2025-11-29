package com.autobots.automanager.servicos;

import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.repositorios.RepositorioMercadoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MercadoriaServico {

    @Autowired
    private RepositorioMercadoria repositorioMercadoria;

    public Mercadoria salvar(Mercadoria mercadoria) {
        return repositorioMercadoria.save(mercadoria);
    }

    public Optional<Mercadoria> buscarPorId(Long id) {
        return repositorioMercadoria.findById(id);
    }

    public List<Mercadoria> buscarTodos() {
        return repositorioMercadoria.findAll();
    }

    public Mercadoria atualizar(Long id, Mercadoria dadosMercadoria) {
        return repositorioMercadoria.findById(id).map(mercadoriaExistente -> {

            mercadoriaExistente.setNome(dadosMercadoria.getNome());
            mercadoriaExistente.setQuantidade(dadosMercadoria.getQuantidade());
            mercadoriaExistente.setValor(dadosMercadoria.getValor());
            mercadoriaExistente.setDescricao(dadosMercadoria.getDescricao());
            mercadoriaExistente.setFabricao(dadosMercadoria.getFabricao());
            mercadoriaExistente.setValidade(dadosMercadoria.getValidade());

            return repositorioMercadoria.save(mercadoriaExistente);
        }).orElseThrow(() -> new RuntimeException("Mercadoria não encontrada com ID: " + id));
    }

    public void deletar(Long id) {
        if (!repositorioMercadoria.existsById(id)) {
            throw new RuntimeException("Mercadoria não encontrada com ID: " + id);
        }
        repositorioMercadoria.deleteById(id);
    }
}
