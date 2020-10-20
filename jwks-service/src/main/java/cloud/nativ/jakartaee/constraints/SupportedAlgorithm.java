package cloud.nativ.jakartaee.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom {@link Constraint} annotation to check for supported Algorithms.
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = SupportedAlgorithmValidator.class)
@Documented
public @interface SupportedAlgorithm {
    /**
     * The default validation message key.
     *
     * @return the validation message key
     */
    String message() default "{cloud.nativ.javaee.constraints.SupportedAlgorithm.message}";

    /**
     * The classes in this validation group.
     *
     * @return validation group classes
     */
    Class<?>[] groups() default {};

    /**
     * Payload type attached to constraint.
     *
     * @return the constraint payload
     */
    Class<? extends Payload>[] payload() default {};
}
