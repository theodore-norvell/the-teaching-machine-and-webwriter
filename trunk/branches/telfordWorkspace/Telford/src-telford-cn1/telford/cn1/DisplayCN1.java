package telford.cn1;


import com.codename1.ui.Form;

import telford.common.Root;

public class DisplayCN1 implements telford.common.Display {
	protected Root root;
	
	DisplayCN1 () {		
	}
	
	public void setRoot(Root root) {
		this.root = root;
		((Form) root.getPeer().getRepresentative()).show();;
	}

	public Root getRoot() {
		return root;
	}

	public void setPreferredSize(int width, int height) {
		//default do nothing.
	}
}
