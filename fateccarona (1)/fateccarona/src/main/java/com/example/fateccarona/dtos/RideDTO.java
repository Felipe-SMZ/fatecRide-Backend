package com.example.fateccarona.dtos;

import java.time.LocalDateTime;

public record RideDTO (

		 Integer idMotorista,
	     String origem,
	     String destino,
	     Double latitudeOrigem,
	     Double longitudeOrigem,
	     Double latitudeDestino,
	     Double longitudeDestino,
	     LocalDateTime dataHora,
	     Integer vagasDisponiveis,
	     Integer idStatus
){}
