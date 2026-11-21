package com.edu.web.services;

import com.edu.web.entities.Linha;
import com.edu.web.repositories.LinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class LinhaService {

    @Autowired
    private LinhaRepository repository;

    public List<Linha> listarTodas() {
        return repository.findAll();
    }

    public Linha buscarPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Linha não encontrada: " + id));
    }

    public Linha salvar(Linha linha) {
        if (linha.getExtensaoKm() == null || linha.getExtensaoKm() <= 0) {
            throw new IllegalArgumentException("A extensão da linha deve ser maior que zero.");
        }
        return repository.save(linha);
    }

    public void excluir(UUID id) {
        repository.deleteById(id);
    }
}