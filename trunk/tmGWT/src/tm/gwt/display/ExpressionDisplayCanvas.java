package tm.gwt.display;

import tm.interfaces.StateInterface ;
import tm.portableDisplays.ExpressionDisplayer;
import tm.portableDisplays.PortableContextInterface;

public class ExpressionDisplayCanvas extends ExpressionDisplayer {
	StateInterface evaluator;

	public ExpressionDisplayCanvas(PortableContextInterface context) {
		super(null, context);
	}

	public void updateExpDisplay(MirrorState e) {
		theExpression = e.getExpression();
	}
	
	public void setEvaluator(MirrorState e) {
		evaluator = e;
	}
	
	

	@Override
	public void refresh() {
		if (evaluator == null)
			theExpression = "";
		else
			theExpression = evaluator.getExpression();
	}
}
