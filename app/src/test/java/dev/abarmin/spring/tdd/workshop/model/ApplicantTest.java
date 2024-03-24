package dev.abarmin.spring.tdd.workshop.model;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Aleksandr Barmin
 */
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration(classes = ValidationAutoConfiguration.class)
class ApplicantTest {
    @Autowired
    private Validator validator;

    @Test
    @DisplayName("Person should not be null")
    void personShouldNotBeNull() {
        final Applicant applicant = Applicant.builder()
                .build();

        final Set<ConstraintViolation<Applicant>> violations = validator.validate(applicant);

        assertThat(violations)
                .isNotNull()
                .withFailMessage("Violations should not be null");

        assertThat(violations)
                .isNotEmpty()
                .withFailMessage("There should be a violation");

        assertThat(violations)
                .anySatisfy(v -> {
                    final Path propertyPath = v.getPropertyPath();
                    assertThat(propertyPath)
                            .anySatisfy(path -> {
                                assertThat(path.getName())
                                        .isEqualTo("person");
                            });
                });
    }

    @Test
    void personNameShouldNotBeNull() {
        final Applicant applicant = Applicant.builder()
                .person(Person.builder().build())
                .build();

        final Set<ConstraintViolation<Applicant>> violations = validator.validate(applicant);

        assertThat(violations)
                .withFailMessage("Violations should not be empty")
                .anySatisfy(v -> {
                    final Path propertyPath = v.getPropertyPath();
                    assertThat(propertyPath)
                            .anySatisfy(path -> {
                                assertThat(path.getName())
                                        .isEqualTo("personName");
                            });
                })
                .isNotEmpty();
    }
}