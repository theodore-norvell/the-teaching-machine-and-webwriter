/**
 * ClosableTabPanel.java - play.ide.view - PLAY
 * Created on 2012-07-22 by Kai Zhu
 */
package play.ide.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

import play.controller.Controller;

/**
 * @author Kai
 * 
 */
public class ClosableTabPanel extends JPanel {

    private static final long serialVersionUID = 5080591067779385015L;

    private JTabbedPane tabbedPane;

    public ClosableTabPanel(JTabbedPane tabbedPane, String text) {
	this.tabbedPane = tabbedPane;
	JLabel label = new JLabel(text);
	this.add(label);
	label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
	JButton button = new TabbedPaneButton();
	this.add(button);
	this.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }

    private class TabbedPaneButton extends JButton {

	private static final long serialVersionUID = -4223186068625419185L;

	public TabbedPaneButton() {
	    int size = 17;
	    this.setPreferredSize(new Dimension(size, size));
	    this.setToolTipText("Close");
	    this.setUI(new BasicButtonUI());
	    this.setContentAreaFilled(false);
	    this.setFocusable(false);
	    this.setBorder(BorderFactory.createEtchedBorder());
	    this.setBorderPainted(false);
	    this.addMouseListener(new MouseAdapter() {

		@Override
		public void mouseEntered(MouseEvent e) {
		    Component component = e.getComponent();
		    if (component instanceof AbstractButton) {
			AbstractButton button = (AbstractButton) component;
			button.setBorderPainted(true);
		    }
		}

		@Override
		public void mouseExited(MouseEvent e) {
		    Component component = e.getComponent();
		    if (component instanceof AbstractButton) {
			AbstractButton button = (AbstractButton) component;
			button.setBorderPainted(false);
		    }
		}

	    });
	    this.setRolloverEnabled(true);
	    this.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
		    int i = ClosableTabPanel.this.tabbedPane
			    .indexOfTabComponent(ClosableTabPanel.this);
		    if (i != -1) {
			ClosableTabPanel.this.tabbedPane.remove(i);
		    }
		    if (ClosableTabPanel.this.tabbedPane.getTabCount() == 0) {
			Controller.getInstance().createNewClass();
		    }
		}

	    });
	}

	@Override
	public void updateUI() {
	}

	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D graphics2d = (Graphics2D) g.create();
	    if (getModel().isPressed()) {
		graphics2d.translate(1, 1);
	    }
	    graphics2d.setStroke(new BasicStroke(2));
	    graphics2d.setColor(Color.BLACK);
	    if (getModel().isRollover()) {
		graphics2d.setColor(Color.MAGENTA);
	    }
	    int delta = 6;
	    graphics2d.drawLine(delta, delta, getWidth() - delta - 1,
		    getHeight() - delta - 1);
	    graphics2d.drawLine(getWidth() - delta - 1, delta, delta,
		    getHeight() - delta - 1);
	    graphics2d.dispose();
	}

    }

}
