package com.example.fateccarona.dtos;

import java.sql.Date;

public record VehicleDTO(
	    String modelo,
	    String marca,
	    String placa,
	    String cor,
	    Integer ano
) {
}
