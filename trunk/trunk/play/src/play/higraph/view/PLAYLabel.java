/**
 * PLAYLabel.java - play.higraph.view - PLAY
 * 
 * Created on Feb 16, 2012 by Kai Zhu
 */
package play.higraph.view;

import higraph.view.ComponentView;
import higraph.view.Label;

import java.awt.Font;
import java.awt.Graphics2D;

import play.higraph.model.PLAYEdge;
import play.higraph.model.PLAYEdgeLabel;
import play.higraph.model.PLAYHigraph;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYWholeGraph;
import tm.backtrack.BTTimeManager;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYLabel
	extends
	Label<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> {

    private Font font;

    /**
     * @param id
     * @param position
     * @param cv
     * @param timeMan
     */
    public PLAYLabel(
	    String id,
	    int position,
	    ComponentView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> cv,
	    BTTimeManager timeMan) {
	super(id, position, cv, timeMan);
	this.font = new Font(Font.DIALOG_INPUT, Font.BOLD | Font.ITALIC, 12);
    }

    public void setFont(Font font) {
	this.font = font;
    }

    public void draw(Graphics2D screen) {
	Font font = screen.getFont();
	screen.setFont(this.font);
	super.draw(screen);
	screen.setFont(font);
    }

}
