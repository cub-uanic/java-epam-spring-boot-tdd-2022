package dev.abarmin.spring.tdd.workshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.abarmin.spring.tdd.workshop.model.*;
import dev.abarmin.spring.tdd.workshop.service.ApplicantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class ApplicantControllerTest {
    public static final long TEST_APPLICANT_ID = 10L;
    public static final String TEST_LAST_NAME = "lastName";
    public static final String TEST_EMAIL = "test@test.com";
    public static final String TEST_URL_APPLICANTS = "/applicants";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ApplicantService applicantService;

    @Test
    void check_contestStarts() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void createApplicant_whenValidEmailAndLastNameProvided_thenReturnId() throws Exception {
        Applicant originalApplicant = Applicant.builder()
                .person(Person.builder()
                        .personName(PersonName.builder()
                                .lastName(TEST_LAST_NAME)
                                .build()
                        ).build())
                .contactPoint(ContactPoint.builder()
                        .electronicAddress(ElectronicAddress.builder()
                                .email(TEST_EMAIL)
                                .build()
                        ).build()
                ).build();

        when(applicantService.save(any(Applicant.class)))
                .thenAnswer(inv -> {
                    final Applicant applicant = inv.getArgument(0, Applicant.class);
                    applicant.setApplicantId(TEST_APPLICANT_ID);
                    return applicant;
                });

        mockMvc.perform(
                        post(TEST_URL_APPLICANTS)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(originalApplicant))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, TEST_URL_APPLICANTS + "/" + TEST_APPLICANT_ID))
                .andExpect(jsonPath("$.applicantId", equalTo(TEST_APPLICANT_ID), Long.class))
                .andExpect(jsonPath("$.person.personName.lastName", equalTo(TEST_LAST_NAME)))
        ;

        final ArgumentCaptor<Applicant> applicantArgumentCaptor = ArgumentCaptor.forClass(Applicant.class);

        verify(applicantService, times(1)).save(applicantArgumentCaptor.capture());

        final Applicant capturedValue = applicantArgumentCaptor.getValue();
        assertThat(capturedValue)
                .usingRecursiveComparison()
                .ignoringFields("applicantId")
                .isEqualTo(originalApplicant);
    }

    @ParameterizedTest
    @MethodSource("parameters_noEmailOrLastName")
    void createApplicant_whenEmailOrLastNameNotProvided_thenBadRequest(final Applicant applicant) throws Exception {
        when(applicantService.save(any(Applicant.class)))
                .thenAnswer(inv -> {
                    final Applicant tmp = inv.getArgument(0, Applicant.class);
                    tmp.setApplicantId(TEST_APPLICANT_ID);
                    return tmp;
                });

        mockMvc.perform(post(TEST_URL_APPLICANTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(applicant))
                )
                .andExpect(status().isBadRequest());
    }

    public static Stream<Arguments> parameters_noEmailOrLastName() {
        return Stream.of(
                Arguments.of(
                        Applicant.builder().build()
                ),
                Arguments.of(
                        Applicant.builder()
                                .person(Person.builder()
                                        .personName(PersonName.builder()
                                                .lastName(TEST_LAST_NAME)
                                                .build()
                                        ).build())
                                .build()
                )
        );
    }
}