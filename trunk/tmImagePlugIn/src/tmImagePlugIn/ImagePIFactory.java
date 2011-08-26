package tmImagePlugIn;

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
public class ImagePIFactory implements DisplayPIFactoryIntf{
	
	private String configId;
	
	private ImagePIFactory(String parameter){
		configId = parameter;
	}

	
    static public ImagePIFactory createInstance( String parameter ) {
    	return new ImagePIFactory(parameter) ;
    }
	
	public DisplayInterface createPlugin(DisplayContextInterface dc) {
			return new ImageDisplay(dc, configId);
	}

	public Requirement[] getRequirements() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getParameter() {
		return configId;
	}
	
	public String toString(){return "ImagePiFactory{" + configId + ")";}

}

