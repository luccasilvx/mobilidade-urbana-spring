package com.edu.web.entities;

import jakarta.persistence.*;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Entity
public class EstacaoPonto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nome;
    private Boolean estaAcessivel;
    private Boolean integraBicicletario;

    @ManyToMany(mappedBy = "estacoes")
    private List<Linha> linhas = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getEstaAcessivel() {
        return estaAcessivel;
    }

    public void setEstaAcessivel(Boolean estaAcessivel) {
        this.estaAcessivel = estaAcessivel;
    }

    public Boolean getIntegraBicicletario() {
        return integraBicicletario;
    }

    public void setIntegraBicicletario(Boolean integraBicicletario) {
        this.integraBicicletario = integraBicicletario;
    }

    public List<Linha> getLinhas() {
        return linhas;
    }

    public void setLinhas(List<Linha> linhas) {
        this.linhas = linhas;
    }
}