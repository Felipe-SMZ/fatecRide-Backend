package com.example.fateccarona.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

	@Entity
	@Table(name = "tipoUsuario") 
	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public class UserType {

		  @Id
		    @GeneratedValue(strategy = GenerationType.IDENTITY)
		    @Column(name = "id_tipo_usuario")
		    private Integer idTipoUsuario;

		    @Column(name = "tipo", nullable = false, unique = true)
		    private String tipo;
}
