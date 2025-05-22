package com.example.fateccarona.controller;

import java.util.Optional;

import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.fateccarona.dtos.UserDTO;
import com.example.fateccarona.dtos.Response.LoginReposnseDTO;
import com.example.fateccarona.models.Gender;
import com.example.fateccarona.models.User;
import com.example.fateccarona.models.UserType;
import com.example.fateccarona.models.Vehicle;
import com.example.fateccarona.repository.GenderRepository;
import com.example.fateccarona.repository.UserRepository;
import com.example.fateccarona.repository.UserTypeRepository;
import com.example.fateccarona.repository.VehicleRepositore;
import com.example.fateccarona.service.TokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    @Autowired
    GenderRepository genderRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    UserTypeRepository userTypeRepository;
    
    @Autowired
    VehicleRepositore vehicleRepository;  // Correção do nome do repositório

    
    private final PasswordEncoder passwordEncoder;
	private final TokenService tokenService;
	
    @PostMapping("/cadastrar")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        System.out.println("Recebido tipo de usuário ID: " + userDTO.idTipoUsuario());
        System.out.println("Recebido gênero ID: " + userDTO.idGenero());

        Optional<UserType> userTypeOptional = userTypeRepository.findById(userDTO.idTipoUsuario());
        Optional<Gender> genderOptional = genderRepository.findById(userDTO.idGenero());
        Optional<User> userOptional = userRepository.findByEmail(userDTO.email());
        
        if(userDTO.vehicle() != null) {
            Optional<Vehicle> vehicleOptional = vehicleRepository.findByPlaca(userDTO.vehicle().placa());
            if(vehicleOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Placa já cadastrada");
            }
        }

        if(userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já cadastrado");
        }

        if (userTypeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de usuário não encontrado");
        }

        if (genderOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gênero não encontrado");
        }
        
        UserType userType = userTypeOptional.get();
        Gender gender = genderOptional.get();

        User newUser = new User();
        newUser.setNome(userDTO.nome());
        newUser.setSobrenome(userDTO.sobrenome());
        newUser.setEmail(userDTO.email());
        newUser.setSenha(passwordEncoder.encode(userDTO.senha()));
        newUser.setTelefone(userDTO.telefone());
        newUser.setFoto(userDTO.foto());
        newUser.setGenero(gender);
        newUser.setTipoUsuario(userType);

        userRepository.save(newUser);

        if (userDTO.vehicle() != null) {
            Vehicle newVehicle = new Vehicle();
            BeanUtils.copyProperties(userDTO.vehicle(), newVehicle);
            newVehicle.setUsuario(newUser);
            vehicleRepository.save(newVehicle);
        }
		String token = this.tokenService.generateToken(newUser);

        return ResponseEntity.ok(new LoginReposnseDTO(newUser.getIdUsuario(), token));
    }
    
    @PutMapping("/usuario/{id_usuario}")
    public ResponseEntity<?> updateUser(@PathVariable(name = "id_usuario") Integer id_usuario, @RequestBody UserDTO userDTO) {
        // Verifica se o usuário existe
        Optional<User> userOptional = userRepository.findById(id_usuario);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        // Verifica o tipo de usuário e gênero
        Optional<UserType> userTypeOptional = userTypeRepository.findById(userDTO.idTipoUsuario());
        Optional<Gender> genderOptional = genderRepository.findById(userDTO.idGenero());
        Optional<User> userEmailOptional = userRepository.findByEmail(userDTO.email());

        // Verifica se o email já está registrado por outro usuário
        if (userEmailOptional.isPresent() && !userEmailOptional.get().getIdUsuario().equals(id_usuario)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já cadastrado por outro usuário");
        }

        // Verifica se o tipo de usuário e gênero existem
        if (userTypeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de usuário não encontrado");
        }
        if (genderOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gênero não encontrado");
        }

        // Atualiza as informações do usuário
        User existingUser = userOptional.get();
        existingUser.setNome(userDTO.nome());
        existingUser.setSobrenome(userDTO.sobrenome());
        existingUser.setEmail(userDTO.email());
        existingUser.setSenha(userDTO.senha());
        existingUser.setTelefone(userDTO.telefone());
        existingUser.setFoto(userDTO.foto());
        existingUser.setTipoUsuario(userTypeOptional.get());
        existingUser.setGenero(genderOptional.get());

        // Salva o usuário atualizado
        userRepository.save(existingUser);

        // Atualiza o veículo, se fornecido no DTO
        if (userDTO.vehicle() != null) {
            // Verifica se a placa já está registrada por outro usuário
            Optional<Vehicle> existingVehicleByPlaca = vehicleRepository.findByPlaca(userDTO.vehicle().placa());
            if (existingVehicleByPlaca.isPresent() && !existingVehicleByPlaca.get().getUsuario().getIdUsuario().equals(id_usuario)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Placa já cadastrada por outro usuário");
            }

            // Busca o veículo existente ou cria um novo para o usuário
            Optional<Vehicle> userVehicleOptional = vehicleRepository.findByUsuarioIdUsuario(id_usuario);
            Vehicle vehicleToSave = userVehicleOptional.orElse(new Vehicle());

            // Copia as propriedades do DTO para o veículo, garantindo que o usuário seja associado corretamente
            BeanUtils.copyProperties(userDTO.vehicle(), vehicleToSave);

            // Garante que o veículo seja associado ao usuário correto
            vehicleToSave.setUsuario(existingUser);

            // Salva o veículo (atualiza ou cria conforme necessário)
            vehicleRepository.save(vehicleToSave);
        }

        return ResponseEntity.ok("Usuário atualizado com sucesso");
    }
    @DeleteMapping("/usuario/{id_usuario}")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "id_usuario") Integer id_usuario){
    	
    	
    	Optional<User> userOptional = userRepository.findById(id_usuario);
    	if(userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");

    	}
    	Optional<Vehicle> optionalVehicle = vehicleRepository.findByUsuarioIdUsuario(id_usuario);
    	if(!optionalVehicle.isEmpty()){
    		vehicleRepository.delete(optionalVehicle.get());
    	}
    	
    	userRepository.delete(userOptional.get());
    	return ResponseEntity.status(HttpStatus.OK).body("Usuario deleteado com sucesso");
    }
    	
}
