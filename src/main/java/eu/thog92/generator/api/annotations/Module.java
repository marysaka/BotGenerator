package eu.thog92.generator.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Created by Thog92 on 23/03/2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={TYPE})
public @interface Module
{
    String name();

    String version();
}
