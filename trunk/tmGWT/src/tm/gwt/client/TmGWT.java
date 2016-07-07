package tm.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.FlowPanel;

import telford.common.ActionEvent;
import telford.common.ActionListener;
import telford.common.Button;
import telford.common.Container;
import telford.common.Display;
import telford.common.Kit;
import telford.common.Root;
import tm.gwt.display.CodeDisplayCanvas;
import tm.gwt.display.ConcreteMirrorState;
import tm.gwt.display.ExpressionDisplayCanvas;
import tm.gwt.display.GWTContext;
import tm.gwt.display.Viewer;
import tm.gwt.jsInterface.MirrorState;
import tm.gwt.telford.KitGWT;
import tm.portableDisplays.PortableContextInterface;
import tm.portableDisplays.Selection;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TmGWT implements EntryPoint {
	private static Viewer expView;
	private static Viewer codeView;
	private Root expRoot;
	private Root codeRoot;

	private Container expContainer;
	private Container codeContainer;

	public void onModuleLoad() {
		GWT.log("Start GWT test.", null);
		Kit.setKit(new KitGWT());
		Display expDisplay = Kit.getKit().getDisplay();
		expRoot = new Root("expDisplayPanel");
		expDisplay.setRoot(expRoot);

		Display codeDisplay = Kit.getKit().getDisplay();
		codeRoot = new Root("codeDisplayPanel");
		codeDisplay.setRoot(codeRoot);

		// Button goForwardButton = new Button("-&gt;");
		// Button goBackdButton = new Button("&lt;-");
		Button goForwardButton = new Button("<img src='/images/Advance.gif'/>");
		Button goBackdButton = new Button("<img src='/images/Backup.gif'/>");
		goForwardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// expression display refresh operation
				expView.repaint();
				expRoot.add(expContainer);
				codeRoot.add(codeContainer);
			}
		});

		goBackdButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				expView.repaint();
				expRoot.add(expContainer);
				codeRoot.add(codeContainer);
			}
		});

		Container toolBar = new Container();
		toolBar.add(goBackdButton);
		toolBar.add(goForwardButton);

		expContainer = new Container();
		expContainer.add(toolBar);

		codeContainer = new Container();
		codeContainer.setStyleName("tm-flowPanel");

		PortableContextInterface context = new GWTContext();

		// the first parameter of constructor (i.e. MirrorEvaluator) will be set
		// later by JS side
		expView = new Viewer(new ExpressionDisplayCanvas(context));
		expView.getPlayer().setStyleName("tm-smallCanvas");
		codeView = new Viewer(
				new CodeDisplayCanvas(new ConcreteMirrorState(new Selection(Selection.TokenType.TRUE)), context));
		codeView.getPlayer().setStyleName("tm-largeCanvas");
		codeView.getPlayer().resetSize(380, 800);
		expView.repaint();
		codeView.repaint();
		expContainer.add(expView.getPlayer());
		codeContainer.add(codeView.getPlayer());

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				int bottomLine = codeView.getPlayer().calFocusPosition();
				FlowPanel fp = (FlowPanel) codeContainer.getPeer().getRepresentative();
				int scrollBottom = fp.getElement().getScrollTop() + fp.getElement().getClientHeight();
				if(bottomLine >  scrollBottom) {
					fp.getElement().setScrollTop(bottomLine - 4 * codeView.getPlayer().getVScale()); 
				}
				
//				int vertValue = myWorkPane.getVerticalScrollBar().getValue();
//				if (topLine < vertValue || bottomLine > vertValue + myWorkPane.getViewport().getHeight()) {
//					paintImmediately(getBounds());
//					myWorkPane.getVerticalScrollBar().setValue(topLine - 3 * getVScale());
//				}
//				((FlowPanel) codeContainer.getPeer().getRepresentative()).getElement()
//						.setScrollTop(codeView.getPlayer().calFocusPosition());
			}
		});

		expRoot.add(expContainer);
		codeRoot.add(codeContainer);
		// to explore JSNI method to JS
		getExpressionDisplay();
		setMirrorState();
	}

	private static ExpressionDisplayCanvas getDisplayer() {
		return (ExpressionDisplayCanvas) expView.getPlayer();
	}

	private static void setEvaluator(ExpressionDisplayCanvas expDisplay, MirrorState evaluator) {
		expDisplay.setEvaluator(evaluator);
	}

	// ==========JSNI methods================
	private static native void getExpressionDisplay() /*-{
		$wnd.getExpressionDisplay = function() {
			return @tm.gwt.client.TmGWT::getDisplayer()();
		}
	}-*/;

	private static native void setMirrorState() /*-{
		$wnd.setMirrorState = function(obj1, obj2) {
			@tm.gwt.client.TmGWT::setEvaluator(Ltm/gwt/display/ExpressionDisplayCanvas;Ltm/gwt/jsInterface/MirrorState;)(obj1,obj2);
		}
	}-*/;
}
