package com.ifba.tide.controller;

import com.ifba.tide.dto.OcorrenciaDTO;
import com.ifba.tide.model.Ocorrencia;
import com.ifba.tide.service.OcorrenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ocorrencias")
public class OcorrenciaController {

    private final OcorrenciaService service;

    public OcorrenciaController(OcorrenciaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Ocorrencia> criar(@RequestBody OcorrenciaDTO dto) {
        Ocorrencia novaOcorrencia = service.registrar(dto);
        return ResponseEntity.ok(novaOcorrencia);
    }

    @GetMapping
    public ResponseEntity<List<Ocorrencia>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ocorrencia> buscarPorId(@PathVariable Long id) {
        Ocorrencia ocorrencia = service.buscarPorId(id);
        if (ocorrencia != null) {
            return ResponseEntity.ok(ocorrencia);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/proximas")
    public ResponseEntity<List<Ocorrencia>> buscarProximas(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double raio) {
        List<Ocorrencia> proximas = service.buscarProximas(latitude, longitude, raio);
        return ResponseEntity.ok(proximas);
    }
}
