package com.ifba.tide.dto;

import java.time.LocalDateTime;

public record OcorrenciaDTO(
    String nomeVitima,
    String relato,
    LocalDateTime dataHora,
    double latitude,
    double longitude
) {}