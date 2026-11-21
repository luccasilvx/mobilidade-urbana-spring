package com.edu.web.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Ocorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getResolvida() {
        return resolvida;
    }

    public void setResolvida(Boolean resolvida) {
        this.resolvida = resolvida;
    }

    public TipoOcorrencia getTipo() {
        return tipo;
    }

    public void setTipo(TipoOcorrencia tipo) {
        this.tipo = tipo;
    }

    public GravidadeOcorrencia getGravidade() {
        return gravidade;
    }

    public void setGravidade(GravidadeOcorrencia gravidade) {
        this.gravidade = gravidade;
    }

    public Viagem getViagem() {
        return viagem;
    }

    public void setViagem(Viagem viagem) {
        this.viagem = viagem;
    }

    private Boolean resolvida;

    @Enumerated(EnumType.STRING)
    private TipoOcorrencia tipo;

    @Enumerated(EnumType.STRING)
    private GravidadeOcorrencia gravidade;

    @ManyToOne
    @JoinColumn(name = "viagem_id")
    private Viagem viagem;
}