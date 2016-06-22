package tm.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import telford.common.ActionEvent;
import telford.common.ActionListener;
import telford.common.Button;
import telford.common.Container;
import telford.common.Display;
import telford.common.Kit;
import telford.common.Root;
import tm.gwt.display.ExpressionDisplayerGWT;
import tm.gwt.display.MirrorState;
import tm.gwt.display.CodeDisplayerGWT;
import tm.gwt.display.ConcreteMirrorState;
import tm.gwt.display.Context;
import tm.gwt.display.Viewer;
import tm.gwt.telford.KitGWT;
import tm.portableDisplaysGWT.PortableContextInterface;

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

		Button goForwardButton = new Button("-&gt;");//("GoForward");
		Button goBackdButton = new Button("&lt;-");//("GoBack");
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
		
		expContainer = new Container(0);
		expContainer.add(toolBar);
		
		codeContainer = new Container(1);
		codeContainer.setStyleName("tm-codeScrollPanel");

		PortableContextInterface context = new Context();

		// the first parameter of constructor (i.e. MirrorEvaluator) will be set
		// later by JS side
		expView = new Viewer(new ExpressionDisplayerGWT(context));
		expView.getPlayer().setStyleName("tm-smallCanvas");
		codeView = new Viewer(new CodeDisplayerGWT(new ConcreteMirrorState(), context));
		codeView.getPlayer().setStyleName("tm-largeCanvas");
		codeView.getPlayer().resetSize(380, 300);
		expView.repaint();
		codeView.repaint();
		expContainer.add(expView.getPlayer());
		codeContainer.add(codeView.getPlayer());
		expRoot.add(expContainer);
		codeRoot.add(codeContainer);
		// to explore JSNI method to JS
		getExpressionDisplay();
		setMirrorState();
	}

	private static ExpressionDisplayerGWT getDisplayer() {
		return (ExpressionDisplayerGWT) expView.getPlayer();
	}

	private static void setEvaluator(ExpressionDisplayerGWT expDisplay, MirrorState evaluator){
		expDisplay.setEvaluator(evaluator);
	}

	//==========JSNI methods================
	private static native void getExpressionDisplay() /*-{
		$wnd.getExpressionDisplay = function() {
			return @tm.gwt.client.TmGWT::getDisplayer()();
		}
	}-*/;

	private static native void setMirrorState() /*-{
	$wnd.setMirrorState = function(obj1, obj2) {
		@tm.gwt.client.TmGWT::setEvaluator(Ltm/gwt/display/ExpressionDisplayerGWT;Ltm/gwt/display/MirrorState;)(obj1,obj2);
	}
}-*/;
}
