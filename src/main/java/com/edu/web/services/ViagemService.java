package com.edu.web.services;

import com.edu.web.entities.StatusViagem;
import com.edu.web.entities.Viagem;
import com.edu.web.repositories.ViagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ViagemService {

    @Autowired
    private ViagemRepository repository;

    public List<Viagem> listarTodas() {
        return repository.findAll();
    }

    public Viagem buscarPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Viagem não encontrada: " + id));
    }

    public Viagem salvar(Viagem viagem) {
        if (viagem.getVeiculo() == null || !Boolean.TRUE.equals(viagem.getVeiculo().getEstaAtivo())) {
            throw new IllegalStateException("Não é possível agendar ou iniciar viagem com veículo inativo.");
        }
        if (viagem.getStatus() == null) {
            viagem.setStatus(StatusViagem.AGENDADA);
        }
        return repository.save(viagem);
    }

    public Viagem atualizarStatus(UUID id, StatusViagem novoStatus) {
        Viagem viagem = buscarPorId(id);
        StatusViagem atual = viagem.getStatus();

        if (atual == StatusViagem.CANCELADA) {
            throw new IllegalStateException("Viagem cancelada não pode mudar de status.");
        }
        if (atual == StatusViagem.CONCLUIDA) {
            throw new IllegalStateException("Viagem já concluída não pode ser alterada.");
        }
        if (atual == StatusViagem.AGENDADA && novoStatus == StatusViagem.CONCLUIDA) {
            throw new IllegalStateException("Uma viagem agendada precisa entrar EM_CURSO antes de ser concluída.");
        }

        viagem.setStatus(novoStatus);
        return repository.save(viagem);
    }
}
