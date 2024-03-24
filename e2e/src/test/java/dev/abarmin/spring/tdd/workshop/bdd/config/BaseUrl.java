package dev.abarmin.spring.tdd.workshop.bdd.config;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Aleksandr Barmin
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface BaseUrl {
}
