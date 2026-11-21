package com.edu.web.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Linha {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String codigo;
    private String nome;
    private Double extensaoKm;
    private Boolean estaAtiva;

    @Enumerated(EnumType.STRING)
    private Modal modal;

    @ManyToOne
    @JoinColumn(name = "cidade_id")
    private Cidade cidade;

    @ManyToMany
    @JoinTable(
            name = "linha_ponto",
            joinColumns = @JoinColumn(name = "linha_id"),
            inverseJoinColumns = @JoinColumn(name = "ponto_id")
    )
    private List<EstacaoPonto> estacoes = new ArrayList<>();

    public Linha() {}

    // Getters e Setters (pode gerar no IntelliJ com Alt+Insert)
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Double getExtensaoKm() { return extensaoKm; }
    public void setExtensaoKm(Double extensaoKm) { this.extensaoKm = extensaoKm; }
    public Boolean getEstaAtiva() { return estaAtiva; }
    public void setEstaAtiva(Boolean estaAtiva) { this.estaAtiva = estaAtiva; }
    public Modal getModal() { return modal; }
    public void setModal(Modal modal) { this.modal = modal; }
    public Cidade getCidade() { return cidade; }
    public void setCidade(Cidade cidade) { this.cidade = cidade; }
}