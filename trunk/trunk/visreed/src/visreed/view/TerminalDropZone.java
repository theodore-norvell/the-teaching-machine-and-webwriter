package visreed.view;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import higraph.view.NodeView;
import tm.backtrack.BTTimeManager;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.VisreedPayload;

public class TerminalDropZone extends VisreedDropZone {

	private TerminalNodeView view;

	public TerminalDropZone(
			NodeView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> nv,
			BTTimeManager timeMan) {
		super(nv, timeMan);
		if (nv instanceof TerminalNodeView) {
			this.view = (TerminalNodeView) nv;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * visreed.view.VisreedNodeView#handleDoubleClick(java.awt.event.MouseEvent)
	 */
	public void handleDoubleClick(MouseEvent e) {
		if (this.view != null) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				String newValue = (String) JOptionPane.showInputDialog(
					null,
					"Editing value for Terminal Node",
					this.view.getTerminal()
				);

				if (newValue != null && newValue.length() > 0
						&& !(newValue.equals(this.view.getTerminal()))) {
					this.view.setTerminal(newValue);
					this.getHigraphView().getHigraph().getWholeGraph()
							.notifyObservers();
				}
			}
		}
	}
}
