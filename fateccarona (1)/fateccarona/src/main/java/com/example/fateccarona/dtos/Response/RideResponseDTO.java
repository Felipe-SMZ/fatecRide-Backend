package com.example.fateccarona.dtos.Response;

import java.time.LocalDateTime;

public record RideResponseDTO(
		Integer idCarona,

		Integer idMotorista,
		
		String origem,


		Double longitudeOrigem,
		
		
		Double latitudeOrigem,
		
		
		String destino,
		
	
		Double longitudeDestino,
		
		

		Double latitudeDestino,
		
		
		LocalDateTime dataHora,
		

		
		Integer vagasDisponiveis,
		

		Integer idStatus
	
) {}