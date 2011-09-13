/**
 * OptionPayload.java
 * 
 * @date: Sep 12, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * JavaCCOptions provides an unified management for JavaCC options.
 * @author Xiaoyu Guo
 */
public class JavaCCOptions {

	protected Map<String, Object> optionValues = null;
	
	public boolean jdkVersionAtLeast(double version) {
	    double jdkVersion = doubleValue("JDK_VERSION");
	    // Comparing doubles is safe here, as it is two simple assignments.
	    return jdkVersion >= version;
	}
	
	protected static final Map<String, Object> DEFAULT_VALUES = new HashMap<String, Object>(); {
		DEFAULT_VALUES.put("LOOKAHEAD", new Integer(1));
		DEFAULT_VALUES.put("CHOICE_AMBIGUITY_CHECK", new Integer(2));
		DEFAULT_VALUES.put("OTHER_AMBIGUITY_CHECK", new Integer(1));

		DEFAULT_VALUES.put("STATIC", Boolean.TRUE);
	    DEFAULT_VALUES.put("DEBUG_PARSER", Boolean.FALSE);
	    DEFAULT_VALUES.put("DEBUG_LOOKAHEAD", Boolean.FALSE);
	    DEFAULT_VALUES.put("DEBUG_TOKEN_MANAGER", Boolean.FALSE);
	    DEFAULT_VALUES.put("ERROR_REPORTING", Boolean.TRUE);
	    DEFAULT_VALUES.put("JAVA_UNICODE_ESCAPE", Boolean.FALSE);
	    DEFAULT_VALUES.put("UNICODE_INPUT", Boolean.FALSE);
	    DEFAULT_VALUES.put("IGNORE_CASE", Boolean.FALSE);
	    DEFAULT_VALUES.put("USER_TOKEN_MANAGER", Boolean.FALSE);
	    DEFAULT_VALUES.put("USER_CHAR_STREAM", Boolean.FALSE);
	    DEFAULT_VALUES.put("BUILD_PARSER", Boolean.TRUE);
	    DEFAULT_VALUES.put("BUILD_TOKEN_MANAGER", Boolean.TRUE);
	    DEFAULT_VALUES.put("TOKEN_MANAGER_USES_PARSER", Boolean.FALSE);
	    DEFAULT_VALUES.put("SANITY_CHECK", Boolean.TRUE);
	    DEFAULT_VALUES.put("FORCE_LA_CHECK", Boolean.FALSE);
	    DEFAULT_VALUES.put("COMMON_TOKEN_ACTION", Boolean.FALSE);
	    DEFAULT_VALUES.put("CACHE_TOKENS", Boolean.FALSE);
	    DEFAULT_VALUES.put("KEEP_LINE_COLUMN", Boolean.TRUE);
	    
	    DEFAULT_VALUES.put("GENERATE_CHAINED_EXCEPTION", Boolean.FALSE);
	    DEFAULT_VALUES.put("GENERATE_GENERICS", Boolean.FALSE);
	    DEFAULT_VALUES.put("GENERATE_STRING_BUILDER", Boolean.FALSE);
	    DEFAULT_VALUES.put("GENERATE_ANNOTATIONS", Boolean.FALSE);
	    DEFAULT_VALUES.put("SUPPORT_CLASS_VISIBILITY_PUBLIC", Boolean.TRUE);

	    DEFAULT_VALUES.put("OUTPUT_DIRECTORY", ".");
	    DEFAULT_VALUES.put("JDK_VERSION", "1.5");
	    DEFAULT_VALUES.put("TOKEN_EXTENDS", "");
	    DEFAULT_VALUES.put("TOKEN_FACTORY", "");
	    DEFAULT_VALUES.put("GRAMMAR_ENCODING", "");
	    DEFAULT_VALUES.put("OUTPUT_LANGUAGE", "java");
    }
	
	public JavaCCOptions() {
		super();
	    this.optionValues = new HashMap<String, Object>();
	}
	
	public void normalize(){
		optionValues.put("GENERATE_CHAINED_EXCEPTION", Boolean.valueOf(jdkVersionAtLeast(1.4)));
		optionValues.put("GENERATE_GENERICS", Boolean.valueOf(jdkVersionAtLeast(1.5)));
		optionValues.put("GENERATE_STRING_BUILDER", Boolean.valueOf(jdkVersionAtLeast(1.5)));
		optionValues.put("GENERATE_ANNOTATIONS", Boolean.valueOf(jdkVersionAtLeast(1.5)));
	
	}
	
	/**
	 * Convenience method to retrieve integer options.
	 */
	protected int intValue(final String option) {
		return ((Integer) rawGet(option)).intValue();
	}

	/**
	 * Convenience method to retrieve boolean options.
	 */
	protected boolean booleanValue(final String option) {
		return ((Boolean) rawGet(option)).booleanValue();
	}

	/**
	 * Convenience method to retrieve string options.
	 */
	protected String stringValue(final String option) {
		return rawGet(option).toString();
	}
	
	/**
	 * Convenience method to retrieve double options.
	 */
	protected double doubleValue(final String option) {
		return Double.parseDouble(rawGet(option).toString());
	}

	public String format() {
	    StringBuffer sb = new StringBuffer();

	    int count = 0;
	    for (Iterator<String> iter = optionValues.keySet().iterator(); iter.hasNext(); count++) {
			if (count > 0) {
				sb.append(";\n");
			}
			String key = iter.next();
			sb.append(key);
			sb.append('=');
			sb.append(optionValues.get(key));
		}

	    return sb.toString();
	}
	
	public void set(String key, int value){
		rawSet(key, value);
	}
	
	public void set(String key, boolean value){
		rawSet(key, value);
	}
	
	public void set(String key, String value){
		rawSet(key, value);
	}
	
	private void rawSet(String key, Object value){
		if(value != null && key != null && key.length() > 0 && DEFAULT_VALUES.containsKey(key)){
			// ensures that the key and value are both valid
			if(!optionValues.containsKey(key) || !optionValues.get(key).equals(value)){
				// ensures that the optionValues only contains the different 
				// value from the default ones.
				optionValues.put(key, value);
			} else if (optionValues.containsKey(key) && DEFAULT_VALUES.get(key).equals(value)){
				// remove new values that equals to the default value
				optionValues.remove(key);
			}
		}
	}
	
	private Object rawGet(String key){
		Object result = null;
		if(key != null && key.length() > 0 && DEFAULT_VALUES.containsKey(key)){
			if(optionValues.containsKey(key)){
				result = optionValues.get(key);
			} else {
				result = DEFAULT_VALUES.get(key);
			}
		}
		return result;
	}
}
