package com.example.fateccarona.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fateccarona.dtos.UserDTO;
import com.example.fateccarona.dtos.VehicleDTO;
import com.example.fateccarona.models.Gender;
import com.example.fateccarona.models.User;
import com.example.fateccarona.models.UserType;
import com.example.fateccarona.models.Vehicle;
import com.example.fateccarona.repository.GenderRepository;
import com.example.fateccarona.repository.UserRepository;
import com.example.fateccarona.repository.UserTypeRepository;
import com.example.fateccarona.repository.VehicleRepository;
import com.example.fateccarona.service.TokenService;
import com.example.fateccarona.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UserController {

	private final GenderRepository genderRepository;
	private final UserRepository userRepository;
	private final UserTypeRepository userTypeRepository;
	private final VehicleRepository vehicleRepository;

	private final PasswordEncoder passwordEncoder;
	private final TokenService tokenService;

	private Optional<String> extractToken(String authHeader) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return Optional.empty();
		}
		return Optional.of(authHeader.substring(7));
	}

	@GetMapping("/logado")
	public ResponseEntity<?> getUsuarioLogado(
			@RequestHeader(name = "Authorization", required = false) String authHeader) {

		Optional<String> tokenOpt = extractToken(authHeader);
		if (tokenOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token não fornecido ou inválido");
		}

		Integer userId;
		try {
			userId = tokenService.getUserIdFromToken(tokenOpt.get());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
		}

		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido: usuário não identificado");
		}

		Optional<User> userOptional = userRepository.findById(userId);
		if (userOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
		}

		User user = userOptional.get();

		VehicleDTO vehicleDTO = null;
		Optional<Vehicle> vehicleOptional = vehicleRepository.findByUsuarioIdUsuario(userId);
		if (vehicleOptional.isPresent()) {
			Vehicle v = vehicleOptional.get();
			vehicleDTO = new VehicleDTO(v.getModelo(), v.getMarca(), v.getPlaca(), v.getCor(), v.getAno());
		}

		UserDTO userDTO = new UserDTO(user.getNome(), user.getSobrenome(), user.getEmail(), null, user.getTelefone(),
				user.getFoto(), user.getTipoUsuario() != null ? user.getTipoUsuario().getIdTipoUsuario() : null,
				user.getGenero() != null ? user.getGenero().getIdGenero() : null, vehicleDTO);

		return ResponseEntity.ok(userDTO);
	}

	@PutMapping("/logado")
	public ResponseEntity<?> updateUsuarioLogado(
			@RequestHeader(name = "Authorization", required = false) String authHeader, @RequestBody UserDTO userDTO) {

		Optional<String> tokenOpt = extractToken(authHeader);
		if (tokenOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token não fornecido ou inválido");
		}

		Integer userId;
		try {
			userId = tokenService.getUserIdFromToken(tokenOpt.get());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
		}

		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido: usuário não identificado");
		}

		Optional<User> userOptional = userRepository.findById(userId);
		if (userOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
		}

		Optional<UserType> userTypeOptional = userTypeRepository.findById(userDTO.idTipoUsuario());
		Optional<Gender> genderOptional = genderRepository.findById(userDTO.idGenero());
		Optional<User> userEmailOptional = userRepository.findByEmail(userDTO.email());

		if (userEmailOptional.isPresent() && !userEmailOptional.get().getIdUsuario().equals(userId)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já cadastrado por outro usuário");
		}

		if (userTypeOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de usuário não encontrado");
		}
		if (genderOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gênero não encontrado");
		}

		User existingUser = userOptional.get();
		existingUser.setNome(userDTO.nome());
		existingUser.setSobrenome(userDTO.sobrenome());
		existingUser.setEmail(userDTO.email());

		if (userDTO.senha() != null && !userDTO.senha().isEmpty()) {
			existingUser.setSenha(passwordEncoder.encode(userDTO.senha()));
		}

		existingUser.setTelefone(userDTO.telefone());
		existingUser.setFoto(userDTO.foto());
		existingUser.setTipoUsuario(userTypeOptional.get());
		existingUser.setGenero(genderOptional.get());

		userRepository.save(existingUser);

		if (userDTO.vehicle() != null) {
			Optional<Vehicle> existingVehicleByPlaca = vehicleRepository.findByPlaca(userDTO.vehicle().placa());
			if (existingVehicleByPlaca.isPresent()
					&& !existingVehicleByPlaca.get().getUsuario().getIdUsuario().equals(userId)) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Placa já cadastrada por outro usuário");
			}

			Optional<Vehicle> userVehicleOptional = vehicleRepository.findByUsuarioIdUsuario(userId);
			Vehicle vehicleToSave = userVehicleOptional.orElse(new Vehicle());

			// Copia propriedades do DTO para a entidade veículo, exceto id
			BeanUtils.copyProperties(userDTO.vehicle(), vehicleToSave, "id");
			vehicleToSave.setUsuario(existingUser);

			vehicleRepository.save(vehicleToSave);
		}

		return ResponseEntity.ok("Usuário atualizado com sucesso");
	}

	@PutMapping("/{id_usuario}")
	public ResponseEntity<?> updateUser(@PathVariable(name = "id_usuario") Integer id_usuario,
			@RequestBody UserDTO userDTO) {

		Optional<User> userOptional = userRepository.findById(id_usuario);
		if (userOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
		}

		Optional<UserType> userTypeOptional = userTypeRepository.findById(userDTO.idTipoUsuario());
		Optional<Gender> genderOptional = genderRepository.findById(userDTO.idGenero());
		Optional<User> userEmailOptional = userRepository.findByEmail(userDTO.email());

		if (userEmailOptional.isPresent() && !userEmailOptional.get().getIdUsuario().equals(id_usuario)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já cadastrado por outro usuário");
		}

		if (userTypeOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de usuário não encontrado");
		}
		if (genderOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gênero não encontrado");
		}

		User existingUser = userOptional.get();
		existingUser.setNome(userDTO.nome());
		existingUser.setSobrenome(userDTO.sobrenome());
		existingUser.setEmail(userDTO.email());

		if (userDTO.senha() != null && !userDTO.senha().isEmpty()) {
			existingUser.setSenha(passwordEncoder.encode(userDTO.senha()));
		}

		existingUser.setTelefone(userDTO.telefone());
		existingUser.setFoto(userDTO.foto());
		existingUser.setTipoUsuario(userTypeOptional.get());
		existingUser.setGenero(genderOptional.get());

		userRepository.save(existingUser);

		if (userDTO.vehicle() != null) {
			Optional<Vehicle> existingVehicleByPlaca = vehicleRepository.findByPlaca(userDTO.vehicle().placa());
			if (existingVehicleByPlaca.isPresent()
					&& !existingVehicleByPlaca.get().getUsuario().getIdUsuario().equals(id_usuario)) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Placa já cadastrada por outro usuário");
			}

			Optional<Vehicle> userVehicleOptional = vehicleRepository.findByUsuarioIdUsuario(id_usuario);
			Vehicle vehicleToSave = userVehicleOptional.orElse(new Vehicle());

			BeanUtils.copyProperties(userDTO.vehicle(), vehicleToSave, "id");
			vehicleToSave.setUsuario(existingUser);

			vehicleRepository.save(vehicleToSave);
		}

		return ResponseEntity.ok("Usuário atualizado com sucesso");
	}

	@Autowired
	private UserService userService;

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
	    Optional<User> userOpt = userRepository.findById(id);
	    if (userOpt.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
	    }
	    userRepository.delete(userOpt.get());
	    return ResponseEntity.ok("Usuário e dados relacionados removidos com sucesso");
	}
	
	
	@PostMapping
	public ResponseEntity<?> criarUsuario(@RequestBody UserDTO userDTO) {
	    // Validar email único
	    if (userRepository.findByEmail(userDTO.email()).isPresent()) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já cadastrado");
	    }

	    // Buscar gênero e tipo usuário
	    Optional<Gender> genderOpt = genderRepository.findById(userDTO.idGenero());
	    Optional<UserType> userTypeOpt = userTypeRepository.findById(userDTO.idTipoUsuario());

	    if (genderOpt.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gênero não encontrado");
	    }
	    if (userTypeOpt.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de usuário não encontrado");
	    }

	    // Criar novo usuário
	    User newUser = new User();
	    newUser.setNome(userDTO.nome());
	    newUser.setSobrenome(userDTO.sobrenome());
	    newUser.setEmail(userDTO.email());
	    newUser.setSenha(passwordEncoder.encode(userDTO.senha())); // criptografa a senha
	    newUser.setTelefone(userDTO.telefone());
	    newUser.setGenero(genderOpt.get());
	    newUser.setTipoUsuario(userTypeOpt.get());

	    userRepository.save(newUser);

	    // Retornar id criado para frontend
	    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("idUsuario", newUser.getIdUsuario()));
	}


}
