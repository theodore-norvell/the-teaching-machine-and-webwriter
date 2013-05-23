package tmplugins.arrayBar;

import tm.displayEngine.DisplayInterface ;
import tm.displayEngine.DisplayPIFactoryIntf ;
import tm.displayEngine.DisplayManager; 
import tm.plugins.Requirement ;

/**
 * 
 * A description of the Type and its responsibilities
 *
 * @author mpbl
 */
public class ArrayBarPiFactory implements DisplayPIFactoryIntf {
	
	private String configId;
	
	private ArrayBarPiFactory(String parameter){
		configId = parameter;
	}

	
    static public ArrayBarPiFactory createInstance( String parameter ) {
    	return new ArrayBarPiFactory(parameter) ;
    }
	
	public DisplayInterface createPlugin(DisplayManager dm) {
			return new ArrayBarDisplay(dm, configId);
	}

	public Requirement[] getRequirements() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getParameter() {
		return configId;
	}
	
	public String toString(){return "ArrayBarPiFactory{" + configId + ")";}

}


