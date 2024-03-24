package dev.abarmin.spring.tdd.workshop.model;

import jakarta.persistence.Embeddable;
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
public class PersonName {
    String firstName;
    String lastName;
    String middleName;
}
