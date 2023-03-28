package com.exchange.payingservice.controllers;

import com.exchange.payingservice.IntegrationTestBase;
import com.exchange.payingservice.entity.Card;
import com.exchange.payingservice.entity.Payment;
import com.exchange.payingservice.util.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
//@RunWith(Runner.class)
class PaymentRestControllerTest extends IntegrationTestBase {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should get payment by id")
    void shouldGetPayment() throws Exception {
        mockMvc.perform(get("/api/payments/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should get all payments")
    void shouldGetAllPayments() throws Exception {
        mockMvc.perform(get("/api/payments"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should update payment by id")
    void shouldUpdatePayment() throws Exception {
        mockMvc.perform(put("/api/payments/{id}", 1L)
                        .content(objectMapper.writeValueAsString
                                (new Payment(1L,
                                        new Card("2011-2222-3333-4567",
                                                "user5",
                                                "515",
                                                6L),
                                        new Date(),
                                        new Date(),
                                        6L,
                                        Status.SUCCESSFULLY,
                                        "MESSAGE")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should delete payment by id")
    void shouldDeletePayment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/payments/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should not get payment by wrong id")
    void shouldNotGetPayment() throws Exception {
        mockMvc.perform(get("/api/payments/{id}", 10))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }


}