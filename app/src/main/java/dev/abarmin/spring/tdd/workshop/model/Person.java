package dev.abarmin.spring.tdd.workshop.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @author Aleksandr Barmin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Person {
    @Embedded
    @Valid
    @NotNull
    PersonName personName;
}
