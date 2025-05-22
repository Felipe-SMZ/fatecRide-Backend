package com.example.fateccarona.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="veiculos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id_veiculo")
	    private Integer idVeiculo;

	    @OneToOne
	    @JoinColumn(name = "id_usuario", nullable = false)  // Esta é a chave estrangeira
	    @JsonBackReference
	    private User usuario; // Relacionamento com o usuário (um veículo pertence a um usuário)

	    private String modelo;
	    private String marca;
	    private String placa;
	    private String cor;
	    private Integer ano;
}
