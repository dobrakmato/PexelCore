package eu.matejkormuth.pexel.PexelCore.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used for marking subcomamnds. Must be used in class, that have annotation of {@link CommandHandler}.
 * 
 * @author Mato Kormuth
 * 
 */
@Target({ java.lang.annotation.ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {
    /**
     * Name of subcommand.
     * 
     * @return the name
     */
    String name() default "";
    
    /**
     * Description of command.
     * 
     * @return description
     */
    String description() default "";
}
