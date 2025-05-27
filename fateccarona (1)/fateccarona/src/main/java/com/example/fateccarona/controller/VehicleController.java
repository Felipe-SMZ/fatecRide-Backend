package com.example.fateccarona.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.fateccarona.dtos.VehicleDTO;
import com.example.fateccarona.models.User;
import com.example.fateccarona.models.Vehicle;
import com.example.fateccarona.repository.UserRepository;
import com.example.fateccarona.repository.VehicleRepository;

@RestController
@RequestMapping("/api/veiculos")
public class VehicleController {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<VehicleDTO> getVehicleByUserId(@PathVariable Integer idUsuario) {
        return vehicleRepository.findByUsuarioIdUsuario(idUsuario)
            .map(veiculo -> ResponseEntity.ok(new VehicleDTO(
                veiculo.getModelo(),
                veiculo.getMarca(),
                veiculo.getPlaca(),
                veiculo.getCor(),
                veiculo.getAno()
            )))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<String> saveOrUpdateVehicle(
            @PathVariable Integer idUsuario,
            @RequestBody VehicleDTO dto) {

        User user = userRepository.findById(idUsuario).orElse(null);
        if (user == null) return ResponseEntity.badRequest().body("Usuário não encontrado");

        Vehicle veiculo = vehicleRepository.findByUsuarioIdUsuario(idUsuario).orElse(new Vehicle());
        veiculo.setUsuario(user);
        veiculo.setModelo(dto.modelo());
        veiculo.setMarca(dto.marca());
        veiculo.setPlaca(dto.placa());
        veiculo.setCor(dto.cor());
        veiculo.setAno(dto.ano());

        vehicleRepository.save(veiculo);

        return ResponseEntity.ok("Informações do veículo salvas com sucesso!");
    }
}
