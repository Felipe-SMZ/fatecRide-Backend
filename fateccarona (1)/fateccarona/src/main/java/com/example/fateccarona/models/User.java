package com.example.fateccarona.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    private String nome;
    private String sobrenome;
    private String email;

    @JsonIgnore // Evita retornar a senha nas respostas da API
    private String senha;

    private String telefone;
    private String foto;

    @ManyToOne
    @JoinColumn(name = "id_tipo_usuario")
    private UserType tipoUsuario;

    @ManyToOne
    @JoinColumn(name = "id_genero")
    private Gender genero;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Vehicle veiculo;

    public void setVeiculo(Vehicle veiculo) {
        this.veiculo = veiculo;
        if (veiculo != null && veiculo.getUsuario() != this) {
            veiculo.setUsuario(this);
        }
    }


    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Endereco endereco;

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
        if (endereco != null && endereco.getUsuario() != this) {
            endereco.setUsuario(this);
        }
    }

    // Compatibilidade com bibliotecas externas
    public Integer getId() {
        return this.idUsuario;
    }

    public void setId(Integer id) {
        this.idUsuario = id;
    }
}
