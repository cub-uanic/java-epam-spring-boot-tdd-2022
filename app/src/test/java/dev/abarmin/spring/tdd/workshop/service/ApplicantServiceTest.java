package dev.abarmin.spring.tdd.workshop.service;

import dev.abarmin.spring.tdd.workshop.model.Applicant;
import dev.abarmin.spring.tdd.workshop.model.ContactPoint;
import dev.abarmin.spring.tdd.workshop.model.ElectronicAddress;
import dev.abarmin.spring.tdd.workshop.model.Person;
import dev.abarmin.spring.tdd.workshop.model.PersonName;

import java.util.Optional;
import java.util.UUID;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Aleksandr Barmin
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ApplicantService.class,
        MethodValidationPostProcessor.class
})
class ApplicantServiceTest {
    @Autowired
    private ApplicantService service;

    @MockBean
    private ApplicantRepository repository;

    @Test
    void contextShouldStart() {
        assertThat(service)
                .isNotNull();
    }

    @Test
    @DisplayName("Should not allow null values")
    void save_shouldNotAllowNullValue() {
        assertThatThrownBy(() -> {
            service.save(null);
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("must not be null");
    }

    @Test
    void save_shouldValidateAllConstraints() {
        final Applicant applicant = Applicant.builder()
                .build();

        assertThatThrownBy(() -> {
            service.save(applicant);
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("save.applicant.person: must not be null");
    }

    @Test
    void save_shouldThrowExceptionIfEmailDuplicates() {
        final Applicant applicant = Applicant.builder()
                .person(Person.builder()
                        .personName(PersonName.builder()
                                .build())
                        .build())
                .contactPoint(ContactPoint.builder()
                        .electronicAddress(ElectronicAddress.builder()
                                .email("test@email.com")
                                .build())
                        .build())
                .build();

        when(repository.getApplicantByEmail(eq("test@email.com"))).thenReturn(Optional.of(
                Applicant.builder().build()
        ));

        assertThatThrownBy(() -> {
            service.save(applicant);
        })
                .isInstanceOf(ApplicantExistsException.class);

        verify(repository, times(1)).getApplicantByEmail(eq("test@email.com"));
    }

    @Test
    void save_shouldReturnTheValue() {
        final Applicant applicant = Applicant.builder()
                .person(Person.builder()
                        .personName(PersonName.builder()
                                .build())
                        .build())
                .contactPoint(ContactPoint.builder()
                        .electronicAddress(ElectronicAddress.builder()
                                .email("email@test.com")
                                .build())
                        .build())
                .build();

        when(repository.save(any(Applicant.class))).thenAnswer(inv -> {
            final Applicant toSave = inv.getArgument(0);
            toSave.setApplicantId(10L);
            return toSave;
        });

        final Applicant savedApplicant = service.save(applicant);

        assertThat(savedApplicant)
                .withFailMessage("Should not be null")
                .isNotNull();

        assertThat(savedApplicant)
                .extracting(Applicant::getApplicantId)
                .withFailMessage("ApplicantId is null")
                .isNotNull();

        assertThat(savedApplicant)
                .usingRecursiveComparison()
                .ignoringFields("applicantId")
                .withFailMessage("Saved applicant is not the same")
                .isEqualTo(applicant);

        verify(repository, times(1)).save(eq(applicant));
    }

    @Test
    void getApplicant_shouldUseRepository() {
        final Long applicantId = 10L;

        when(repository.findById(eq(applicantId))).thenReturn(Optional.of(Applicant.builder()
                .build()));

        final Optional<Applicant> optionalApplicant = service.getApplicant(applicantId);

        verify(repository, times(1)).findById(eq(applicantId));

        assertThat(optionalApplicant)
                .isPresent()
                .withFailMessage("No applicant returned")
                .isNotNull();
    }
}