package dev.abarmin.spring.tdd.workshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aleksandr Barmin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Applicant {
    private Long applicantId;
    private Person person;
    private ContactPoint contactPoint;
}
