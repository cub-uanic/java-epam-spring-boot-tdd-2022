package dev.abarmin.spring.tdd.workshop.service;

import dev.abarmin.spring.tdd.workshop.model.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author Aleksandr Barmin
 */
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    @Query(value = "select * from Applicant where email = ?1", nativeQuery = true)
    Optional<Applicant> getApplicantByEmail(String email);
}
