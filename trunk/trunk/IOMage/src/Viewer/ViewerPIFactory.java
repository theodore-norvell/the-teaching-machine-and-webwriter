package Viewer;

import tm.displayEngine.DisplayInterface;
import tm.displayEngine.DisplayPIFactoryIntf;
import tm.interfaces.DisplayContextInterface;
import tm.plugins.Requirement;

/**
 * 
 * A description of the Type and its responsibilities
 *
 * @author mpbl
 */
public class ViewerPIFactory implements DisplayPIFactoryIntf{
	
	private String configId;
	
	private ViewerPIFactory(String parameter){
		configId = parameter;
	}

	
    static public ViewerPIFactory createInstance( String parameter ) {
    	return new ViewerPIFactory(parameter) ;
    }
	
	public DisplayInterface createPlugin(DisplayContextInterface dm) {
		if(configId.equalsIgnoreCase("IOMageSW"))
			return new ImageViewerSW(dm, configId);
		return null; //new ImageViewerJIF(dm, configId);
	}

	public Requirement[] getRequirements() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getParameter() {
		return configId;
	}
	
	public String toString(){return "Viewer pIFactory{" + configId + ")";}

}


