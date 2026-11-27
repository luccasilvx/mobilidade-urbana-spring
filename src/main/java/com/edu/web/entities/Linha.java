package com.edu.web.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "linha", indexes = {
        @Index(name = "idx_linha_codigo", columnList = "codigo", unique = true),
        @Index(name = "idx_linha_cidade", columnList = "cidade_id")
})
public class Linha {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O código da linha é obrigatório.")
    @Size(max = 20, message = "O código deve ter no máximo 20 caracteres.")
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @NotBlank(message = "O nome da linha é obrigatório.")
    @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres.")
    @Column(nullable = false, length = 120)
    private String nome;

    @NotNull(message = "A extensão em km é obrigatória.")
    @Positive(message = "A extensão da linha deve ser maior que zero.")
    @Column(name = "extensao_km", nullable = false)
    private Double extensaoKm;

    @Column(name = "esta_ativa", nullable = false)
    private Boolean estaAtiva = true;

    @NotNull(message = "O modal é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Modal modal;

    @NotNull(message = "A cidade é obrigatória.")
    @ManyToOne
    @JoinColumn(name = "cidade_id", nullable = false)
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
    public List<EstacaoPonto> getEstacoes() { return estacoes; }
    public void setEstacoes(List<EstacaoPonto> estacoes) { this.estacoes = estacoes; }
}