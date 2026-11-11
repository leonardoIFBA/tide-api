package com.ifba.tide.repository;

import com.ifba.tide.model.Ocorrencia;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long> {

    // Utiliza a função ST_DWithin do PostGIS convertendo para 'geography' para calcular o raio exato em metros
    @Query(value = "SELECT * FROM TB_OCORRENCIAS WHERE ST_DWithin(CAST(localizacao AS geography), CAST(:ponto AS geography), :raio)", nativeQuery = true)
    List<Ocorrencia> buscarPorProximidade(@Param("ponto") Point ponto, @Param("raio") double raioEmMetros);
}