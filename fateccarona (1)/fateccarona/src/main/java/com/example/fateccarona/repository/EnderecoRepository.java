package com.example.fateccarona.repository;

import com.example.fateccarona.models.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {

    // Método para buscar endereço pelo usuário (idUsuario)
    Endereco findByUsuarioIdUsuario(Integer idUsuario);
}
