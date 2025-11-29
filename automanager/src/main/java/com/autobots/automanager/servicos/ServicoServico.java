package com.autobots.automanager.servicos;

import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.repositorios.RepositorioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicoServico {

    @Autowired
    private RepositorioServico repositorioServico;

    public Servico salvar(Servico servico) {
        return repositorioServico.save(servico);
    }

    public Optional<Servico> buscarPorId(Long id) {
        return repositorioServico.findById(id);
    }

    public List<Servico> buscarTodos() {
        return repositorioServico.findAll();
    }

    public Servico atualizar(Long id, Servico dadosServico) {
        return repositorioServico.findById(id).map(servicoExistente -> {
            // 2. Atualiza os campos
            servicoExistente.setNome(dadosServico.getNome());
            servicoExistente.setValor(dadosServico.getValor());
            servicoExistente.setDescricao(dadosServico.getDescricao());

            return repositorioServico.save(servicoExistente);
        }).orElseThrow(() -> new RuntimeException("Serviço não encontrado com ID: " + id));
    }

    public void deletar(Long id) {
        if (!repositorioServico.existsById(id)) {
            throw new RuntimeException("Serviço não encontrado com ID: " + id);
        }
        repositorioServico.deleteById(id);
    }
}
