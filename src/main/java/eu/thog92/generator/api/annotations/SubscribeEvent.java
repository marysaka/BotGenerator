package eu.thog92.generator.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Thog92 on 25/03/2015.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface SubscribeEvent
{
}
