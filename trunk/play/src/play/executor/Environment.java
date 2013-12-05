package play.executor;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * An Environment maintains a list of variable name and a mapping from those
 * variable names to values  
 * 
 * @author Ravneet
 *
 */
public class Environment {
	private HashMap<String, String> map = new HashMap<String,String>();
	private ArrayList<String> names = new ArrayList<String>() ;
	private Environment parent;
	
	Environment(Environment parent){
		this.parent = parent;
	}
	/** Is the given name among the set of variable names in the Environment?
	 * 
	 * @param name -- a name to check.
	 * @return
	 */
	public boolean has( String name) {
		return map.containsKey( name ) ;
	}
	
	/**
	 * Add the given name to the set of variable names.
	 * <p>
	 * Precondition: The name must not be mapped:  ! has(name)
	 * <p>
	 * Initially the name will be mapped to "false".
	 * @param name -- a name to add.
	 */
	public void add(String name) {
		assert ( ! has( name ) ) ;
		map.put(name, null) ;
		names.add( name ) ;
	}
	
	/** Get the value that a given name maps to
	 * <p>
	 * Precondition: The name must be mapped: has(name)
	 * <p>
	 * Result is the the current mapping for the name.
	 * 
	 * @param name
	 * @return
	 */
	public String get(String name) {
		assert( has( name ) ) ;
		return map.get( name ) ;
	}
	
	
	/** Change the mapping of one variable name.
	 * 
	 * @param name
	 * @param value
	 */
	public void set( String name, String value ) {
		assert( has( name ) ) ;
		map.put(name, value ) ;
	}
}