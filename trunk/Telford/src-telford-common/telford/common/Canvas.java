package telford.common;

import telford.common.peers.CanvasPeer;

public class Canvas extends Component {
	public Canvas() {
		this(true);
	}

	public Canvas(boolean makePeer) {
		super(false);
		if (makePeer) {
			peer = Kit.getKit().makeCanvasPeer(this);
		}
	}

	@Override
	public CanvasPeer getPeer() {
		return (CanvasPeer) peer;
	}

	@Override
	public void paintComponent(Graphics g) {
	}
	
	public void resetSize(int width, int height){
		this.getPeer().resetSize(width, height);
	}
	
	public FontMetrics getFontMetrics( Font f ) {
	    return this.getPeer().getFontMetrics( f ) ;
	}

}
