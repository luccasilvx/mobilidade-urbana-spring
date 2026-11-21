package com.edu.web.services;

import com.edu.web.entities.Linha;
import com.edu.web.entities.Modal;
import com.edu.web.repositories.LinhaRepository;
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
class LinhaServiceTest {

    @Mock
    private LinhaRepository repository;

    @InjectMocks
    private LinhaService service;

    @Test
    void deveSalvarLinhaComExtensaoValida() {
        Linha linha = new Linha();
        linha.setCodigo("L02001");
        linha.setNome("Eixo Norte");
        linha.setExtensaoKm(15.5);
        linha.setEstaAtiva(true);
        linha.setModal(Modal.ONIBUS);

        when(repository.save(any(Linha.class))).thenReturn(linha);

        Linha salva = service.salvar(linha);

        assertNotNull(salva);
        assertEquals("L02001", salva.getCodigo());
        verify(repository, times(1)).save(linha);
    }

    @Test
    void deveAceitarCasoLimiteExtensaoMinimaPositiva() {
        Linha linha = new Linha();
        linha.setExtensaoKm(0.01);

        when(repository.save(any(Linha.class))).thenReturn(linha);

        Linha salva = service.salvar(linha);
        assertEquals(0.01, salva.getExtensaoKm());
    }

    @Test
    void deveLancarErroQuandoExtensaoForZeroOuNegativa() {
        Linha linhaZero = new Linha();
        linhaZero.setExtensaoKm(0.0);

        Linha linhaNegativa = new Linha();
        linhaNegativa.setExtensaoKm(-5.0);

        assertThrows(IllegalArgumentException.class, () -> service.salvar(linhaZero));
        assertThrows(IllegalArgumentException.class, () -> service.salvar(linhaNegativa));
        verify(repository, never()).save(any());
    }

    @Test
    void deveLancarErroQuandoExtensaoForNula() {
        Linha linha = new Linha();
        linha.setExtensaoKm(null);

        assertThrows(IllegalArgumentException.class, () -> service.salvar(linha));
    }

    @Test
    void deveLancarErroAoBuscarLinhaInexistente() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(id));
    }
}
