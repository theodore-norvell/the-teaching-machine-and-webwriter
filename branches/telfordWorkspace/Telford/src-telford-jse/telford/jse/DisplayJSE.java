package telford.jse;

import java.awt.Container;

import javax.swing.*;

import telford.common.*;

public class DisplayJSE extends JFrame implements Display{
	protected Root root;

	DisplayJSE () {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(500, 500);
		setVisible(true);
		//TODO do we want to create an initial root?
	}
	@Override
	public void setRooot(Root root) {
		this.root = root;
		setContentPane((Container) root.getPeer().getRepresentative());
	}

	@Override
	public Root getRoot() {
		return root;
	}

	@Override
	public void setPreferredSize(int width, int height) {
		setSize(width, height);
	}
	

}
