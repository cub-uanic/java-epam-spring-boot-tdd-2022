package dev.abarmin.spring.tdd.workshop.controller;

import dev.abarmin.spring.tdd.workshop.model.Applicant;
import dev.abarmin.spring.tdd.workshop.service.ApplicantService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/applicants")
@AllArgsConstructor
public class ApplicantController {
    @Autowired
    ApplicantService applicantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SneakyThrows
    public ResponseEntity<Applicant> createApplicant(final @Valid @RequestBody Applicant applicant) {
        Applicant savedApplicant = applicantService.save(applicant);
        return ResponseEntity
                .created(new URI("/applicants/" + savedApplicant.getApplicantId()))
                .body(savedApplicant);
    }
}
