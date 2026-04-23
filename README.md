# Calculadora de Materiais para Obra Residencial

Projeto em Java + Spring Boot para atender a atividade avaliativa de Desenvolvimento de Sistemas — UniCEUB.

---

## Funcionalidades

- Modelagem da planta baixa como grafo G=(V,A): vértices = pilares, arestas = paredes
- Cálculo do volume de concreto para vigas baldrame (Etapa 2)
- Cálculo da quantidade de tijolos nas paredes (Etapa 3)
- API REST organizada em camadas (Controller → Service → Repository)
- Documentação automática via Swagger/OpenAPI
- Testes automatizados com JUnit 5

---

## Tecnologias

- Java 17
- Spring Boot 3.2.5
- Spring Web
- Spring Data JPA
- Spring Validation
- H2 Database (em memória)
- Swagger / SpringDoc OpenAPI 2.5.0
- Lombok
- Maven

---

## Como executar

```bash
# Clone o repositório
git clone <URL_DO_REPOSITORIO>
cd calculadora-materiais

# Execute com Maven
./mvnw spring-boot:run
```

A aplicação sobe em:

- **API:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **H2 Console:** http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:calculadoradb`
  - Usuário: `sa` | Senha: *(vazia)*

---

## Endpoints

### 1) Calcular volume de concreto

```
POST /api/materiais/concreto
```

**Fórmula:** `Volume = Largura × AlturaViga × Comprimento` (por aresta)

**Exemplo de requisição:**
```json
{
  "arestas": [
    {
      "nome": "a12",
      "verticeOrigem": "V1",
      "verticeDestino": "V2",
      "comprimento": 5.0,
      "largura": 0.15,
      "altura": 2.8,
      "tipoAbertura": "NENHUMA"
    },
    {
      "nome": "a23",
      "verticeOrigem": "V2",
      "verticeDestino": "V3",
      "comprimento": 4.0,
      "largura": 0.15,
      "altura": 2.8,
      "tipoAbertura": "JANELA",
      "alturaAbertura": 1.2,
      "comprimentoAbertura": 1.0
    }
  ],
  "alturaViga": 0.40
}
```

**Exemplo de resposta:**
```json
{
  "volumeTotalM3": 0.540,
  "detalhes": [
    { "aresta": "a12", "largura": 0.15, "alturaViga": 0.40, "comprimento": 5.0, "volumeM3": 0.300 },
    { "aresta": "a23", "largura": 0.15, "alturaViga": 0.40, "comprimento": 4.0, "volumeM3": 0.240 }
  ]
}
```

---

### 2) Calcular quantidade de tijolos

```
POST /api/materiais/tijolos
```

**Fórmula:**
- Área da parede = Comprimento × Altura
- Área da abertura = ComprimentoAbertura × AlturaAbertura (se houver)
- Área líquida = Área da parede − Área da abertura
- Área do tijolo = (CompTijolo + Junta) × (AltTijolo + Junta)
- Quantidade = ⌈Área líquida / Área do tijolo⌉

**Exemplo de requisição:**
```json
{
  "arestas": [
    {
      "nome": "a12",
      "verticeOrigem": "V1",
      "verticeDestino": "V2",
      "comprimento": 5.0,
      "largura": 0.15,
      "altura": 2.8,
      "tipoAbertura": "PORTA",
      "alturaAbertura": 2.1,
      "comprimentoAbertura": 0.9
    }
  ],
  "alturaTijolo": 0.057,
  "comprimentoTijolo": 0.19,
  "larguraTijolo": 0.09,
  "espessuraJunta": 0.01
}
```

**Exemplo de resposta:**
```json
{
  "quantidadeTotalTijolos": 1247,
  "detalhes": [
    {
      "aresta": "a12",
      "areaParede": 14.0,
      "areaAberturas": 1.89,
      "areaLiquida": 12.11,
      "areaTijolo": 0.0134,
      "quantidadeTijolos": 904
    }
  ]
}
```

---

## Estrutura do Projeto

```
src/
└── main/
    └── java/com/obras/calculadora/
        ├── CalculadoraApplication.java   # Ponto de entrada
        ├── SwaggerConfig.java            # Configuração OpenAPI
        ├── model/
        │   ├── Vertice.java              # Vértice do grafo (pilar)
        │   ├── Aresta.java               # Aresta do grafo (parede)
        │   ├── Comodo.java               # Cômodo da planta
        │   └── TipoAbertura.java         # Enum: JANELA, PORTA, NENHUMA
        ├── dto/
        │   └── MateriaisDTO.java         # Records de request/response
        ├── repository/
        │   ├── VerticeRepository.java
        │   ├── ArestaRepository.java
        │   └── ComodoRepository.java
        ├── service/
        │   ├── ConcreteService.java      # Lógica Etapa 2
        │   └── TijoloService.java        # Lógica Etapa 3
        └── controller/
            ├── MateriaisController.java  # Endpoints REST
            └── GlobalExceptionHandler.java
└── test/
    └── java/com/obras/calculadora/
        ├── ConcreteServiceTest.java
        └── TijoloServiceTest.java
```
