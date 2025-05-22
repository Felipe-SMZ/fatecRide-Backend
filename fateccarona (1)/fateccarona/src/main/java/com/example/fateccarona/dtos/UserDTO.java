package com.example.fateccarona.dtos;

import com.example.fateccarona.models.Gender;
import com.example.fateccarona.models.UserType;

public record UserDTO(
		   String nome,
		    String sobrenome,
		    String email,
		    String senha,
		    String telefone,
		    String foto,
		    Integer idTipoUsuario,
		    Integer idGenero,
		    VehicleDTO vehicle
) {}
