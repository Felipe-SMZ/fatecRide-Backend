package com.example.fateccarona.dtos;

import java.time.LocalDateTime;

public record RideDTO (

     Integer idMotorista,
     String origem,
     String destino,
     LocalDateTime dataHora,
     Integer vagasDisponiveis,
     Integer idStatus
){}
