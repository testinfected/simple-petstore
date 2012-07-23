package com.pyxis.petstore;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Indicates that an annotated class is a "Factory" in the DDD sense.
 *
 * <p>This annotation also serves as a specialization
 * of {@link org.springframework.stereotype.Component @Component}, allowing for implementation classes
 * to be autodetected through classpath scanning.
 *
 * @see org.springframework.stereotype.Component
 * @see org.springframework.context.annotation.ClassPathBeanDefinitionScanner
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Factory {

	/**
	 * The value may indicate a suggestion for a logical component name,
	 * to be turned into a Spring bean in case of an autodetected component.
	 * @return the suggested component name, if any
	 */
	String value() default "";
}