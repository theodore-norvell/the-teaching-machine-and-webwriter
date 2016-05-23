package telford.client.tm;

import telford.common.Canvas;
import telford.common.Graphics;

public class ViewGWT extends Canvas {
	TMDisplayInterface display;
	public ViewGWT(TMDisplayInterface display) {
		super();
		this.display = display;
		repaint();
	}

	@Override
	public void paintComponent(Graphics tg) {
		super.paintComponent(tg); // redraw canvas
		display.drawArea(tg);
	}

	public void terminate() {
		timer.stop();
	}

	public void start() {
		timer.start();
	}

	// It is important to use a Swing timer here
	private telford.common.Timer timer;
}
