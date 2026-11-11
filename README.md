# Sistema TIDE - Monitoramento Geográfico de Ocorrências

O **TIDE** é um sistema inteligente de registro e monitoramento de ocorrências geoespaciais, focado em mapear e simular áreas de risco urbano no município de Brumado, Bahia. Através da integração com dados de GPS e mapas interativos, a aplicação permite visualizar a densidade de incidentes em perímetros configuráveis pelo usuário.

---

## Tecnologias Utilizadas

### Back-end
- **Java 17** com **Spring Boot** (Data JPA, Web)
- **Hibernate Spatial** & **JTS (LocationTech)** para manipulação de geometrias espaciais
- **PostgreSQL** com a extensão **PostGIS** para armazenamento geográfico

### Front-end
- **Vue 3** (Composition API) com **Vite**
- **Axios** para consumo de API REST
- **Leaflet Maps** para renderização do mapa dinâmico via OpenStreetMap

---

## Base URL do Servidor (Render)

Todas as requisições para a API devem utilizar o prefixo:

https://tide-api-c6t2.onrender.com/tide-api


---

## Endpoints da API REST

A tabela abaixo detalha as rotas disponíveis no sistema para integração com o front-end ou testes via Postman:

| Método   | Endpoint                | Parâmetros / Corpo                             | Descrição     | Código HTTP de Retorno |
|   :---   |   :---                  |        :---                                    |      :---     |          :---          |
| **POST** | `/ocorrencias`          | `JSON (OcorrenciaDTO)` no Body                 | Cadastra uma nova ocorrência com coordenadas decimais de GPS. | `200 OK` |
| **GET**  | `/ocorrencias`          | Nenhum                                         | Lista absolutamente todas as ocorrências salvas na base, sem filtros. | `200 OK` |
| **GET**  | `/ocorrencias/{id}`     | `id` (na URL)                                  | Recupera de forma exclusiva os detalhes de uma ocorrência pelo seu ID único. | `200 OK` ou `404 Not Found` |
| **GET**  | `/ocorrencias/proximas` | `latitude`, `longitude`, `raio` (Query Params) | Endpoint espacial que localiza crimes dentro de um raio circular medido em metros. | `200 OK` |

### 💡 Exemplo de Chamada Espacial (Proximidade)

Para testar o JSON do radar geográfico diretamente no navegador ou no Postman focando no Centro de Brumado (raio de 5km):

https://tide-api-c6t2.onrender.com/tide-api/ocorrencias/proximas?latitude=-14.2045&longitude=-41.6644&raio=5000


## Configuração e Execução do Banco de Dados

O banco de dados utiliza tabelas nativas com suporte ao formato `geometry(Point, 4326)`. Para simular consultas de raio exatas em metros, as coordenadas cartesianas passam por uma conversão dinâmica na camada do repositório utilizando a função nativa do PostGIS:

    SELECT * FROM TB_OCORRENCIAS WHERE ST_DWithin(CAST(localizacao AS geography), CAST(:ponto AS geography), :raio)", nativeQuery = true)


---

## Como Executar o Projeto Localmente

### ☕ 1. Executando o Back-end (Spring Boot)

#### Pré-requisitos:
- **Java 17** instalado e configurado no PATH do sistema.
- **Maven** instalado (ou utilize o wrapper `./mvnw`).
- Banco de dados **PostgreSQL** com a extensão **PostGIS** ativada (ou utilize a conexão do Supabase configurada no `application.properties`).

#### Passos para rodar:
1. Abra o terminal na pasta raiz do seu projeto Back-end (onde fica o arquivo `pom.xml`).
2. Atualize as dependências e compile o projeto executando o comando:
   ```bash
   mvn clean install
   ```
3. Inicialize o servidor Spring Boot com o comando:
   ```bash
   mvn spring-boot:run
   ```
4. O servidor estará rodando localmente no endereço: `http://localhost:8080/tide-api`

---

### 2. Executando o Front-end (Vue 3 + Vite)

#### Pré-requisitos:
- **Node.js** (versão 18 ou superior recomendada) instalado.
- Gerenciador de pacotes **npm** ou **yarn**.

#### Passos para rodar:
1. Abra o terminal na pasta raiz do seu projeto Front-end (onde fica o arquivo `package.json`).
2. Instale todas as dependências necessárias (incluindo o Leaflet e o Axios) executando:
   ```bash
   npm install
   ```
3. Inicie o servidor de desenvolvimento local do Vite com o comando:
   ```bash
   npm run dev
   ```
4. O Vite informará uma URL no terminal (geralmente `http://localhost:5173/`). Abra este endereço no seu navegador para utilizar o sistema.

---

##  Dica para Testes Locais Integrais
Com ambos os servidores rodando em sua máquina, o front-end na porta `5173` enviará requisições para a porta `8080` do back-end. Graças à classe `CorsConfig.java` configurada no Java, a comunicação acontecerá de forma limpa e sem bloqueios de segurança do navegador.
