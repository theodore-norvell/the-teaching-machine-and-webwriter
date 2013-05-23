package tmplugins.sequence;

import tm.displayEngine.DisplayInterface;
import tm.displayEngine.DisplayPIFactoryIntf;
import tm.displayEngine.DisplayManager; 
import tm.plugins.Requirement;

/**
 * 
 * A description of the Type and its responsibilities
 *
 * @author mpbl
 */
public class SequencePiFactory implements DisplayPIFactoryIntf{
	
	private String configId;
	
	private SequencePiFactory(String parameter){
		configId = parameter;
	}

	
    static public SequencePiFactory createInstance( String parameter ) {
    	return new SequencePiFactory(parameter) ;
    }
	
	public DisplayInterface createPlugin(DisplayManager dm) {
			return new SequenceDisplay(dm, configId);
	}

	public Requirement[] getRequirements() {
		return null;
	}

	public String getParameter() {
		return configId;
	}
	
	public String toString(){return "SequencePiFactory{" + configId + ")";}

}