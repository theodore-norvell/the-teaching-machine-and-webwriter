package tm.gwt.display;

import tm.portableDisplaysGWT.ExpressionDisplayer;
import tm.portableDisplaysGWT.PortableContextInterface;

public class ExpressionDisplayerGWT extends ExpressionDisplayer {
	MirrorState evaluator;

	public ExpressionDisplayerGWT(PortableContextInterface context) {
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
