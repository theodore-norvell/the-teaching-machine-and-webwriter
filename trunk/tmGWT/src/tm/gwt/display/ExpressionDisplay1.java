package tm.gwt.display;

import telford.common.ActionEvent;
import telford.common.ActionListener;
import telford.common.Button;
import tm.gwt.jsInterface.MirrorState;
import tm.gwt.jsInterface.MirrorStateTest;
import tm.portableDisplays.ExpressionDisplayer;
import tm.portableDisplays.PortableContextInterface;

public class ExpressionDisplay1 extends DisplayAdapter {
	MirrorState evaluator;
	MirrorStateTest testModel;
	PortableContextInterface context = new GWTContext();

	public ExpressionDisplay1(MirrorState evaluator, PortableContextInterface context) {
		super(new ExpressionDisplayer(evaluator, context), "expDisplayPanel", "Expression Display", 150, 75);
		this.evaluator = evaluator;
		this.context = context;
		Button goForwardButton = new Button("<img src='/images/Advance.gif'/>");
		goForwardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				refresh();
			}
		});
		Button goBackdButton = new Button("<img src='/images/Backup.gif'/>");
		goBackdButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				refresh();
			}
		});
		toolBar.add((com.google.gwt.user.client.ui.Button) goForwardButton.getPeer().getRepresentative());
		toolBar.add((com.google.gwt.user.client.ui.Button) goBackdButton.getPeer().getRepresentative());
		myWorkPane.setStyleName("tm-smallScrollPanel");
	}

	@Override
	public void refresh() {
		if (displayer instanceof ExpressionDisplayer && testModel != null) {
			((ExpressionDisplayer) displayer).updateExp(testModel.getExpression());
		}
		super.refresh();
	}

	public void setJsMirrorState(MirrorState e) {
		if (displayer instanceof ExpressionDisplayer) {
			((ExpressionDisplayer) displayer).setState(e);
		}
	}

	public void setJsMirrorStateTest(MirrorStateTest e) {
		testModel = e;
	}
}
