/**
 * ViewPropertiesPanel.java - play.ide.view - PLAY
 * Created on 2012-07-25 by Kai Zhu
 */
package play.ide.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalToggleButtonUI;

import play.controller.Controller;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYNodeView;
import play.ide.util.SpringUtilities;

/**
 * @author Kai
 * 
 */
public class ViewPropertiesPanel extends JPanel {

    private static final long serialVersionUID = 8506725187516960763L;

    private Controller controller;

    private JLabel nameLabel;

    protected JTextField nameTextField;

    private JLabel drawColorLabel;

    protected JButton drawColorButton;

    private JColorChooser drawColorChooser;

    private JLabel fillColorLabel;

    protected JButton fillColorButton;

    private JColorChooser fillColorChooser;

    private JLabel isFilledLabel;

    protected JCheckBox isFilledCheckBox;

    private JLabel strokeLabel;

    protected JSlider strokeSlider;

    private JFrame colorChooserFrame;

    private PLAYNodeView playNodeView;

    public ViewPropertiesPanel() {
	this.controller = Controller.getInstance();

	this.setLayout(new SpringLayout());

	this.nameLabel = new JLabel("Name:", JLabel.TRAILING);
	this.nameLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	this.nameTextField = new JTextField(10);
	this.nameTextField.setEditable(false);
	this.nameLabel.setLabelFor(this.nameTextField);

	this.drawColorLabel = new JLabel("DrawColor:", JLabel.TRAILING);
	this.drawColorLabel.setBorder(BorderFactory
		.createLineBorder(Color.GRAY));
	this.drawColorButton = new JButton();
	this.drawColorButton.setUI(new MetalToggleButtonUI());
	this.drawColorButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		ViewPropertiesPanel.this.drawColorChooser
			.setColor(ViewPropertiesPanel.this.drawColorButton
				.getBackground());
		ViewPropertiesPanel.this.colorChooserFrame.getContentPane()
			.removeAll();
		ViewPropertiesPanel.this.colorChooserFrame.getContentPane()
			.add(ViewPropertiesPanel.this.drawColorChooser);
		ViewPropertiesPanel.this.colorChooserFrame
			.setTitle("Draw Color");
		ViewPropertiesPanel.this.colorChooserFrame.pack();
		ViewPropertiesPanel.this.colorChooserFrame.setVisible(true);
	    }

	});
	this.drawColorLabel.setLabelFor(this.drawColorButton);

	this.fillColorLabel = new JLabel("FillColor:", JLabel.TRAILING);
	this.fillColorLabel.setBorder(BorderFactory
		.createLineBorder(Color.GRAY));
	this.fillColorButton = new JButton();
	this.fillColorButton.setUI(new MetalToggleButtonUI());
	this.fillColorButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		ViewPropertiesPanel.this.fillColorChooser
			.setColor(ViewPropertiesPanel.this.fillColorButton
				.getBackground());
		ViewPropertiesPanel.this.colorChooserFrame.getContentPane()
			.removeAll();
		ViewPropertiesPanel.this.colorChooserFrame.getContentPane()
			.add(ViewPropertiesPanel.this.fillColorChooser);
		ViewPropertiesPanel.this.colorChooserFrame
			.setTitle("Fill Color");
		ViewPropertiesPanel.this.colorChooserFrame.pack();
		ViewPropertiesPanel.this.colorChooserFrame.setVisible(true);
	    }

	});

	this.isFilledLabel = new JLabel("IsFilled:", JLabel.TRAILING);
	this.isFilledLabel
		.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	this.isFilledCheckBox = new JCheckBox();
	this.isFilledLabel.setLabelFor(this.isFilledCheckBox);
	this.isFilledCheckBox.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent e) {
		if (ViewPropertiesPanel.this.isFilledCheckBox.isSelected()) {
		    Color color = ViewPropertiesPanel.this.fillColorButton
			    .getBackground();
		    ViewPropertiesPanel.this.playNodeView.setFillColor(color);
		} else {
		    ViewPropertiesPanel.this.playNodeView.setFillColor(null);
		}
		ViewPropertiesPanel.this.controller
			.refresh((PLAYHigraphView) ViewPropertiesPanel.this.playNodeView
				.getHigraphView());
	    }

	});

	this.strokeLabel = new JLabel("Stroke Width:", JLabel.TRAILING);
	this.strokeLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	this.strokeSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
	this.strokeSlider.setMajorTickSpacing(1);
	this.strokeSlider.setMinorTickSpacing(1);
	this.strokeSlider.setPaintTicks(true);
	this.strokeSlider.setPaintLabels(true);
	this.strokeSlider.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent e) {
		if (ViewPropertiesPanel.this.strokeSlider.getValueIsAdjusting()) {
		    ViewPropertiesPanel.this.playNodeView
			    .setStroke(new BasicStroke(
				    ViewPropertiesPanel.this.strokeSlider
					    .getValue()));
		    ViewPropertiesPanel.this.controller
			    .refresh((PLAYHigraphView) ViewPropertiesPanel.this.playNodeView
				    .getHigraphView());
		}
	    }

	});
	this.strokeLabel.setLabelFor(this.strokeSlider);

	this.drawColorChooser = new JColorChooser();
	this.drawColorChooser.getSelectionModel().addChangeListener(
		new ChangeListener() {

		    @Override
		    public void stateChanged(ChangeEvent e) {
			Color color = ViewPropertiesPanel.this.drawColorChooser
				.getColor();
			ViewPropertiesPanel.this.drawColorButton
				.setBackground(color);
			ViewPropertiesPanel.this.playNodeView.setColor(color);
			ViewPropertiesPanel.this.controller
				.refresh((PLAYHigraphView) ViewPropertiesPanel.this.playNodeView
					.getHigraphView());
		    }

		});

	this.fillColorChooser = new JColorChooser();
	this.fillColorChooser.getSelectionModel().addChangeListener(
		new ChangeListener() {

		    @Override
		    public void stateChanged(ChangeEvent e) {
			Color color = ViewPropertiesPanel.this.fillColorChooser
				.getColor();
			ViewPropertiesPanel.this.fillColorButton
				.setBackground(color);
			ViewPropertiesPanel.this.playNodeView
				.setFillColor(color);
			ViewPropertiesPanel.this.controller
				.refresh((PLAYHigraphView) ViewPropertiesPanel.this.playNodeView
					.getHigraphView());
		    }

		});

	this.colorChooserFrame = new JFrame();
	this.colorChooserFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	this.colorChooserFrame.setLocation(300, 200);
	this.colorChooserFrame.setVisible(false);

	this.add(this.nameLabel);
	this.add(this.nameTextField);
	this.add(this.drawColorLabel);
	this.add(this.drawColorButton);
	this.add(this.fillColorLabel);
	this.add(this.fillColorButton);
	this.add(this.isFilledLabel);
	this.add(this.isFilledCheckBox);
	this.add(this.strokeLabel);
	this.add(this.strokeSlider);
	SpringUtilities.makeCompactGrid(this, 5, 2, 2, 2, 2, 2);
    }

    public void updateView(PLAYNodeView playNodeView) {
	this.playNodeView = playNodeView;
	this.setVisible(false);
	this.nameTextField.setText(this.playNodeView.getNode().getTag().name());
	this.drawColorButton.setBackground(this.playNodeView.getColor());
	this.fillColorButton.setBackground(this.playNodeView.getFillColor());
	this.strokeSlider.setValue((int) ((BasicStroke) this.playNodeView
		.getStroke()).getLineWidth());
	this.setVisible(true);
    }

    public Color getDrawColor() {
	return this.drawColorButton.getBackground();
    }

    public Color getFillColor() {
	return this.fillColorButton.getBackground();
    }

    public int getStroke() {
	return this.strokeSlider.getValue();
    }

    public boolean isFilled() {
	return this.isFilledCheckBox.isSelected();
    }

}
