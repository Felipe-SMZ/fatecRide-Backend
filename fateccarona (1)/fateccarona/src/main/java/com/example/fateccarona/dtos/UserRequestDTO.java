package com.example.fateccarona.dtos;

import java.time.LocalDateTime;

public record UserRequestDTO (
	
	Integer idCarona,
	Integer idPassageiro,
	LocalDateTime dataHora,
	  String origem,
	  String destino,		
	  Integer vagasDisponiveis,
	  Integer idStatus
) {}
