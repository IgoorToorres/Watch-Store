# Desafio Backend (Spring Boot) - Controle de Estoque de Relógios

API REST para controle de estoque de relógios, implementada com **Spring Boot**, **PostgreSQL** e arquitetura em **camadas tradicionais**:

✅ `entity` → `repository` → `service` → `controller`

✅ DTO + Mapper

✅ Paginação + busca + filtros + ordenação

✅ CRUD completo (criar / atualizar / remover)

✅ Validação + tratamento global de erros (`@ControllerAdvice`)

✅ Seed inicial para teste rápido

---

## Tecnologias

- Java 17+
- Spring Boot (Web, Data JPA, Validation)
- PostgreSQL
- Lombok

---

## Arquitetura

Organização por responsabilidade:

```
com.market.watchapi
  entity/
  repository/
  service/
  controller/
  dto/
  mapper/
  config/
  exception/
```

### Responsabilidades

- **entity**: entidades JPA e enums
- **repository**: acesso a dados (Spring Data JPA)
- **service**: regras de listagem, filtros, ordenação e CRUD
- **dto**: contratos de entrada/saída da API
- **mapper**: conversão `Entity -> DTO` + campos calculados
- **controller**: endpoints REST
- **exception**: exceções de domínio + handler global
- **config**: seed de dados iniciais

---

## Funcionalidades

### 1) Listagem de relógios com filtros

- Paginação (`pagina`, `porPagina`)
- Busca textual (`busca`) em `marca`, `modelo`, `referencia`
- Filtros combináveis:
    - `marca`
    - `tipoMovimento` (quartz | automatic | manual)
    - `materialCaixa` (steel | titanium | resin | bronze | ceramic)
    - `tipoVidro` (mineral | sapphire | acrylic)
    - faixa de resistência: `resistenciaMin`, `resistenciaMax`
    - faixa de preço: `precoMin`, `precoMax`
    - faixa de diâmetro: `diametroMin`, `diametroMax`
- Ordenação (`ordenar`)
    - `newest` (mais recentes)
    - `price_asc`
    - `price_desc`
    - `diameter_asc`
    - `wr_desc`

### 2) Detalhe por ID

Retorna um relógio específico.

### 3) CRUD

- Criar relógio
- Atualizar relógio
- Remover relógio

---

## Endpoints

Base URL:

```
http://localhost:8080
```

### Listar relógios

**GET** `/api/relogios`

### Query params

| Parâmetro | Tipo | Padrão | Descrição |
| --- | --- | --- | --- |
| `pagina` | int | `1` | Página atual |
| `porPagina` | int | `12` | Itens por página (**máx 60**) |
| `busca` | string | - | Busca em marca/modelo/referência |
| `marca` | string | - | Filtra por marca (case-insensitive) |
| `tipoMovimento` | string | - | `quartz` | `automatic` | `manual` |
| `materialCaixa` | string | - | `steel` | `titanium` | `resin` | `bronze` | `ceramic` |
| `tipoVidro` | string | - | `mineral` | `sapphire` | `acrylic` |
| `resistenciaMin` | int | - | Resistência mínima (m) |
| `resistenciaMax` | int | - | Resistência máxima (m) |
| `precoMin` | long | - | Preço mínimo (centavos) |
| `precoMax` | long | - | Preço máximo (centavos) |
| `diametroMin` | int | - | Diâmetro mínimo (mm) |
| `diametroMax` | int | - | Diâmetro máximo (mm) |
| `ordenar` | string | `newest` | `newest`, `price_asc`, `price_desc`, `diameter_asc`, `wr_desc` |

### Exemplos

```bash
curl"http://localhost:8080/api/relogios?pagina=1&porPagina=12"
curl"http://localhost:8080/api/relogios?busca=seiko"
curl"http://localhost:8080/api/relogios?tipoMovimento=automatic&tipoVidro=mineral"
curl"http://localhost:8080/api/relogios?resistenciaMin=100&resistenciaMax=250"
curl"http://localhost:8080/api/relogios?precoMin=50000&precoMax=200000"
curl"http://localhost:8080/api/relogios?diametroMin=38&diametroMax=42"
curl"http://localhost:8080/api/relogios?ordenar=price_desc"

```

### Response

```json
{
  "itens": [
    {
      "id": "b7c1a1a6-3b59-4d09-8a5a-9b2ed13d72d9",
      "marca": "Seiko",
      "modelo": "Diver 200m",
      "referencia": "SKX-Style",
      "tipoMovimento": "automatic",
      "materialCaixa": "steel",
      "tipoVidro": "mineral",
      "resistenciaAguaM": 200,
      "diametroMm": 42,
      "lugToLugMm": 46,
      "espessuraMm": 13,
      "larguraLugMm": 22,
      "precoEmCentavos": 159990,
      "urlImagem": "https://picsum.photos/seed/relogio2/800/800",
      "etiquetaResistenciaAgua": "mergulho",
      "pontuacaoColecionador": 73
    }
  ],
  "total": 3
}
```

