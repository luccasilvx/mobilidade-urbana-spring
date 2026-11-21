package com.edu.web.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Integer capacidade;
    private Boolean estaAtivo;

    @Enumerated(EnumType.STRING)
    private Modal modal;

    public Veiculo() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Integer getCapacidade() { return capacidade; }
    public void setCapacidade(Integer capacidade) { this.capacidade = capacidade; }
    public Boolean getEstaAtivo() { return estaAtivo; }
    public void setEstaAtivo(Boolean estaAtivo) { this.estaAtivo = estaAtivo; }
    public Modal getModal() { return modal; }
    public void setModal(Modal modal) { this.modal = modal; }
}