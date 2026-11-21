package com.edu.web.services;

import com.edu.web.entities.StatusViagem;
import com.edu.web.entities.Veiculo;
import com.edu.web.entities.Viagem;
import com.edu.web.repositories.ViagemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViagemServiceTest {

    @Mock
    private ViagemRepository repository;

    @InjectMocks
    private ViagemService service;

    @Test
    void devePermitirTransicaoDeAgendadaParaEmCurso() {
        UUID id = UUID.randomUUID();
        Viagem viagem = new Viagem();
        viagem.setId(id);
        viagem.setStatus(StatusViagem.AGENDADA);

        when(repository.findById(id)).thenReturn(Optional.of(viagem));
        when(repository.save(any(Viagem.class))).thenAnswer(inv -> inv.getArgument(0));

        Viagem atualizada = service.atualizarStatus(id, StatusViagem.EM_CURSO);

        assertEquals(StatusViagem.EM_CURSO, atualizada.getStatus());
    }

    @Test
    void deveBloquearTransicaoDeCanceladaParaConcluida() {
        UUID id = UUID.randomUUID();
        Viagem viagem = new Viagem();
        viagem.setId(id);
        viagem.setStatus(StatusViagem.CANCELADA);

        when(repository.findById(id)).thenReturn(Optional.of(viagem));

        assertThrows(IllegalStateException.class, () ->
                service.atualizarStatus(id, StatusViagem.CONCLUIDA));
        verify(repository, never()).save(any());
    }

    @Test
    void deveBloquearPuloDeAgendadaDiretoParaConcluida() {
        UUID id = UUID.randomUUID();
        Viagem viagem = new Viagem();
        viagem.setId(id);
        viagem.setStatus(StatusViagem.AGENDADA);

        when(repository.findById(id)).thenReturn(Optional.of(viagem));

        assertThrows(IllegalStateException.class, () ->
                service.atualizarStatus(id, StatusViagem.CONCLUIDA));
    }

    @Test
    void deveBloquearViagemComVeiculoInativo() {
        Veiculo veiculo = new Veiculo();
        veiculo.setEstaAtivo(false);

        Viagem viagem = new Viagem();
        viagem.setVeiculo(veiculo);

        assertThrows(IllegalStateException.class, () -> service.salvar(viagem));
        verify(repository, never()).save(any());
    }
}
