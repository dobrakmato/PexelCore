package me.dobrakmato.plugins.pexel.PexelCore.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {
    String name() default "";
    
    String description() default "";
}
