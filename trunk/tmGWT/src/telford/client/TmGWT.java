package telford.client;

import com.google.gwt.core.client.EntryPoint;

import telford.client.tm.ExpressionDisplay;
import telford.client.tm.ViewGWT;
import telford.common.ActionEvent;
import telford.common.ActionListener;
import telford.common.Button;
import telford.common.Container;
import telford.common.Display;
import telford.common.Kit;
import telford.common.Root;
import telford.gwt.KitGWT;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TmGWT implements EntryPoint {
	private Button goForwardButton;
	private Button goBackdButton;
	public void onModuleLoad() {
		Kit.setKit(new KitGWT());
		Display display = Kit.getKit().getDisplay();
		Root root = new Root("expDisplayPanel");
		display.setRoot(root);

		goForwardButton = new Button("GoForward");
		goBackdButton = new Button("GoBack");
		goForwardButton.addActionListener( new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				goBackdButton.setEnabled(false);
				//TODO expression display refresh operation
			}} ) ;
		
		goBackdButton.addActionListener( new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				//TODO expression display refresh operation
			}} ) ;
		
		Container toolBar = new Container();
		toolBar.add(goBackdButton);
		toolBar.add(goForwardButton);
		
		root.add(toolBar);

		ViewGWT view = new ViewGWT(new ExpressionDisplay());
		view.repaint();
		root.add(view);
//		root.add(view, lm.getCenter());
	}
}
