package telford.common.peers;

import telford.common.Canvas;

public abstract class CanvasPeer extends ComponentPeer {
	public CanvasPeer(Canvas canvas) {
		super(canvas);
	}

	public abstract Object getRepresentative();

	public abstract void addMouseListener(int count);

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract void repaint();

}