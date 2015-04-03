package eu.thog92.generator.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {TYPE})
public @interface Module
{
    String name();

    String version() default "1.1";

    String dependencies() default "";
}
