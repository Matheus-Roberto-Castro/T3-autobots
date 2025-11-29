package com.autobots.automanager.servicos;

import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioVenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VendaServico {

    @Autowired
    private RepositorioVenda repositorioVenda;

    public Venda salvar(Venda venda) {

        if (venda.getCadastro() == null) {
            venda.setCadastro(new Date());
        }

        return repositorioVenda.save(venda);
    }

    public Optional<Venda> buscarPorId(Long id) {
        return repositorioVenda.findById(id);
    }

    public List<Venda> buscarTodos() {
        return repositorioVenda.findAll();
    }

    public Venda atualizar(Long id, Venda dadosVenda) {
        return repositorioVenda.findById(id).map(vendaExistente -> {

            vendaExistente.setIdentificacao(dadosVenda.getIdentificacao());
            vendaExistente.setCadastro(dadosVenda.getCadastro());
            vendaExistente.setCliente(dadosVenda.getCliente());
            vendaExistente.setFuncionario(dadosVenda.getFuncionario());
            vendaExistente.setVeiculo(dadosVenda.getVeiculo());
            vendaExistente.setMercadorias(dadosVenda.getMercadorias());
            vendaExistente.setServicos(dadosVenda.getServicos());

            return repositorioVenda.save(vendaExistente);

        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venda não encontrada com ID: " + id));
    }

    public void deletar(Long id) {
        if (!repositorioVenda.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Venda não encontrada com ID: " + id);
        }
        repositorioVenda.deleteById(id);
    }
}