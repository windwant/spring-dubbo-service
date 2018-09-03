package org.windwant.spring.core.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * int enum 验证注解
 * Created by Administrator on 18-5-22.
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
@Constraint(validatedBy = EnumIntValidator.class)
public @interface EnumIntValid {
    String message() default "{enum value invalid}";

    int[] value() default {};

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * Defines several {@link EnumIntValid} annotations on the same element.
     *
     * @see EnumIntValid
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {

        EnumIntValid[] value();
    }
}
