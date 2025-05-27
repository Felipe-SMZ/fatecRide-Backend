package com.example.fateccarona.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "enderecos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", unique = true)
    @JsonBackReference
    private User usuario;

    // Método auxiliar para garantir integridade bidirecional (opcional)
    public void setUsuario(User usuario) {
        this.usuario = usuario;
        if (usuario != null && usuario.getEndereco() != this) {
            usuario.setEndereco(this);
        }
    }
}
