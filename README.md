<h1 align="center">
  Projeto TCC / SeboOnline
</h1>

<p align="center">
 <img src="https://img.shields.io/static/v1?label=SeboOnline&message=SistemaWeb&color=8257E5&labelColor=000000" alt="@laerte" />
 <img src="https://img.shields.io/static/v1?label=UFPR&message=TCC&color=8257E5&labelColor=000000" alt="Desafio/Teste" />
</p>

<p align="center">
 <img src="https://img.shields.io/static/v1?label=BACKEND&message=SPRING&color=8257E5&labelColor=000000" alt="@laerte" />
</p>

API para Disponibilizar recursos para o front end ;

## Equipe :
<ul>
  
  <li>Laerte Souza</li>

  <li>Orientador :  Professor Doutor Dieval Guizelini</li>
</ul>


## Tecnologias
 
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [SpringDoc OpenAPI 3](https://springdoc.org/v2/#spring-webflux-support)
- [Postgresql](https://www.postgresql.org/download/)

## Banco de Dados Postgresql

O banco de dados utiliza a API JPA do Java de persistência.
- JAVA JPA HIBERNATE

## **Práticas adotadas**

- SOLID, DRY, YAGNI, KISS
- API REST
- Consultas com Spring Data JPA
- Injeção de Dependências
- Tratamento de respostas de erro
- Geração automática do Swagger com a OpenAPI 3

## Como Executar

- Clonar repositório git
- Construir o projeto:
```
$ ./mvnw clean package
```
- Executar a aplicação:
```
$ java -jar target/todolist-0.0.1-SNAPSHOT.jar
```


- Executar a aplicação 2 :
```
$ spring-boot:run
```

A API poderá ser acessada em [localhost:8081](http://localhost:8081).
O Swagger poderá ser visualizado em [localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

## API Endpoints

Para fazer as requisições HTTP abaixo, foi utilizada a ferramenta [httpie](https://httpie.io):


![image](https://github.com/LaerteC/Sebo_Online_Backend/assets/66754738/7e5edc1a-4612-4341-9a80-6a00de65eb79)

