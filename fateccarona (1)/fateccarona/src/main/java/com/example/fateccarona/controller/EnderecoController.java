package com.example.fateccarona.controller;

import com.example.fateccarona.models.Endereco;
import com.example.fateccarona.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    // Criar ou atualizar endereço para usuário
    @PostMapping("/{idUsuario}")
    public ResponseEntity<Endereco> salvarEndereco(@PathVariable Integer idUsuario, @RequestBody Endereco endereco) {
        try {
            Endereco enderecoSalvo = enderecoService.salvarEndereco(idUsuario, endereco);
            return ResponseEntity.ok(enderecoSalvo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Buscar endereço pelo id do usuário
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Endereco> buscarPorUsuario(@PathVariable Integer idUsuario) {
        Optional<Endereco> endereco = enderecoService.buscarPorUsuario(idUsuario);
        return endereco.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Buscar endereço pelo id do endereço
    @GetMapping("/{id}")
    public ResponseEntity<Endereco> buscarPorId(@PathVariable Integer id) {
        Optional<Endereco> endereco = enderecoService.buscarPorId(id);
        return endereco.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Deletar endereço pelo id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEndereco(@PathVariable Integer id) {
        enderecoService.deletarEndereco(id);
        return ResponseEntity.noContent().build();
    }
}
