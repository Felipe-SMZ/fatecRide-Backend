package com.example.fateccarona.service;

import com.example.fateccarona.dtos.VehicleDTO;
import com.example.fateccarona.models.User;
import com.example.fateccarona.models.Vehicle;
import com.example.fateccarona.repository.UserRepository;
import com.example.fateccarona.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    public Vehicle salvarVeiculo(Integer idUsuario, VehicleDTO dto) {
        User usuario = userRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Vehicle veiculo = new Vehicle();
        veiculo.setUsuario(usuario);
        veiculo.setModelo(dto.modelo());
        veiculo.setMarca(dto.marca());
        veiculo.setPlaca(dto.placa());
        veiculo.setCor(dto.cor());
        veiculo.setAno(dto.ano());

        return vehicleRepository.save(veiculo);
    }
}
