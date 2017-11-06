package tm.gwt.shared;

import tm.TMBigAppletPIFactory;
import tm.evaluator.Evaluator;
import tm.gwt.state.MirrorState;
import tm.interfaces.EvaluatorInterface;
import tm.interfaces.StateInterface;
import tm.interfaces.TMStatusCode;
import tm.languageInterface.Language;
import tm.languageInterface.LanguagePIFactoryIntf;
import tm.plugins.PlugInManager;
import tm.plugins.PlugInNotFound;
import tm.utilities.Assert;

public class EvaluatorWrapper {
	
	private StateInterface evaluator;
	
	public EvaluatorWrapper(){
        
//		evaluator = new Evaluator(null, null, null, null, null, 0, 0, 0, 0, 0, 0, 0, 0);
	}
	
    public TMServiceResult loadString(String fileName, String programSource) {
        // TODO Complete this method
        TMServiceResult result = new TMServiceResult() ;
        result.resultState = (MirrorState) this.evaluator;
        return result ;
    }


    public TMServiceResult loadRemoteFile(String root, String fileName) {
        // TODO Complete this method
        TMServiceResult result = new TMServiceResult() ;
        result.resultState = (MirrorState) this.evaluator;
        return result ;
    }


    public TMServiceResult initializeTheState() {
        // TODO Complete this method
        TMServiceResult result = new TMServiceResult() ;
        result.resultState = (MirrorState) this.evaluator;
        return result ;
    }


    public TMServiceResult go(String commandString) {
        // TODO Complete this method
        TMServiceResult result = new TMServiceResult() ;
        result.resultState = (MirrorState) this.evaluator;
        return result ;
    }


    public TMServiceResult goBack() {
        // TODO Complete this method
        TMServiceResult result = new TMServiceResult() ;
        result.resultState = (MirrorState) this.evaluator;
        return result ;
    }


    public TMServiceResult toCursor(String fileName, int cursor) {
        // TODO Complete this method
        TMServiceResult result = new TMServiceResult() ;
        result.resultState = (MirrorState) this.evaluator;
        return result ;
    }

}
