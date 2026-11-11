# =========================================================================
# Etapa 1: Compilação (Build) utilizando a imagem oficial do Maven
# =========================================================================
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copia o arquivo de configuração e descarrega as dependências (otimiza a cache do Render)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código-fonte da aplicação
COPY src ./src

# Compila o projeto Spring Boot 4.1.2 gerando o ficheiro .jar
RUN mvn clean package -DskipTests -e -X

# =========================================================================
# Etapa 2: Execução (Runtime leve)
# =========================================================================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copia automaticamente o JAR gerado na etapa anterior (independente do nome do artefacto)
COPY --from=build /app/target/*.jar app.jar

# Configura o Spring Boot para escutar a porta que o Render atribuir dinamicamente
ENV SERVER_PORT=${PORT:-8080}
EXPOSE 8080

# Inicia a aplicação
CMD ["java", "-jar", "app.jar"]