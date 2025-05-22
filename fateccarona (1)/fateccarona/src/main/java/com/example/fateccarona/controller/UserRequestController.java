package com.example.fateccarona.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fateccarona.dtos.RouteCoordinatesDTO;
import com.example.fateccarona.dtos.UserRequestDTO;
import com.example.fateccarona.dtos.Response.UserRequestResponseDTO;
import com.example.fateccarona.models.NominatimResult;
import com.example.fateccarona.models.RequestStatus;
import com.example.fateccarona.models.Ride;
import com.example.fateccarona.models.User;
import com.example.fateccarona.models.UserRequests;
import com.example.fateccarona.repository.RequestRepository;
import com.example.fateccarona.repository.RequestStatusRepository;
import com.example.fateccarona.repository.RideRepository;
import com.example.fateccarona.repository.UserRepository;
import com.example.fateccarona.service.GeoLocationService;

@RestController
@RequestMapping("/passageiro")
public class UserRequestController {

	@Autowired
	RideRepository rideRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RequestStatusRepository requestStatusRepository;
	
    @Autowired
    GeoLocationService geoLocationService;
	
    @Autowired
    RequestRepository requestRepository;
	@PostMapping
	public ResponseEntity<?> createRequest(@RequestBody UserRequestDTO userRequestDTO){
	
		Optional<User> user = userRepository.findById(userRequestDTO.idPassageiro());
		Optional<Ride> ride = rideRepository.findById(userRequestDTO.idCarona());
		Optional<RequestStatus> requestStatus = requestStatusRepository.findById(userRequestDTO.idStatus());
		
		
		if (user.isEmpty()) {
			 return ResponseEntity.badRequest().body("Usuario não encontrado");
		}
		
		if (ride.isEmpty()) {
			 return ResponseEntity.badRequest().body("carona não encontrada");

		}
		if(requestStatus.isEmpty()) {
			 return ResponseEntity.badRequest().body("Status da solicitação inválido");

		}
		if (userRequestDTO.dataHora().isBefore(LocalDateTime.now())) {
	        return ResponseEntity.badRequest().body("Data e hora da carona não podem ser anteriores ao momento atual");

		}
		Optional<NominatimResult> nomOptionalOrigem = geoLocationService.buscarLocal(userRequestDTO.origem());
		
		if (nomOptionalOrigem.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Origem não encontrado");
		}
		
		Optional<NominatimResult> nomOptionalDestino = geoLocationService.buscarLocal(userRequestDTO.destino());
		if (nomOptionalOrigem.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Destino não encontrado");
		}
		
		
		UserRequests newuserRequests = new UserRequests();
	    newuserRequests.setIdPassageiro(user.get());
	    newuserRequests.setIdCarona(ride.get());
	    newuserRequests.setOrigem(userRequestDTO.origem());
	    newuserRequests.setLatitudeOrigem(Double.parseDouble(nomOptionalOrigem.get().getLat()));
	    newuserRequests.setLongitudeOrigem(Double.parseDouble(nomOptionalOrigem.get().getLon()));
	    newuserRequests.setDestino(userRequestDTO.destino());
	    newuserRequests.setLatitudeDestino(Double.parseDouble(nomOptionalDestino.get().getLat()));
	    newuserRequests.setLongitudeDestino(Double.parseDouble(nomOptionalDestino.get().getLon()));		    
	    newuserRequests.setDataHora(userRequestDTO.dataHora());
	    newuserRequests.setIdStatus(requestStatus.get());

	    // Salvar no banco
	    UserRequests savedUserRequest = requestRepository.save(newuserRequests);
//corrigir 
	    UserRequestResponseDTO responseDTO = new UserRequestResponseDTO(
				savedUserRequest.getIdSolicitacao(),
				savedUserRequest.getIdCarona().getIdCarona(),
				savedUserRequest.getIdPassageiro().getIdUsuario(),
				savedUserRequest.getDataHora(),
				savedUserRequest.getOrigem(),
				savedUserRequest.getLongitudeOrigem(),
				savedUserRequest.getLatitudeOrigem(),
				savedUserRequest.getDestino(),
				savedUserRequest.getLongitudeDestino(),
				savedUserRequest.getLatitudeDestino(),
				savedUserRequest.getIdStatus().getIdStatus()
			);

			return ResponseEntity.ok(responseDTO);	
	}
	
