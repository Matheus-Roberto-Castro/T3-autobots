package com.autobots.automanager.conversores;

import com.autobots.automanager.DTO.VendaDTO;
import com.autobots.automanager.entitades.*;
import com.autobots.automanager.repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.List;

@Component
public class VendaConversor {

    @Autowired private RepositorioUsuario repositorioUsuario;
    @Autowired private RepositorioVeiculo repositorioVeiculo;
    @Autowired private RepositorioMercadoria repositorioMercadoria;
    @Autowired private RepositorioServico repositorioServico;

    public VendaDTO converterParaDto(Venda entidade) {
        VendaDTO dto = new VendaDTO();
        dto.setId(entidade.getId());
        dto.setIdentificacao(entidade.getIdentificacao());
        dto.setCadastro(entidade.getCadastro());

        if (entidade.getCliente() != null) {
            dto.setClienteId(entidade.getCliente().getId());
        }
        if (entidade.getFuncionario() != null) {
            dto.setFuncionarioId(entidade.getFuncionario().getId());
        }
        if (entidade.getVeiculo() != null) {
            dto.setVeiculoId(entidade.getVeiculo().getId());
        }

        dto.setMercadoriasIds(entidade.getMercadorias().stream()
                .map(Mercadoria::getId)
                .collect(Collectors.toList()));

        dto.setServicosIds(entidade.getServicos().stream()
                .map(Servico::getId)
                .collect(Collectors.toList()));

        return dto;
    }

    public Venda converterParaEntidade(VendaDTO dto) {
        Venda entidade = new Venda();

        entidade.setIdentificacao(dto.getIdentificacao());
        entidade.setCadastro(dto.getCadastro());

        entidade.setMercadorias(new HashSet<>());
        entidade.setServicos(new HashSet<>());

        if (dto.getClienteId() != null) {
            Usuario cliente = repositorioUsuario.findById(dto.getClienteId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente não encontrado."));
            entidade.setCliente(cliente);
        }

        if (dto.getFuncionarioId() != null) {
            Usuario funcionario = repositorioUsuario.findById(dto.getFuncionarioId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Funcionário não encontrado."));
            entidade.setFuncionario(funcionario);
        }

        if (dto.getVeiculoId() != null) {
            Veiculo veiculo = repositorioVeiculo.findById(dto.getVeiculoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Veículo não encontrado."));
            entidade.setVeiculo(veiculo);
        }

        if (dto.getMercadoriasIds() != null) {
            List<Mercadoria> mercadoriasList = dto.getMercadoriasIds().stream()
                    .map(id -> repositorioMercadoria.findById(id)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mercadoria não encontrada com ID: " + id)))
                    .collect(Collectors.toList());

            entidade.setMercadorias(new HashSet<>(mercadoriasList));
        }

        if (dto.getServicosIds() != null) {
            List<Servico> servicosList = dto.getServicosIds().stream()
                    .map(id -> repositorioServico.findById(id)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Serviço não encontrado com ID: " + id)))
                    .collect(Collectors.toList());

            entidade.setServicos(new HashSet<>(servicosList));
        }

        return entidade;
    }
}