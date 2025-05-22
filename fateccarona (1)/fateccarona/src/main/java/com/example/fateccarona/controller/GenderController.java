package com.example.fateccarona.controller;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fateccarona.models.Gender;
import com.example.fateccarona.repository.GenderRepository;

@RestController
@RequestMapping("/genders")
public class GenderController {
	@Autowired
	GenderRepository genderRepository;
	@GetMapping
	public ResponseEntity<?> getterGender(){
		List<Gender> gender = genderRepository.findAll();
		
		if(gender.isEmpty()) {
			ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum sexo encontrado");
		}
		return ResponseEntity.status(HttpStatus.OK).body(gender);
	}
}
