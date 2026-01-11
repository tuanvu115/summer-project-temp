//package com.summer.presentation.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.summer.application.exception.TransactionNotFoundException;
//import com.summer.application.usecase.CreateTransactionUseCase;
//import com.summer.application.usecase.GetTransactionUseCase;
//import com.summer.domain.enums.AccountType;
//import com.summer.domain.enums.PaymentChannel;
//import com.summer.domain.enums.TransactionStatus;
//import com.summer.domain.enums.TransactionType;
//import com.summer.domain.model.AccountInfo;
//import com.summer.domain.model.ReferenceInfo;
//import com.summer.domain.model.TransactionInfo;
//import com.summer.presentation.dto.AccountInfoDTO;
//import com.summer.presentation.dto.ReferenceInfoDTO;
//import com.summer.presentation.dto.TransactionInfoDTO;
//import com.summer.presentation.mapper.PRTransactionMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
///**
// * Controller tests for TransactionController
// * Using Spring Boot Test with MockMvc for testing the web layer
// */
//@WebMc
//@DisplayName("Transaction Controller API Tests")
//class TransactionInfoControllerTest {
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockitoBean
//    private CreateTransactionUseCase createTransactionUseCase;
//
//    @MockitoBean
//    private GetTransactionUseCase getTransactionUseCase;
//
//    @MockitoBean
//    private PRTransactionMapper mapper;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//    }
//
//
//    @Nested
//    @DisplayName("POST /transactions - Create Transaction")
//    class CreateTransactionTests {
//
//        @Test
//        @DisplayName("Should create transaction successfully with all fields")
//        void shouldCreateTransactionSuccessfully() throws Exception {
//            UUID transactionId = UUID.randomUUID();
//            LocalDateTime now = LocalDateTime.now();
//
//            AccountInfoDTO fromAccountDTO = new AccountInfoDTO("ACC001", "John Doe", AccountType.CUSTOMER);
//            AccountInfoDTO toAccountDTO = new AccountInfoDTO("ACC002", "Jane Smith", AccountType.CUSTOMER);
//            ReferenceInfoDTO referenceDTO = new ReferenceInfoDTO("CLI-123", "EXT-456");
//
//            Map<String, Object> metadata = new HashMap<>();
//            metadata.put("source", "mobile");
//            metadata.put("version", "1.0");
//
//            TransactionInfoDTO requestDTO = new TransactionInfoDTO(
//                transactionId, TransactionType.TRANSFER, new BigDecimal("100.50"), "USD",
//                null,"00000001",
//                fromAccountDTO, toAccountDTO, "MOBILE", PaymentChannel.CITAD,
//                referenceDTO, "Transfer to Jane", TransactionStatus.PENDING, now, null, metadata
//            );
//
//            TransactionInfo domainModel = TransactionInfo.builder()
//                    .id(transactionId)
//                    .type(TransactionType.TRANSFER)
//                    .amount(new BigDecimal("100.50"))
//                    .currency("USD")
//                    .trxDate(null)
//                    .relationNo("00000001")
//                    .fromAccount(new AccountInfo("ACC001", "John Doe", AccountType.CUSTOMER))
//                    .toAccount(new AccountInfo("ACC002", "Jane Smith", AccountType.CUSTOMER))
//                    .channel("MOBILE")
//                    .paymentChannel(PaymentChannel.CITAD)
//                    .reference(new ReferenceInfo("CLI-123", "EXT-456"))
//                    .description("Transfer to Jane")
//                    .status(TransactionStatus.PENDING)
//                    .createdAt(now)
//                    .processedAt(null)
//                    .metadata(metadata)
//                    .build();
//
//
//            when(mapper.toDomain(any(TransactionInfoDTO.class))).thenReturn(domainModel);
//            when(createTransactionUseCase.execute(any(TransactionInfo.class))).thenReturn(domainModel);
//            when(mapper.toDTO(any(TransactionInfo.class))).thenReturn(requestDTO);
//
//            mockMvc.perform(post("/transactions")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(requestDTO)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(transactionId.toString()))
//                .andExpect(jsonPath("$.type").value("TRANSFER"))
//                .andExpect(jsonPath("$.amount").value(100.50))
//                .andExpect(jsonPath("$.currency").value("USD"))
//                .andExpect(jsonPath("$.fromAccount.accountId").value("ACC001"))
//                .andExpect(jsonPath("$.toAccount.accountId").value("ACC002"))
//                .andExpect(jsonPath("$.status").value("PENDING"));
//
//            verify(mapper).toDomain(any(TransactionInfoDTO.class));
//            verify(createTransactionUseCase).execute(any(TransactionInfo.class));
//            verify(mapper).toDTO(any(TransactionInfo.class));
//        }
//
//        @Test
//        @DisplayName("Should create transaction with minimal fields")
//        void shouldCreateTransactionWithMinimalFields() throws Exception {
//            UUID transactionId = UUID.randomUUID();
//            LocalDateTime now = LocalDateTime.now();
//
//            TransactionInfoDTO requestDTO = new TransactionInfoDTO(
//                transactionId, TransactionType.TRANSFER, new BigDecimal("50.00"), "USD", null, "00000002",
//                new AccountInfoDTO("ACC001", "John", AccountType.CUSTOMER),
//                new AccountInfoDTO("ACC002", "Jane", AccountType.CUSTOMER),
//                null, null, null, null, TransactionStatus.PENDING, now, null, null
//            );
//
//            TransactionInfo domainModel = TransactionInfo.builder()
//                    .id(transactionId)
//                    .type(TransactionType.TRANSFER)
//                    .amount(new BigDecimal("50.00"))
//                    .currency("USD")
//                    .trxDate(null)
//                    .relationNo("00000002")
//                    .fromAccount(new AccountInfo("ACC001", "John", AccountType.CUSTOMER))
//                    .toAccount(new AccountInfo("ACC002", "Jane", AccountType.CUSTOMER))
//                    .channel(null)
//                    .paymentChannel(null)
//                    .reference(null)
//                    .description(null)
//                    .status(TransactionStatus.PENDING)
//                    .createdAt(now)
//                    .processedAt(null)
//                    .metadata(null)
//                    .build();
//
//            when(mapper.toDomain(any(TransactionInfoDTO.class))).thenReturn(domainModel);
//            when(createTransactionUseCase.execute(any(TransactionInfo.class))).thenReturn(domainModel);
//            when(mapper.toDTO(any(TransactionInfo.class))).thenReturn(requestDTO);
//
//            mockMvc.perform(post("/transactions")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(transactionId.toString()))
//                .andExpect(jsonPath("$.amount").value(50.00));
//
//            verify(createTransactionUseCase).execute(any(TransactionInfo.class));
//        }
//    }
//
//    @Nested
//    @DisplayName("GET /transactions/{id} - Get Transaction")
//    class GetTransactionTests {
//
//        @Test
//        @DisplayName("Should get transaction by ID successfully")
//        void shouldGetTransactionById() throws Exception {
//            UUID transactionId = UUID.randomUUID();
//            LocalDateTime now = LocalDateTime.now();
//
//            TransactionInfo domainModel = TransactionInfo.builder()
//                    .id(transactionId)
//                    .type(TransactionType.TRANSFER)
//                    .amount(new BigDecimal("100.00"))
//                    .currency("USD")
//                    .trxDate(null)
//                    .relationNo("00000001")
//                    .fromAccount(new AccountInfo("ACC001", "John Doe", AccountType.CUSTOMER))
//                    .toAccount(new AccountInfo("ACC002", "Jane Smith", AccountType.CUSTOMER))
//                    .channel(null)
//                    .paymentChannel(null)
//                    .reference(null)
//                    .description(null)
//                    .status(TransactionStatus.COMPLETED)
//                    .createdAt(now)
//                    .processedAt(now.plusMinutes(5))
//                    .metadata(null)
//                    .build();
//
//            TransactionInfoDTO responseDTO = new TransactionInfoDTO(
//                transactionId, TransactionType.TRANSFER, new BigDecimal("100.00"), "USD", null, "00000001",
//                new AccountInfoDTO("ACC001", "John Doe", AccountType.CUSTOMER),
//                new AccountInfoDTO("ACC002", "Jane Smith", AccountType.CUSTOMER),
//                null, null, null, null, TransactionStatus.COMPLETED, now, now.plusMinutes(5), null
//            );
//
//            when(getTransactionUseCase.execute(transactionId)).thenReturn(domainModel);
//            when(mapper.toDTO(any(TransactionInfo.class))).thenReturn(responseDTO);
//
//            mockMvc.perform(get("/transactions/{id}", transactionId)
//                    .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(transactionId.toString()))
//                .andExpect(jsonPath("$.type").value("TRANSFER"))
//                .andExpect(jsonPath("$.amount").value(100.00))
//                .andExpect(jsonPath("$.status").value("COMPLETED"));
//
//            verify(getTransactionUseCase).execute(transactionId);
//            verify(mapper).toDTO(any(TransactionInfo.class));
//        }
//
//        @Test
//        @DisplayName("Should return 404 when transaction not found")
//        void shouldReturn404WhenTransactionNotFound() throws Exception {
//            UUID transactionId = UUID.randomUUID();
//            when(getTransactionUseCase.execute(transactionId))
//                .thenThrow(new TransactionNotFoundException(transactionId));
//
//            mockMvc.perform(get("/transactions/{id}", transactionId)
//                    .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value(containsString("Transaction not found")));
//
//            verify(getTransactionUseCase).execute(transactionId);
//            verify(mapper, never()).toDTO(any(TransactionInfo.class));
//        }
//
//        @Test
//        @DisplayName("Should handle invalid UUID format")
//        void shouldHandleInvalidUUID() throws Exception {
//            mockMvc.perform(get("/transactions/{id}", "invalid-uuid")
//                    .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//
//            verify(getTransactionUseCase, never()).execute(any(UUID.class));
//        }
//    }
//}
