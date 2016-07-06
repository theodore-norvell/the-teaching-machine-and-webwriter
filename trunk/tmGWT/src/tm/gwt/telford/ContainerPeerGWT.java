package tm.gwt.telford;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class ContainerPeerGWT extends telford.common.peers.ContainerPeer {

	MyContainer myContainer;

	ContainerPeerGWT(telford.common.Container container) {
		super(container);
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
		return myContainer.getPeer().getOffsetWidth();
	}

	@Override
	public int getHeight() {
		return myContainer.getPeer().getOffsetHeight();
	}

	@Override
	public Object getRepresentative() {
		return myContainer.getPeer();
	}

	@Override
	public void setLayoutManager(telford.common.LayoutManager lm) {
	}

	@Override
	public void addMouseListener(int count) {
	}

	@Override
	public void setStyleName(String styleName) {
		myContainer.getPeer().setStyleName(styleName);
	}

	class MyContainer implements ClickHandler {

//		HorizontalPanel hPanel;
//		ScrollPanel sPanel;
//		private int type;
//
//		public MyContainer() {
//			this.type = 0;
//			hPanel = new HorizontalPanel();
//		}
//
//		public MyContainer(int type) {
//			this.type = type;
//			switch (type) {
//			case 0:
//				hPanel = new HorizontalPanel();
//				break;
//			case 1:
//				sPanel = new ScrollPanel();
//				break;
//			default:
//				break;
//			}
//		}
//
//		public Object getPeer() {
//			if (type == 0)
//				return hPanel;
//			else
//				return sPanel;
//		}
//
//		public void addComponent(telford.common.Component component) {
//			if (type == 0)
//				hPanel.add((Widget) component.getPeer().getRepresentative());
//			else
//				sPanel.add((Widget) component.getPeer().getRepresentative());
//		}
//
//		public void removeComponent(telford.common.Component component) {
//			if (type == 0)
//				hPanel.remove((Widget) component.getPeer().getRepresentative());
//			else
//				sPanel.remove((Widget) component.getPeer().getRepresentative());
//		}
//
//		public void onClick(ClickEvent event) {
//		}
		
		private FlowPanel panel;

		public MyContainer() {
			panel = new FlowPanel();
		}

		public FlowPanel getPeer() {
			return panel;
		}

		public void addComponent(telford.common.Component component) {
			panel.add((Widget) component.getPeer().getRepresentative());
		}

		public void removeComponent(telford.common.Component component) {
			panel.remove((Widget) component.getPeer().getRepresentative());
		}

		public void onClick(ClickEvent event) {
		}
	}
}
