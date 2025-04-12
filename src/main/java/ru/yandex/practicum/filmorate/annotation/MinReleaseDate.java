package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinReleaseDateValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MinReleaseDate {
    String message() default "Дата релиза фильма должна быть не раньше 28.12.1895";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
