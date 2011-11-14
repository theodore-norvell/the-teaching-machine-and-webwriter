/**
 * Property.java
 * 
 * @date: Nov 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing.properties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Xiaoyu Guo
 *
 */
@Target (ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Property {
	public enum Editor{
		NONE, BOOLEAN, INT, TEXT, LIST
	}
	
	public String defaultValue() default "";
	public Editor editor() default Editor.TEXT;
	public String name() default "";
	
	public int minValue() default 0;
	public int maxValue() default Integer.MAX_VALUE;
	public String[] list() default {};
	
	public String getter() default "";
	public String setter() default "";
}
