package com.example.fateccarona.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fateccarona.models.Gender;

public interface GenderRepository extends JpaRepository<Gender, Integer>{

}
