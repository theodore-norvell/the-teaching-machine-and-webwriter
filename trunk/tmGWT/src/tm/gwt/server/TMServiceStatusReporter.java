package tm.gwt.server;

import tm.evaluator.Evaluator ;
import tm.gwt.shared.TMServiceResult;
import tm.interfaces.StatusConsumer ;
import tm.interfaces.TMStatusCode ;

class TMServiceStatusReporter implements StatusConsumer {
	private TMServiceResult result ; // Must not be null
	private Evaluator evaluator = null ;
	
	TMServiceStatusReporter( TMServiceResult result ) { 
		this.result = result ;
	}
	
	void setResult( TMServiceResult result ) {
		this.result = result ;
	}
	
	void setEvaluator( Evaluator evaluator ) {
	    this.evaluator = evaluator ;
	}
	
	TMServiceResult getResult(){
		return result;
	}

	@Override
	public int getStatusCode() {
	    if( evaluator == null ) return TMStatusCode.NO_EVALUATOR ;
	    else return evaluator.getStatusCode() ;
	}

	@Override
	public String getStatusMessage() {
	    if( evaluator == null ) return "No evaluator" ; 
        else return evaluator.getStatusMessage() ;
	}

	@Override
	public void setStatus(int statusCode, String message) {
		result.statusCode = statusCode ;
		result.statusMessage = message;
		if( evaluator != null ) {
		    evaluator.setStatusCode( statusCode );
		    evaluator.setStatusMessage( message ) ;
		}
	}

	@Override
	public void attention(String message) {
		result.attentionMessage = message ;
	}

	@Override
	public void attention(String message, Throwable th) {
		result.attentionMessage = message ;
		result.exceptionInformation = th.getMessage() + "\n" + tm.AttentionFrame.formatStackTrace( th ) ;
	}
}