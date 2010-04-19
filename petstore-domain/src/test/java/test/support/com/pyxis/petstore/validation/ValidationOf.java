package test.support.com.pyxis.petstore.validation;

import test.support.com.pyxis.petstore.builders.Builder;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ValidationOf {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    public static Set<ConstraintViolation<Object>> validationOf(Builder<?> builder) {
        return validationOf(builder.build());
    }

    public static Set<ConstraintViolation<Object>> validationOf(Object bean) {
        return validator().validate(bean);
    }

    private static Validator validator() {
        return factory.getValidator();
    }
}
