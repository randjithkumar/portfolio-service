package com.peplatform.portfolioservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peplatform.portfolioservice.common.enums.InvestmentStatus;
import com.peplatform.portfolioservice.dto.request.InvestmentRequest;
import com.peplatform.portfolioservice.dto.response.InvestmentResponse;
import com.peplatform.portfolioservice.service.api.InvestmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class InvestmentControllerTest {

    @Mock private InvestmentService investmentService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().findAndRegisterModules();
        mockMvc = MockMvcBuilders
                .standaloneSetup(new InvestmentController(investmentService))
                .build();
    }

    @Test
    void createsInvestment() throws Exception {
        InvestmentResponse response = response(101L);
        when(investmentService.createInvestment(any())).thenReturn(response);

        InvestmentRequest request = new InvestmentRequest();
        request.setInvestorId(11L);
        request.setFundId(21L);
        request.setAmount(new BigDecimal("250000.00"));

        mockMvc.perform(post("/api/investments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Investment created successfully."))
                .andExpect(jsonPath("$.data.id").value(101))
                .andExpect(jsonPath("$.data.status").value("PENDING"));

        verify(investmentService).createInvestment(any(InvestmentRequest.class));
    }

    @Test
    void rejectsInvalidCreateRequestBeforeCallingService() throws Exception {
        InvestmentRequest request = new InvestmentRequest();
        request.setInvestorId(11L);
        request.setFundId(21L);
        request.setAmount(BigDecimal.ZERO);

        mockMvc.perform(post("/api/investments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsAllInvestments() throws Exception {
        when(investmentService.getAllInvestments()).thenReturn(List.of(response(101L), response(102L)));

        mockMvc.perform(get("/api/investments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(101))
                .andExpect(jsonPath("$.data[1].id").value(102));
    }

    private InvestmentResponse response(Long id) {
        return InvestmentResponse.builder()
                .id(id)
                .investorId(11L)
                .fundId(21L)
                .amount(new BigDecimal("250000.00"))
                .nav(new BigDecimal("125.0000"))
                .units(new BigDecimal("2000.0000"))
                .status(InvestmentStatus.PENDING)
                .build();
    }
}
