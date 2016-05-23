package telford.gwt;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ContainerPeerGWT extends telford.common.peers.ContainerPeer {

	MyContainer myContainer ;

	ContainerPeerGWT(telford.common.Container container) {
		super ( container );
		myContainer = new MyContainer();
	}

	@Override
	public void add(telford.common.Component component) {
		myContainer.addComponent(component);
	}

	@Override
	public void remove(telford.common.Component component) {
		myContainer.removeComponent(component);
	}

	@Override
	public void add(telford.common.Component component, Object constraint) {
	}

	@Override
	public void repaint() {
	}

	@Override
	public int getWidth() {
		return UtilGWT.getWidth(myContainer);
	}

	@Override
	public int getHeight() {
		return UtilGWT.getHeight(myContainer);
	}

	@Override
	public Object getRepresentative() {
		return myContainer;
	}

	@Override
	public void setLayoutManager(telford.common.LayoutManager lm) {
	}

	@Override
	public void addMouseListener(int count) {
	}

	class MyContainer extends Composite implements
    ClickHandler{
		
		HorizontalPanel panel;
		/**
	     * Constructs an MyContainer with the given caption on the check.
	     * 
	     * @param caption the caption to be displayed with the check box
	     */
	    public MyContainer() {
	      panel = new HorizontalPanel();
	      initWidget(panel);
	    }
	    
	    public void addComponent(telford.common.Component component){
	    	((HorizontalPanel)getWidget()).add((Widget)component.getPeer().getRepresentative());
	    }
	    
	    public void removeComponent(telford.common.Component component){
	    	((HorizontalPanel)getWidget()).remove((Widget)component.getPeer().getRepresentative());
	    }
	    
	    public void onClick(ClickEvent event) {
	    }
	}
}
