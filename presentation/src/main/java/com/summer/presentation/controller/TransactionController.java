package com.summer.presentation.controller;

import com.summer.application.exception.TransactionNotFoundException;
import com.summer.application.usecase.CreateTransactionUseCase;
import com.summer.application.usecase.GetTransactionUseCase;
import com.summer.domain.model.TransactionInfo;
import com.summer.presentation.dto.ErrorResponse;
import com.summer.presentation.dto.TransactionInfoDTO;
import com.summer.presentation.mapper.PRTransactionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for Transaction operations.
 * Presentation layer - handles HTTP requests/responses.
 */
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "Transaction management APIs")
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final GetTransactionUseCase getTransactionUseCase;
    private final PRTransactionMapper mapper;


    @PostMapping
    @Operation(summary = "Create a new transaction", description = "Creates a new transaction and returns the created transaction details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully",
                    content = @Content(schema = @Schema(implementation = TransactionInfoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<TransactionInfoDTO> createTransaction(
            @Valid @RequestBody TransactionInfoDTO request) {
        TransactionInfo transactionInfo = createTransactionUseCase.execute(mapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(transactionInfo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID", description = "Returns a transaction by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found",
                    content = @Content(schema = @Schema(implementation = TransactionInfoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<TransactionInfoDTO> getTransaction(@PathVariable UUID id) {
        TransactionInfo transactionInfo = getTransactionUseCase.execute(id);
        return ResponseEntity.ok(mapper.toDTO(transactionInfo));
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(TransactionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(ex.getMessage()));
    }



}
