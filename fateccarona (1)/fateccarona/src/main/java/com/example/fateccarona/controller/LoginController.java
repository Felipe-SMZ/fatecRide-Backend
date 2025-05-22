package com.example.fateccarona.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fateccarona.dtos.LoginDTO;
import com.example.fateccarona.dtos.Response.LoginReposnseDTO;
import com.example.fateccarona.models.User;
import com.example.fateccarona.repository.UserRepository;
import com.example.fateccarona.service.TokenService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
	@Autowired
	private UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenService tokenService;
	@PostMapping
	public ResponseEntity login(@RequestBody LoginDTO body) {
		User user = this.userRepository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
		if(passwordEncoder.matches(body.senha(),user.getSenha()) ) {
			String token = tokenService.generateToken(user);
			return ResponseEntity.ok(new LoginReposnseDTO(user.getIdUsuario(),token));
		}
		
		return ResponseEntity.badRequest().build();
	}

}