	@PutMapping("/{id_solicitacao}")
	public ResponseEntity<?> updateResponse (@PathVariable(name = "id_solicitacao") Integer id_solicitacao, @RequestBody UserRequestDTO userRequestDTO){
		Optional<User> user = userRepository.findById(userRequestDTO.idPassageiro());
		Optional<Ride> ride = rideRepository.findById(userRequestDTO.idCarona());
		Optional<RequestStatus> requestStatus = requestStatusRepository.findById(userRequestDTO.idStatus());
		Optional<UserRequests> userRequestOptional = requestRepository.findById(id_solicitacao);
		
		if(userRequestOptional.isEmpty()) {
			 return ResponseEntity.badRequest().body("Solicitação  não encontrada");

		}
		if (user.isEmpty()) {
			 return ResponseEntity.badRequest().body("Usuario não encontrado");
		}
		
		if (ride.isEmpty()) {
			 return ResponseEntity.badRequest().body("carona não encontrada");

		}
		if(requestStatus.isEmpty()) {
			 return ResponseEntity.badRequest().body("Status da solicitação inválido");

		}
		if (userRequestDTO.dataHora().isBefore(LocalDateTime.now())) {
	        return ResponseEntity.badRequest().body("Data e hora da carona não podem ser anteriores ao momento atual");

		}
		Optional<NominatimResult> nomOptionalOrigem = geoLocationService.buscarLocal(userRequestDTO.origem());
		
		if (nomOptionalOrigem.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Origem não encontrado");
		}
		
		Optional<NominatimResult> nomOptionalDestino = geoLocationService.buscarLocal(userRequestDTO.destino());
		if (nomOptionalOrigem.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Destino não encontrado");
		}
		
		UserRequests existingUserRequest = userRequestOptional.get()	;
		
		existingUserRequest.setIdCarona(ride.get());
		existingUserRequest.setIdPassageiro(user.get());
		existingUserRequest.setIdStatus(requestStatus.get());
		existingUserRequest.setDataHora(userRequestDTO.dataHora());

		existingUserRequest.setOrigem(userRequestDTO.origem());
		existingUserRequest.setLongitudeOrigem(Double.parseDouble(nomOptionalOrigem.get().getLon()));
		existingUserRequest.setLatitudeOrigem(Double.parseDouble(nomOptionalOrigem.get().getLat()));

		existingUserRequest.setDestino(userRequestDTO.destino());
		existingUserRequest.setLongitudeDestino(Double.parseDouble(nomOptionalDestino.get().getLon()));
		existingUserRequest.setLatitudeDestino(Double.parseDouble(nomOptionalDestino.get().getLat()));
		
		
		requestRepository.save(existingUserRequest);
		return ResponseEntity.ok(existingUserRequest);	
	}
	
	
	@GetMapping("/rides/nearby")
	public ResponseEntity<?> findNearbyDrivers(@RequestBody RouteCoordinatesDTO routeCoordinatesDTO){
		
		List<Ride> rideRepositore = rideRepository.findAll();
		
        List<Ride> motoristaProximos =new ArrayList<>();
        
        for(Ride i : rideRepositore) {
        	
        	double distanciaOrigem = geoLocationService.calcularDistancia(routeCoordinatesDTO.latitudeOrigem(), routeCoordinatesDTO.longitudeOrigem(),i.getLatitudeOrigem(), i.getLongitudeOrigem());
        	double distanciaDestino = geoLocationService.calcularDistancia(routeCoordinatesDTO.latitudeDestino(), routeCoordinatesDTO.longitudeDestino(),i.getLatitudeDestino(), i.getLongitudeDestino());
        	if(distanciaOrigem < 9.000000  &&  distanciaDestino < 0.3000000000000000  ) {
				motoristaProximos.add(i);
			}
        }
        
        
        if(motoristaProximos.isEmpty()) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum motorista Proximo que ira a um lugar proximo ao seu");
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(motoristaProximos);
	}
	
}
