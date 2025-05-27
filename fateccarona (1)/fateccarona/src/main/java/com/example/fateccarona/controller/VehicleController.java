package com.example.fateccarona.controller;

import com.example.fateccarona.dtos.VehicleDTO;
import com.example.fateccarona.models.Vehicle;
import com.example.fateccarona.service.VehicleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/veiculos")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/{idUsuario}")
    public ResponseEntity<Vehicle> salvarVeiculo(@PathVariable Integer idUsuario, @RequestBody VehicleDTO vehicleDTO) {
        try {
            Vehicle veiculoSalvo = vehicleService.salvarVeiculo(idUsuario, vehicleDTO);
            return ResponseEntity.ok(veiculoSalvo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
