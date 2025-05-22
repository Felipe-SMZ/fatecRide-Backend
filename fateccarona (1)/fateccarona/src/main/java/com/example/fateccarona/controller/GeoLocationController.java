package com.example.fateccarona.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fateccarona.models.NominatimResult;
import com.example.fateccarona.service.GeoLocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RestController
@RequestMapping
public class GeoLocationController {

	@Autowired
	GeoLocationService geoLocationService;
	@GetMapping("/local")
	public ResponseEntity<?> buscarLocal(@RequestParam String local) {
		
		Optional<NominatimResult> nominatimResult  = geoLocationService.buscarLocal(local);
		
		if (nominatimResult.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum endereço encontrado");
		}
		return  ResponseEntity.status(HttpStatus.OK).body(nominatimResult);

	}

	
}
