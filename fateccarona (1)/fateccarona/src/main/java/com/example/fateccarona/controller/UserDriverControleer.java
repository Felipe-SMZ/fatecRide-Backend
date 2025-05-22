package com.example.fateccarona.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fateccarona.dtos.RideDTO;
import com.example.fateccarona.dtos.Response.RideResponseDTO;
import com.example.fateccarona.models.NominatimResult;
import com.example.fateccarona.models.Ride;
import com.example.fateccarona.models.RideStatus;
import com.example.fateccarona.models.User;
import com.example.fateccarona.repository.GenderRepository;
import com.example.fateccarona.repository.RideRepository;
import com.example.fateccarona.repository.RideStatusReoisitory;
import com.example.fateccarona.repository.UserRepository;
import com.example.fateccarona.repository.UserTypeRepository;
import com.example.fateccarona.repository.VehicleRepositore;
import com.example.fateccarona.service.GeoLocationService;

@RestController
@RequestMapping("/motorista")
public class UserDriverControleer {
  
    
    @Autowired
    UserRepository userRepository;
    @Autowired
    RideStatusReoisitory rideStatusRepository;
    @Autowired
    RideRepository rideRepository;
    
    @Autowired
    GeoLocationService geoLocationService;
  

	@PostMapping
	public ResponseEntity<?> createRite(@RequestBody RideDTO rideDTO){
		
		Optional<User> userOptional = userRepository.findById(rideDTO.idMotorista());
		
		if(userOptional.isEmpty()) {
			 return ResponseEntity.badRequest().body("Motorista não encontrado");
		}
		
		Optional<RideStatus> rideStatusOptional = rideStatusRepository.findById(rideDTO.idStatus());

		if (rideStatusOptional.isEmpty() ) {
			 return ResponseEntity.badRequest().body("Status da carona inválido");
		}
		if (rideDTO.dataHora().isBefore(LocalDateTime.now())) {
	        return ResponseEntity.badRequest().body("Data e hora da carona não podem ser anteriores ao momento atual");

		}
		Optional<NominatimResult> nomOptionalOrigem = geoLocationService.buscarLocal(rideDTO.origem());
		
		if (nomOptionalOrigem.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Origem não encontrado");
		}
		
		Optional<NominatimResult> nomOptionalDestino = geoLocationService.buscarLocal(rideDTO.destino());
		if (nomOptionalOrigem.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Destino não encontrado");
		}
		
		
		Ride ride = new Ride();
		    ride.setIdMotorista(userOptional.get());
		    ride.setOrigem(rideDTO.origem());
		    ride.setLatitudeOrigem(Double.parseDouble(nomOptionalOrigem.get().getLat()));
		    ride.setLongitudeOrigem(Double.parseDouble(nomOptionalOrigem.get().getLon()));
		    ride.setDestino(rideDTO.destino());
		    ride.setLatitudeDestino(Double.parseDouble(nomOptionalDestino.get().getLat()));
		    ride.setLongitudeDestino(Double.parseDouble(nomOptionalDestino.get().getLon()));		    ride.setDataHora(rideDTO.dataHora());
		    ride.setVagasDisponiveis(rideDTO.vagasDisponiveis());
		    ride.setIdStatus(rideStatusOptional.get());

		    // Salvar no banco
		    Ride savedRide = rideRepository.save(ride);

		    RideResponseDTO rideResponseDTO =new RideResponseDTO(
			    	ride.getIdCarona()	,
				    ride.getIdMotorista().getIdUsuario(),
			        ride.getOrigem(),
			        ride.getLatitudeOrigem(),
			        ride.getLongitudeOrigem(),
			        ride.getDestino(),
			        ride.getLatitudeDestino(),
			        ride.getLongitudeDestino(),
			        ride.getDataHora(),
			        ride.getVagasDisponiveis(),
			        ride.getIdMotorista().getIdUsuario()
			        );
			        
			    
		    return ResponseEntity.ok(rideResponseDTO);
	}
	
	
	@PutMapping("/{id_carona}")
	public ResponseEntity<?> updateRide(@PathVariable(name = "id_carona") Integer id_carona, @RequestBody RideDTO rideDTO){

		Optional<User> userOptional = userRepository.findById(rideDTO.idMotorista());
		Optional<Ride> rideOptional = rideRepository.findById(id_carona);
		
		if(rideOptional.isEmpty()) {
			
			return ResponseEntity.badRequest().body("Carona  não encontrado");
		}
		if(userOptional.isEmpty()) {
			 return ResponseEntity.badRequest().body("Motorista não encontrado");
		}
		
		Optional<RideStatus> rideStatusOptional = rideStatusRepository.findById(rideDTO.idStatus());

		if (rideStatusOptional.isEmpty() ) {
			 return ResponseEntity.badRequest().body("Status da carona inválido");
		}
		if (rideDTO.dataHora().isBefore(LocalDateTime.now())) {
	        return ResponseEntity.badRequest().body("Data e hora da carona não podem ser anteriores ao momento atual");

		}
		Optional<NominatimResult> nomOptionalOrigem = geoLocationService.buscarLocal(rideDTO.origem());
		
		if (nomOptionalOrigem.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Origem não encontrado");
		}
		
		Optional<NominatimResult> nomOptionalDestino = geoLocationService.buscarLocal(rideDTO.destino());
		if (nomOptionalOrigem.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Destino não encontrado");
		}
		Ride existingRide = rideOptional.get();
		
		existingRide.setOrigem(rideDTO.origem());
		existingRide.setLatitudeOrigem(Double.parseDouble(nomOptionalOrigem.get().getLat()));
		existingRide.setLongitudeOrigem(Double.parseDouble(nomOptionalOrigem.get().getLon()));
		existingRide.setDestino(rideDTO.destino());
		existingRide.setLatitudeDestino(Double.parseDouble(nomOptionalDestino.get().getLat()));
		existingRide.setLongitudeDestino(Double.parseDouble(nomOptionalDestino.get().getLon()));		    
		existingRide.setDataHora(rideDTO.dataHora());
		existingRide.setVagasDisponiveis(rideDTO.vagasDisponiveis());
		existingRide.setIdStatus(rideStatusOptional.get());

		Ride ride=  rideRepository.save(existingRide);
		
		 RideResponseDTO rideResponseDTO =new RideResponseDTO(
				 	ride.getIdCarona(),
				    ride.getIdMotorista().getIdUsuario(),
				    ride.getOrigem(),
			        ride.getLatitudeOrigem(),
			        ride.getLongitudeOrigem(),
			        ride.getDestino(),
			        ride.getLatitudeDestino(),
			        ride.getLongitudeDestino(),
			        ride.getDataHora(),
			        ride.getVagasDisponiveis(),
			        ride.getIdMotorista().getIdUsuario()
			        );
		return ResponseEntity.ok(rideResponseDTO);
		
	}
	
	
	@DeleteMapping("/{id_carona}")
	public ResponseEntity<?> deleteRide(@PathVariable(name = "id_carona") Integer id_carona){
		Optional<Ride> rideOptional = rideRepository.findById(id_carona);
		
		if(rideOptional.isEmpty()) {
			
			return ResponseEntity.badRequest().body("Carona  não encontrado");
		}
		
		rideRepository.deleteById(rideOptional.get().getIdCarona());
		return ResponseEntity.status(HttpStatus.OK).body("carona excluida"); 
		
	}
	
	@GetMapping("/{idMotorista}")
	public ResponseEntity<?> getAllRides(@PathVariable(name = "idMotorista") Integer idMotorista){
	
		Optional<User> optionalUser = userRepository.findById(idMotorista);
		
		if (optionalUser.isEmpty()) {
			return ResponseEntity.badRequest().body("usuario  não encontrado");
		}
		
		List<Ride> allRides = rideRepository.findAllByIdMotorista(optionalUser);
		List<RideResponseDTO> response = allRides.stream().map(ride ->
	    new RideResponseDTO(
	    	ride.getIdCarona()	,
		    ride.getIdMotorista().getIdUsuario(),
	        ride.getOrigem(),
	        ride.getLatitudeOrigem(),
	        ride.getLongitudeOrigem(),
	        ride.getDestino(),
	        ride.getLatitudeDestino(),
	        ride.getLongitudeDestino(),
	        ride.getDataHora(),
	        ride.getVagasDisponiveis(),
	        ride.getIdMotorista().getIdUsuario()
	        
	    )
	).toList();

	return ResponseEntity.ok(response);
		
		
	}
}
