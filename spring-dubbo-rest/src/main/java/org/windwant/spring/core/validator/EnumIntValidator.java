package org.windwant.spring.core.validator;

import org.apache.commons.lang.ArrayUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * int enum 验证器
 * Created by Administrator on 18-5-22.
 */
public class EnumIntValidator implements ConstraintValidator<EnumIntValid, Integer> {
    private int[] enums = {};

    @Override
    public void initialize(EnumIntValid constraintAnnotation) {
        this.enums = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if(value == null) return true;

        if(ArrayUtils.contains(enums, value)){
            return true;
        }
        return false;
    }
}
