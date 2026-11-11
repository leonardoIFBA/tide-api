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

/**
 * Serviço responsável por gerenciar as regras de negócio das ocorrências,
 * integrando persistência de dados e manipulação de geometrias espaciais (PostGIS).
 */

@Service
public class OcorrenciaService {

    // final garante que essa dependência nunca será alterada ou ficará nula depois que a classe for criada.
    private final OcorrenciaRepository repository;
    

    /**
     * Fábrica de geometrias configurada com o SRID 4326.
     * O SRID 4326 define o sistema de coordenadas geográficas WGS 84, 
     * padrão mundial utilizado por sistemas de GPS (Latitude e Longitude em graus).
     */
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public OcorrenciaService(OcorrenciaRepository repository) {
        this.repository = repository;
    }

    /**
     * Registra uma nova ocorrência convertendo os dados simples do DTO 
     * em um objeto geométrico tridimensional aceito pelo PostGIS.
     * 
     * @param dto Objeto contendo os dados da vítima, relato e coordenadas numéricas.
     * @return A entidade Ocorrencia salva e persistida com ID gerado.
     */
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

    /**
     * Exclui uma ocorrência do banco de dados pelo seu ID único.
     * 
     * @param id Identificador da ocorrência a ser removida.
     * @return true se a ocorrência existia e foi deletada, false caso contrário.
     */
    public boolean excluir(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Atualiza os dados de uma ocorrência existente no banco de dados.
     * 
     * @param id  Identificador da ocorrência que será modificada.
     * @param dto Objeto contendo as novas informações para atualização.
     * @return A entidade Ocorrencia atualizada e salva, ou null se o ID não for encontrado.
     */
    public Ocorrencia atualizar(Long id, OcorrenciaDTO dto) {
        return repository.findById(id).map(ocorrenciaExistente -> {
            // Atualiza os dados textuais e temporais
            ocorrenciaExistente.setNomeVitima(dto.nomeVitima());
            ocorrenciaExistente.setRelato(dto.relato());
            ocorrenciaExistente.setDataHora(dto.dataHora());

            // Recalcula e atualiza a localização espacial PostGIS (Longitude, Latitude)
            Point novoPonto = geometryFactory.createPoint(new Coordinate(dto.longitude(), dto.latitude()));
            ocorrenciaExistente.setLocalizacao(novoPonto);

            // Salva as alterações por cima do registro antigo
            return repository.save(ocorrenciaExistente);
        }).orElse(null);
    }

    // Recupera todas as ocorrências cadastradas na base de dados, sem filtros.    
    public List<Ocorrencia> listarTodas() {
        return repository.findAll();
    }

    //Busca os detalhes de uma ocorrência específica por meio de seu ID.
    public Ocorrencia buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Realiza a busca de ocorrências criminais dentro de um perímetro circular específico.
     * 
     * @param latitude Coordenada Y do centro da busca geográfica.
     * @param longitude Coordenada X do centro da busca geográfica.
     * @param raioMetros Distância máxima de alcance medida em metros.
     * @return Lista de ocorrências localizadas dentro do raio delimitado.
     */
    public List<Ocorrencia> buscarProximas(double latitude, double longitude, double raioMetros) {
        // Monta o ponto geográfico central da pesquisa respeitando a ordem (X = Longitude, Y = Latitude)
        Point pontoCentro = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        // Encaminha a consulta espacial para a camada de persistência nativa
        return repository.buscarPorProximidade(pontoCentro, raioMetros);
    }
}