package com.ifba.tide.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) para o envio de novas ocorrências.
 * Utiliza o recurso 'record' do Java para garantir um objeto imutável,
 * servindo exclusivamente para transportar os dados recebidos do Front-end (Vue) 
 * até a camada de controle do Back-end.
 *
 * @param nomeVitima Nome completo ou iniciais da vítima.
 * @param relato Descrição detalhada do incidente criminal.
 * @param dataHora Carimbo de data e hora em que a ocorrência aconteceu.
 * @param latitude Coordenada geográfica vertical (Y) obtida via GPS do dispositivo.
 * @param longitude Coordenada geográfica horizontal (X) obtida via GPS do dispositivo.
 */
public record OcorrenciaDTO(
    String nomeVitima,
    String relato,
    LocalDateTime dataHora,
    double latitude,
    double longitude
) {}