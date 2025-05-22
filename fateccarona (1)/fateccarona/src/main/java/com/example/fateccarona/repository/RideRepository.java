package com.example.fateccarona.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fateccarona.models.Ride;
import com.example.fateccarona.models.User;

public interface RideRepository extends JpaRepository<Ride, Integer>{

	List<Ride> findAllByIdMotorista(Optional<User> optionalUser);

}
