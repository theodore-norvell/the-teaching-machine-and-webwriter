/**
 * PropertyPanel.java - play.ide.view - PLAY
 * Created on 2012-07-25 by Kai Zhu
 */
package play.ide.view;

import java.util.List;

import javax.swing.JPanel;

import play.higraph.view.PLAYNodeView;

/**
 * @author Kai
 * 
 */
public class PropertyPanel extends JPanel {

    private static final long serialVersionUID = -2841308720107612298L;

    private ViewPropertiesPanel viewPropertiesPanel;

    public PropertyPanel() {
	this.viewPropertiesPanel = new ViewPropertiesPanel();
    }

    public void update(List<PLAYNodeView> list) {
	this.setVisible(false);
	this.removeAll();
	if (list != null && list.size() > 0) {
	    this.viewPropertiesPanel.updateView((PLAYNodeView) list.get(list
		    .size() - 1));
	    this.add(this.viewPropertiesPanel);
	}
	this.setVisible(true);
    }

    public ViewPropertiesPanel getViewPropertiesPanel() {
	return viewPropertiesPanel;
    }

}
