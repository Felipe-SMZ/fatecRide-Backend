package com.example.fateccarona.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fateccarona.models.Vehicle;

public interface VehicleRepositore extends JpaRepository<Vehicle, Integer>{

	  Optional<Vehicle> findByPlaca(String placa);
	  Optional<Vehicle> findByUsuarioIdUsuario(Integer idUsuario);



}
