package adamets.filmorate.exceptions.customValidAnnotation;

import adamets.filmorate.exceptions.customValidator.MinDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = {MinDateValidator.class})
@Documented
public @interface MinDate {

    String minDate() default "1895-12-28";

    String message() default "Дата не может быть раньше 28 декабря 1895 года и позже текущей даты";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
