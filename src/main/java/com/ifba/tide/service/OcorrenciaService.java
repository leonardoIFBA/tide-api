package com.ifba.tide.service;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

import com.ifba.tide.dto.OcorrenciaDTO;
import com.ifba.tide.model.Ocorrencia;
import com.ifba.tide.repository.OcorrenciaRepository;

@Service
public class OcorrenciaService {

    private final OcorrenciaRepository repository;
    
    // Configura a fábrica de geometrias para utilizar o SRID espacial 4326 do GPS mundial
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public OcorrenciaService(OcorrenciaRepository repository) {
        this.repository = repository;
    }

    public Ocorrencia registrar(OcorrenciaDTO dto) {
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setNomeVitima(dto.nomeVitima());
        ocorrencia.setRelato(dto.relato());
        ocorrencia.setDataHora(dto.dataHora());

        // Atenção: A ordem do JTS Point obrigatoriamente é (Longitude, Latitude)
        Point ponto = geometryFactory.createPoint(new Coordinate(dto.longitude(), dto.latitude()));
        ocorrencia.setLocalizacao(ponto);

        return repository.save(ocorrencia);
    }

    public List<Ocorrencia> listarTodas() {
        return repository.findAll();
    }

    public Ocorrencia buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Ocorrencia> buscarProximas(double latitude, double longitude, double raioMetros) {
        Point pontoCentro = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        return repository.buscarPorProximidade(pontoCentro, raioMetros);
    }
}