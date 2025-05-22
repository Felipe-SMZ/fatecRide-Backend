package com.example.fateccarona.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "solicitacoes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequests {
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	@Column(name = "id_solicitacao")
	Integer  idSolicitacao;
	
	@ManyToOne	
	@JoinColumn(name = "id_carona")
	Ride idCarona;
	
	@ManyToOne	
	@JoinColumn(name = "id_passageiro ")
	User idPassageiro ;
	
	@Column(name="data_hora")
	LocalDateTime dataHora;
	
	String origem;

	@Column(name = "longitude_origem")
	Double longitudeOrigem;
	
	
	@Column(name = "latitude_origem")
	Double latitudeOrigem;
	
	
	String destino;
	
	@Column(name = "longitude_destino")
	Double longitudeDestino;
	
	
	@Column(name = "latitude_destino")
	Double latitudeDestino;
	

	
	@ManyToOne
	@JoinColumn(name="id_status")
	RequestStatus idStatus;
}
