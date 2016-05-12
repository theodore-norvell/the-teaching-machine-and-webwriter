package telford.client.view;

import telford.common.Display;
import telford.common.Root;

public class DisplayGWT implements Display {
	protected Root root;

	public DisplayGWT() {
	}

	@Override
	public void setRoot(Root root) {
		this.root = root;
	}

	@Override
	public Root getRoot() {
		return root;
	}
	

	@Override
	public void setPreferredSize(int width, int height) {
		//TODO
	}
}
