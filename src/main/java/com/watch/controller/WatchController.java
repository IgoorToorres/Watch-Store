package com.watch.controller;

import com.watch.dto.WatchFilterRequest;
import com.watch.dto.WatchPageResponse;
import com.watch.dto.WatchRequest;
import com.watch.dto.WatchResponse;
import com.watch.exception.ApiErrorResponse;
import com.watch.service.WatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/watches")
@Tag(
        name = "Watches",
        description = "Endpoints para cadastro, consulta, atualização, remoção, paginação, ordenação, busca e filtros de relógios."
)
public class WatchController {
    private final WatchService watchService;

    public WatchController(WatchService watchService){
        this.watchService = watchService;
    }

    @Operation(
            summary = "Criar relógio",
            description = "Cria um novo relógio no estoque. Os campos de enum devem ser enviados em formato de API: movementType como quartz, automatic ou manual; caseMaterial como steel, titanium, resin, bronze ou ceramic; crystalType como mineral, sapphire ou acrylic."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Relógio criado com sucesso.",
                    content = @Content(schema = @Schema(implementation = WatchResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida, erro de validação ou enum inválido.",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<WatchResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para cadastrar um relógio.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = WatchRequest.class),
                            examples = @ExampleObject(
                                    name = "Cadastro de relógio automático",
                                    value = """
                                            {
                                              "brand": "Seiko",
                                              "model": "Diver 200m",
                                              "reference": "SKX-Style",
                                              "movementType": "automatic",
                                              "caseMaterial": "steel",
                                              "crystalType": "mineral",
                                              "waterResistanceM": 200,
                                              "diameterMm": 42,
                                              "lugToLugMm": 46,
                                              "thicknessMm": 13,
                                              "lugWidthMm": 22,
                                              "priceInCents": 159990
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody WatchRequest request
    ){
        WatchResponse response = watchService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Listar relógios",
            description = "Lista relógios com paginação, ordenação, busca textual e filtros combináveis. A busca textual procura em brand, model e reference."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista paginada retornada com sucesso.",
                    content = @Content(schema = @Schema(implementation = WatchPageResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parâmetro inválido, paginação inválida, ordenação inválida ou faixa de filtros inconsistente.",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<WatchPageResponse> listAll(
            @Parameter(
                    description = "Página atual. A primeira página é 1.",
                    example = "1",
                    in = ParameterIn.QUERY
            )
            @RequestParam(defaultValue = "1") Integer page,

            @Parameter(
                    description = "Quantidade de itens por página. Valor máximo permitido: 60.",
                    example = "12",
                    in = ParameterIn.QUERY
            )
            @RequestParam(defaultValue = "12") Integer perPage,

            @Parameter(
                    description = "Ordenação da listagem. Valores aceitos: newest, price_asc, price_desc, diameter_asc, wr_desc.",
                    example = "newest",
                    in = ParameterIn.QUERY
            )
            @RequestParam(defaultValue = "newest") String sort,

            @Parameter(
                    description = "Busca textual aplicada em brand, model e reference.",
                    example = "seiko",
                    in = ParameterIn.QUERY
            )
            @RequestParam(required = false) String search,

            @Parameter(
                    description = "Filtro por marca, ignorando maiúsculas e minúsculas.",
                    example = "Seiko",
                    in = ParameterIn.QUERY
            )
            @RequestParam(required = false) String brand,

            @Parameter(
                    description = "Filtro por tipo de movimento. Valores aceitos: quartz, automatic, manual.",
                    example = "automatic",
                    in = ParameterIn.QUERY
            )
            @RequestParam(required = false) String movementType,

            @Parameter(
                    description = "Filtro por material da caixa. Valores aceitos: steel, titanium, resin, bronze, ceramic.",
                    example = "steel",
                    in = ParameterIn.QUERY
            )
            @RequestParam(required = false) String caseMaterial,

            @Parameter(
                    description = "Filtro por tipo de vidro. Valores aceitos: mineral, sapphire, acrylic.",
                    example = "sapphire",
                    in = ParameterIn.QUERY
            )
            @RequestParam(required = false) String crystalType,

            @Parameter(
                    description = "Resistência à água mínima em metros.",
                    example = "100",
                    in = ParameterIn.QUERY
            )
            @RequestParam(required = false) Integer waterResistanceMin,

            @Parameter(
                    description = "Resistência à água máxima em metros.",
                    example = "300",
                    in = ParameterIn.QUERY
            )
            @RequestParam(required = false) Integer waterResistanceMax,

            @Parameter(
                    description = "Preço mínimo em centavos.",
                    example = "50000",
                    in = ParameterIn.QUERY
            )
            @RequestParam(required = false) Long priceMin,

            @Parameter(
                    description = "Preço máximo em centavos.",
                    example = "200000",
                    in = ParameterIn.QUERY
            )
            @RequestParam(required = false) Long priceMax,

            @Parameter(
                    description = "Diâmetro mínimo da caixa em milímetros.",
                    example = "38",
                    in = ParameterIn.QUERY
            )
            @RequestParam(required = false) Integer diameterMin,

            @Parameter(
                    description = "Diâmetro máximo da caixa em milímetros.",
                    example = "42",
                    in = ParameterIn.QUERY
            )
            @RequestParam(required = false) Integer diameterMax
    ){
        WatchFilterRequest filters = new WatchFilterRequest(
                search,
                brand,
                movementType,
                caseMaterial,
                crystalType,
                waterResistanceMin,
                waterResistanceMax,
                priceMin,
                priceMax,
                diameterMin,
                diameterMax
        );

        WatchPageResponse response = watchService.list(page, perPage, sort, filters);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Buscar relógio por ID",
            description = "Retorna os detalhes de um relógio específico pelo identificador UUID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Relógio encontrado.",
                    content = @Content(schema = @Schema(implementation = WatchResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "UUID inválido.",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Relógio não encontrado.",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<WatchResponse> findById(
            @Parameter(description = "ID do relógio.", example = "b7c1a1a6-3b59-4d09-8a5a-9b2ed13d72d9")
            @PathVariable UUID id
    ){
        WatchResponse response = watchService.findById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Atualizar relógio",
            description = "Atualiza todos os campos editáveis de um relógio existente. O ID, createdAt e updatedAt são controlados pela aplicação."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Relógio atualizado com sucesso.",
                    content = @Content(schema = @Schema(implementation = WatchResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida, erro de validação, UUID inválido ou enum inválido.",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Relógio não encontrado.",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<WatchResponse> update(
            @Parameter(description = "ID do relógio.", example = "b7c1a1a6-3b59-4d09-8a5a-9b2ed13d72d9")
            @PathVariable UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados completos para atualização do relógio.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = WatchRequest.class),
                            examples = @ExampleObject(
                                    name = "Atualização de relógio automático",
                                    value = """
                                            {
                                              "brand": "Seiko",
                                              "model": "Diver 200m Rev 2",
                                              "reference": "SKX-Style",
                                              "movementType": "automatic",
                                              "caseMaterial": "steel",
                                              "crystalType": "mineral",
                                              "waterResistanceM": 200,
                                              "diameterMm": 42,
                                              "lugToLugMm": 46,
                                              "thicknessMm": 13,
                                              "lugWidthMm": 22,
                                              "priceInCents": 169990
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody WatchRequest request
    ){
        WatchResponse response = watchService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Remover relógio",
            description = "Remove um relógio existente pelo identificador UUID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Relógio removido com sucesso.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "UUID inválido.",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Relógio não encontrado.",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do relógio.", example = "b7c1a1a6-3b59-4d09-8a5a-9b2ed13d72d9")
            @PathVariable UUID id
    ){
        watchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
