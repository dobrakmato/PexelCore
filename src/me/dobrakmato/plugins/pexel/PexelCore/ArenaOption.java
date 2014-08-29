package me.dobrakmato.plugins.pexel.PexelCore;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that this field represents arena option.
 * 
 * @author Mato Kormuth
 * 
 */
@Target({ java.lang.annotation.ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ArenaOption
{
	/**
	 * Name of option.
	 * 
	 * @return name
	 */
	String name();
}
