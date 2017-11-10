package tm.gwt.server;

import tm.gwt.shared.TMServiceResult;
import tm.interfaces.StatusConsumer ;

class TMServiceStatusReporter implements StatusConsumer {
	private TMServiceResult result ;
	
	TMServiceStatusReporter( TMServiceResult result ) { 
		this.result = result ;
	}
	
	void setResult( TMServiceResult result ) {
		this.result = result ;
	}
	
	TMServiceResult getResult(){
		return result;
	}

	@Override
	public int getStatusCode() {
		return result.statusCode ;
	}

	@Override
	public String getStatusMessage() {
		return result.statusMessage;
	}

	@Override
	public void setStatus(int statusCode, String message) {
		result.statusCode = statusCode ;
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