---

### Buscar por ID

**GET** `/api/relogios/{id}`

```bash
curl"http://localhost:8080/api/relogios/b7c1a1a6-3b59-4d09-8a5a-9b2ed13d72d9"
```

Response: `RelogioDto`

---

### Criar relógio

**POST** `/api/relogios`

```bash
curl -X POST "http://localhost:8080/api/relogios" \
  -H "Content-Type: application/json" \
  -d '{
    "marca":"Orient",
    "modelo":"Bambino",
    "referencia":"RA-AC0M01S10B",
    "tipoMovimento":"automatic",
    "materialCaixa":"steel",
    "tipoVidro":"mineral",
    "resistenciaAguaM":30,
    "diametroMm":40,
    "lugToLugMm":46,
    "espessuraMm":12,
    "larguraLugMm":21,
    "precoEmCentavos":149990,
    "urlImagem":"https://picsum.photos/seed/relogio4/800/800"
  }'
```

---

### Atualizar relógio

**PUT** `/api/relogios/{id}`

```bash
curl -X PUT "http://localhost:8080/api/relogios/b7c1a1a6-3b59-4d09-8a5a-9b2ed13d72d9" \
  -H "Content-Type: application/json" \
  -d '{
    "marca":"Seiko",
    "modelo":"Diver 200m (Rev 2)",
    "referencia":"SKX-Style",
    "tipoMovimento":"automatic",
    "materialCaixa":"steel",
    "tipoVidro":"mineral",
    "resistenciaAguaM":200,
    "diametroMm":42,
    "lugToLugMm":46,
    "espessuraMm":13,
    "larguraLugMm":22,
    "precoEmCentavos":169990,
    "urlImagem":"https://picsum.photos/seed/relogio2/800/800"
  }'

```

---

### 📍 Remover relógio

**DELETE** `/api/relogios/{id}`

```bash
curl -X DELETE"http://localhost:8080/api/relogios/b7c1a1a6-3b59-4d09-8a5a-9b2ed13d72d9"

```

---

## Campos calculados (Service/Mapper)

O `RelogioDto` inclui 2 campos calculados no `mapper`:

### `etiquetaResistenciaAgua`

Classificação baseada em `resistenciaAguaM`:

- `< 50` → `respingos`
- `50–99` → `uso_diario`
- `100–199` → `natacao`
- `>= 200` → `mergulho`

### `pontuacaoColecionador`

Pontuação simples para enriquecer o DTO, baseada em:

- vidro safira (+25)
- resistência >= 100 (+15)
- resistência >= 200 (bônus +10)
- movimento automático (+20)
- caixa aço (+10) / titânio (+12)
- diâmetro “clássico” 38–42mm (+8)

---

## Tratamento de erros

### 404 — Não encontrado

Quando o ID não existe:

```json
{
"timestamp":"2026-01-15T22:00:00Z",
"status":404,
"erro":"Não encontrado",
"mensagem":"Relógio não encontrado: <id>",
"caminho":"/api/relogios/<id>",
"errosDeCampo":[]
}

```

### 400 — Erro de validação

```json
{
"timestamp":"2026-01-15T22:00:00Z",
"status":400,
"erro":"Requisição inválida",
"mensagem":"Erro de validação",
"caminho":"/api/relogios",
"errosDeCampo":[
{"campo":"marca","mensagem":"must not be blank"}
]
}

```

### 400 — Parâmetro inválido (enum)

Ex.: `tipoMovimento=turbo`

```json
{
"timestamp":"2026-01-15T22:00:00Z",
"status":400,
"erro":"Requisição inválida",
"mensagem":"Tipo de movimento inválido: turbo",
"caminho":"/api/relogios",
"errosDeCampo":[]
}

```

---

## Dados iniciais (Seed)

Ao iniciar, se a tabela estiver vazia, o projeto cria alguns relógios de exemplo automaticamente (`CarregadorDadosInicial`).

---

## Decisões técnicas

- **DTO + Mapper manual**: controle total, simples para desafio e sem dependências extras.
- **Specifications (`JpaSpecificationExecutor`)**: filtros combináveis e escaláveis sem virar um “if-else gigante”.
- **Service centraliza regras**: controller fica fino e previsível.
- **`porPagina` limitado a 60**: evita abuso de payload e dá maturidade ao desafio.
- **Enums com `fromApi/toApi`**: mantém a API consistente e evita “string solta” no banco.