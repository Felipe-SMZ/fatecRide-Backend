package com.example.fateccarona.models;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;

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
@Table(name = "caronas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ride {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	@Column(name = "id_carona")
	Integer  idCarona;
	
	@ManyToOne	
	@JoinColumn(name = "id_motorista")
	User idMotorista;
	
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
	
	@Column(name="data_hora")
	LocalDateTime dataHora;
	
	@Column(name="vagas_disponiveis")
	
	Integer vagasDisponiveis;
	
	@ManyToOne
	@JoinColumn(name="id_status")
	RideStatus idStatus;
}
