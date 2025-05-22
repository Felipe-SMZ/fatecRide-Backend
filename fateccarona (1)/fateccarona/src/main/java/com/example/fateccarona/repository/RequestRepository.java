package com.example.fateccarona.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fateccarona.models.UserRequests;

public interface RequestRepository extends JpaRepository<UserRequests, Integer> {

}
