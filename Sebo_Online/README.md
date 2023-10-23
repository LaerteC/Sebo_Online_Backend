<h1 align="center">
  Projeto User / Task
</h1>

<p align="center">
 <img src="https://img.shields.io/static/v1?label=Junit_API&message=Laerte&color=8257E5&labelColor=000000" alt="@laerte" />
 <img src="https://img.shields.io/static/v1?label=Tipo&message=Desafio&color=8257E5&labelColor=000000" alt="Desafio/Teste" />
</p>

API para Disponibilizar recursos para o front end ;



## Tecnologias
 
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [SpringDoc OpenAPI 3](https://springdoc.org/v2/#spring-webflux-support)
- [Postgresql](https://www.postgresql.org/download/)

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

A API poderá ser acessada em [localhost:8081](http://localhost:8081).
O Swagger poderá ser visualizado em [localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

## API Endpoints

Para fazer as requisições HTTP abaixo, foi utilizada a ferramenta [httpie](https://httpie.io):

- Criar Tarefa/User 
```
$ http POST :8081

[
  {
   
  }
]
```

- Listar 
```
$ http GET :8081

[
  {
  
  }
]
```

- Atualizar T
```
$ http PUT :8081

[
  {
 
  }
]
```

- Remover 
```


[ ]
```