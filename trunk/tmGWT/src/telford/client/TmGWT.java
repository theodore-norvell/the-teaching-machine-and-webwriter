package telford.client;

import com.google.gwt.core.client.EntryPoint;

import telford.client.view.KitGWT;
import telford.client.view.View;
import telford.common.Display;
import telford.common.Kit;
import telford.common.Root;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TmGWT implements EntryPoint {
	public void onModuleLoad() {
		Kit.setKit(new KitGWT());
		Display display = Kit.getKit().getDisplay();
		Root root = new Root("expDisplayPanel");
		display.setRoot(root);
		View view = new View(root);
		view.repaint();
	}
}
