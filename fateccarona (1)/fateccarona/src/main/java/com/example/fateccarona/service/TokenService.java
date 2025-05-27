package com.example.fateccarona.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.fateccarona.models.User;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    // Gera o token JWT com id do usuário como claim e email como subject
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("fateccarona")
                    .withSubject(user.getEmail())          // email no subject (pode manter para compatibilidade)
                    .withClaim("userId", user.getId())     // aqui inclui o id do usuário como claim
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    // Valida o token e retorna o subject (email) caso válido, ou null se inválido
    public String validateToken(String token) {
        System.out.println("Validando token: " + token);
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("fateccarona")
                    .build()
                    .verify(cleanToken(token));
            return decodedJWT.getSubject();
        } catch (JWTVerificationException exception) {
            System.err.println("Token inválido: " + exception.getMessage());
            return null;
        }
    }


    // Extrai o ID do usuário do token JWT
    public Integer getUserIdFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("fateccarona")
                    .build()
                    .verify(cleanToken(token));
            // Retorna o claim "userId" como Integer
            return decodedJWT.getClaim("userId").asInt();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token inválido ou expirado", exception);
        }
    }

    // Remove o prefixo "Bearer " se existir no token
    private String cleanToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

    // Define validade do token: 2 horas à partir do horário atual (UTC)
    private Instant generateExpirationDate() {
        return Instant.now().plus(2, ChronoUnit.HOURS);
    }
    
    }

    

