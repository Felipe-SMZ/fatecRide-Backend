package com.example.fateccarona.service;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.fateccarona.models.User;
import com.example.fateccarona.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // Ignorar autenticação para endpoints públicos
        if ((path.equals("/usuario") && method.equalsIgnoreCase("POST")) ||
                path.startsWith("/cadastrar") || path.startsWith("/genders") || path.startsWith("/cadastro")) {
                filterChain.doFilter(request, response);
                return;
            }

        String token = recoverToken(request);
        System.out.println("SecurityFilter - Token recuperado: " + 
            (token != null ? token.substring(0, Math.min(token.length(), 10)) + "..." : "null"));

        if (token == null || token.isEmpty()) {
            System.out.println("SecurityFilter - Token ausente ou vazio");
            filterChain.doFilter(request, response);
            return;
        }

        String login = tokenService.validateToken(token.trim());
        if (login != null) {
            System.out.println("SecurityFilter - Token válido para usuário: " + login);
            var userOptional = userRepository.findByEmail(login);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("SecurityFilter - Usuário autenticado: " + user.getEmail());
            } else {
                System.out.println("SecurityFilter - Usuário não encontrado no banco: " + login);
            }
        } else {
            System.out.println("SecurityFilter - Token inválido");
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("SecurityFilter - Authorization header ausente ou mal formatado");
            return null;
        }
        // Remove o prefixo "Bearer " e elimina espaços em branco
        return authHeader.substring(7).trim();
    }
}
