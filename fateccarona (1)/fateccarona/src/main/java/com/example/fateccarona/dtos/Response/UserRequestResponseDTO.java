package com.example.fateccarona.dtos.Response;

import java.time.LocalDateTime;

public record UserRequestResponseDTO(
		Integer idSolicitacao,
		Integer idCarona,
		Integer idPassageiro,
		LocalDateTime dataHora,
		String origem,
		Double longitudeOrigem,
		Double latitudeOrigem,
		String destino,
		Double longitudeDestino,
		Double latitudeDestino,
		Integer idStatus		
) {}
