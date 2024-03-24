package dev.abarmin.spring.tdd.workshop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ApplicantControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void check_contestStarts() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void createApplicant_whenValidEmailAndLastNameProvided_thenReturnId() throws Exception {
        mockMvc.perform(
                        post("/applicants")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("")
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/applicants/123"));
    }
}