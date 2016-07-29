package tm.gwt.display;

import telford.common.ActionEvent;
import telford.common.ActionListener;
import telford.common.Button;
import tm.gwt.jsInterface.MirrorStateTest;
import tm.gwt.state.StateCommander ;
import tm.interfaces.StateInterface ;
import tm.portableDisplays.ExpressionDisplayer;
import tm.portableDisplays.PortableContextInterface;

public class ExpressionDisplay1 extends DisplayAdapter {
	final StateInterface evaluator;
	final StateCommander commander ;
	final PortableContextInterface context ;

	public ExpressionDisplay1(StateInterface evaluator, final StateCommander commander, PortableContextInterface context) {
		super(new ExpressionDisplayer(evaluator, context), "expDisplayPanel", "Expression Display", 150, 75);
		this.evaluator = evaluator ;
        this.commander = commander ;
		this.context = context;
		Button goForwardButton = new Button("<img src='/images/Advance.gif'/>");
		goForwardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				commander.goForward();
			}
		});
		Button goBackdButton = new Button("<img src='/images/Backup.gif'/>");
		goBackdButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
                // TODO
			}
		});
		toolBar.add((com.google.gwt.user.client.ui.Button) goForwardButton.getPeer().getRepresentative());
		toolBar.add((com.google.gwt.user.client.ui.Button) goBackdButton.getPeer().getRepresentative());
		myWorkPane.setStyleName("tm-smallScrollPanel");
	}

	@Override
	public void refresh() {
		((ExpressionDisplayer) displayer).updateExp(evaluator.getExpression());
		super.refresh();
	}

	public void setJsMirrorState(StateInterface e) {
		if (displayer instanceof ExpressionDisplayer) {
			((ExpressionDisplayer) displayer).setState(e);
		}
	}
}
