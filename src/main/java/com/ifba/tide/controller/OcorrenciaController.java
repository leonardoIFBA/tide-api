package com.ifba.tide.controller;

import com.ifba.tide.dto.OcorrenciaDTO;
import com.ifba.tide.model.Ocorrencia;
import com.ifba.tide.service.OcorrenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST responsável por expor os endpoints da API de ocorrências.
 * Centraliza as requisições HTTP vindas do Front-end (Vue) e direciona para a camada de serviço.
 */

@RestController
@RequestMapping("/ocorrencias")
public class OcorrenciaController {

    private final OcorrenciaService service;

    // Construtor para injeção de dependência automatizada do Spring Boot.
    public OcorrenciaController(OcorrenciaService service) {
        this.service = service;
    }

    /**
     * Endpoint para cadastrar uma nova ocorrência.
     * Mapeia requisições POST para http://localhost:8080/tide-api/ocorrencias
     * 
     * @RequestBody dto Corpo da requisição contendo os dados estruturados da nova ocorrência.
     * @return Retorna um ResponseEntity preenchido com a ocorrência criada e status 200 OK.
     */
    @PostMapping
    public ResponseEntity<Ocorrencia> criar(@RequestBody OcorrenciaDTO dto) {
        Ocorrencia novaOcorrencia = service.registrar(dto);
        return ResponseEntity.ok(novaOcorrencia);
    }

    /**
     * Endpoint para atualizar uma ocorrência existente.
     * Mapeia requisições PUT para http://localhost:8080/tide-api/ocorrencias/{id}
     * 
     * @param id  Identificador passado na rota da URL.
     * @param dto Corpo JSON contendo os novos dados atualizados da ocorrência.
     * @return Status 200 OK com o objeto atualizado, ou 404 Not Found se o ID não existir.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ocorrencia> atualizar(@PathVariable Long id, @RequestBody OcorrenciaDTO dto) {
        Ocorrencia ocorrenciaAtualizada = service.atualizar(id, dto);
        if (ocorrenciaAtualizada != null) {
            return ResponseEntity.ok(ocorrenciaAtualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para excluir uma ocorrência específica pelo seu ID.
     * Mapeia requisições DELETE para http://localhost:8080/tide-api/ocorrencias/{id}
     * 
     * @param id Identificador passado diretamente na rota da URL.
     * @return Status 204 No Content se deletado com sucesso, ou 404 Not Found se o ID não existir.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        boolean deletado = service.excluir(id);
        if (deletado) {
            // No padrão REST, retornar 204 No Content é a melhor prática para exclusões bem-sucedidas
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para listar todas as ocorrências salvas na base de dados, sem restrição geográfica.
     * Mapeia requisições GET para http://localhost:8080/tide-api/ocorrencias
     * 
     * @return Lista contendo todas as entidades registradas com status 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<Ocorrencia>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    /**
     * Endpoint para buscar uma única ocorrência filtrando pelo identificador exclusivo.
     * Mapeia requisições GET contendo uma variável na URL, ex: /ocorrencias/5
     * 
     * @param id Identificador numérico passado diretamente na rota da URL.
     * @return Retorna 200 OK com a entidade se encontrada, ou 404 Not Found se não existir no banco.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ocorrencia> buscarPorId(@PathVariable Long id) {
        Ocorrencia ocorrencia = service.buscarPorId(id);
        if (ocorrencia != null) {
            return ResponseEntity.ok(ocorrencia);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

     /**
     * Endpoint geoespacial para listar as ocorrências contidas dentro de um raio de proximidade.
     * Mapeia requisições GET utilizando Query Parameters, ex: /ocorrencias/proximas?latitude=-14.2&longitude=-41.6&raio=5000
     * 
     * @param latitude Coordenada Y de origem repassada via parâmetro de busca na URL.
     * @param longitude Coordenada X de origem repassada via parâmetro de busca na URL.
     * @param raio Raio de busca geográfico medido estritamente em metros.
     * @return Lista filtrada de ocorrências que estão dentro do perímetro delimitado.
     */
    @GetMapping("/proximas")
    public ResponseEntity<List<Ocorrencia>> buscarProximas(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double raio) {
        List<Ocorrencia> proximas = service.buscarProximas(latitude, longitude, raio);
        return ResponseEntity.ok(proximas);
    }
}
