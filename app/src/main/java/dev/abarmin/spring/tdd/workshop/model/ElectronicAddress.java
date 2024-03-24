package dev.abarmin.spring.tdd.workshop.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
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
@Embeddable
public class ElectronicAddress {
    @Email
    String email;
}
