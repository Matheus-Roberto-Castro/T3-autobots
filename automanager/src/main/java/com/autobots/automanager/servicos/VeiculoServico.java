package com.autobots.automanager.servicos;

import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class VeiculoServico {

    @Autowired
    private RepositorioVeiculo repositorioVeiculo;

    public Veiculo salvar(Veiculo veiculo) {
        return repositorioVeiculo.save(veiculo);
    }

    public Optional<Veiculo> buscarPorId(Long id) {
        return repositorioVeiculo.findById(id);
    }

    public List<Veiculo> buscarTodos() {
        return repositorioVeiculo.findAll();
    }

    public Veiculo atualizar(Long id, Veiculo dadosVeiculo) {
        return repositorioVeiculo.findById(id).map(veiculoExistente -> {

            veiculoExistente.setModelo(dadosVeiculo.getModelo());
            veiculoExistente.setPlaca(dadosVeiculo.getPlaca());
            veiculoExistente.setTipo(dadosVeiculo.getTipo());

            if (dadosVeiculo.getProprietario() != null) {
                veiculoExistente.setProprietario(dadosVeiculo.getProprietario());
            }

            return repositorioVeiculo.save(veiculoExistente);

        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado com ID: " + id));
    }

    public void deletar(Long id) {
        if (!repositorioVeiculo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado com ID: " + id);
        }
        repositorioVeiculo.deleteById(id);
    }
}