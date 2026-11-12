package com.ifba.tide.model;

import java.time.LocalDateTime;

import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.Data;

@Entity 
@Table(name = "TB_OCORRENCIAS")
@Data
public class Ocorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "nome_vitima")
    private String nomeVitima;

    @Column(columnDefinition = "TEXT")
    private String relato;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    // 1. O Jackson vai ignorar o Point bruto, eliminando qualquer loop interno ou erro de índice
    @JsonIgnore
    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point localizacao;

    // 2. O Jackson chama este método e monta a estrutura perfeita e limpa de coordenadas para o Vue
    @JsonProperty("localizacao")
    public CoordenadasGeo getLocalizacaoCustom() {
        if (this.localizacao == null) return null;
        return new CoordenadasGeo(this.localizacao.getX(), this.localizacao.getY());
    }

    // Estrutura auxiliar interna para formatar o JSON de saída
    public record CoordenadasGeo(Double longitude, Double latitude) {}
}
