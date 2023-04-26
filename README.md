# Aplicação para fins de aprendizagem das melhores praticas de desenvolvimento utilizando Java vs Springboot 3
Aqui temos uma aplicação completa utilizando java, spring boot 3, spring data, spring security, spring doc, testes unitários e integrados, tudo utilizando as melhores praticas seguindo os cursos do canal https://www.youtube.com/@DevDojoBrasil


### Tecnologias utilizadas
* Java 19
* Spring boot 3
* Spring Security 6.0
* Spring doc
* Spring data
* JPA
* JUnit

### Como rodar o projeto
Precisamos apenas rodar o comando `docker-compose up` utilizando o yml abaixo

```yml
version: '3.7'
services:
  db:
    image: mysql
    container_name: db
    environment:
      MYSQL_ROOT_PASSWORD: root
    ports:
    - "3306:3306"
    volumes:
      - devdojo_data:/var/lib/mysql
    networks:
      - login-network

  prometheus:
    image: prom/prometheus:latest
    container_name: prometheus
    volumes:
      - "./src/main/resources/prometheus.yml:/etc/prometheus/prometheus.yml"
    command:
      - "--config.file=/etc/prometheus/prometheus.yml"
    ports:
      - "9090:9090"
    networks:
      - login-network

  grafana:
    image: grafana/grafana:latest
    container_name: grafana
    ports:
      - "3000:3000"
    networks:
      - login-network

  app:
    image: kaillyarruda/springboot2-essentials:latest
    container_name: app
    ports:
      - 8080:8080
    restart: unless-stopped
    depends_on:
      - db
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/anime?createDatabaseIfNotExist=true
    networks:
      - login-network

volumes:
  devdojo_data:

networks:
  login-network:
    driver: bridge
    name: login-network
``` 

### Documentação de Apis
Todas as apis estão documentadas utilizando swagger o caminho para acessar a documentação é: http://localhost:8080/swagger-ui.html

![image](https://user-images.githubusercontent.com/122119589/234647905-a0451b23-7052-4a68-b85d-79914a85ca4b.png)

### Métricas
Nesse projeto estamos utilizando Prometheus e Grafana para exibir as métricas que são exportadas pelo Actuator.
O caminho para acessar o Grafana é http://localhost:3000

![image](https://user-images.githubusercontent.com/122119589/234648232-abbc9428-8795-4ad6-a301-d58911421b1e.png)

