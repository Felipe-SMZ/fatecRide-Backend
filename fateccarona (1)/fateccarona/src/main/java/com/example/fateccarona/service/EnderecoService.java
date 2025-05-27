package com.example.fateccarona.service;

import com.example.fateccarona.models.Endereco;
import com.example.fateccarona.models.User;
import com.example.fateccarona.repository.EnderecoRepository;
import com.example.fateccarona.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UserRepository userRepository;

    // Salvar ou atualizar endereço com link para usuário
    public Endereco salvarEndereco(Integer idUsuario, Endereco endereco) {
        Optional<User> usuarioOpt = userRepository.findById(idUsuario);
        if (!usuarioOpt.isPresent()) {
            throw new RuntimeException("Usuário não encontrado com id: " + idUsuario);
        }

        User usuario = usuarioOpt.get();
        endereco.setUsuario(usuario);
        usuario.setEndereco(endereco);

        // Como cascade ALL está configurado, pode salvar só o usuário (opcional)
        // Mas aqui salvamos direto o endereço para garantir
        return enderecoRepository.save(endereco);
    }

    // Buscar endereço pelo id do usuário
    public Optional<Endereco> buscarPorUsuario(Integer idUsuario) {
        return Optional.ofNullable(enderecoRepository.findByUsuarioIdUsuario(idUsuario));
    }

    // Buscar endereço por id
    public Optional<Endereco> buscarPorId(Integer id) {
        return enderecoRepository.findById(id);
    }

    // Deletar endereço por id
    public void deletarEndereco(Integer id) {
        enderecoRepository.deleteById(id);
    }
}
