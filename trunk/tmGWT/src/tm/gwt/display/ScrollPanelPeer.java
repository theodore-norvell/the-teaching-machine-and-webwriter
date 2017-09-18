package tm.gwt.display;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;


public class ScrollPanelPeer extends Widget {
	protected ScrollPanel scrollPanel;
	
	public ScrollPanelPeer ( ScrollPanel scrollPanel){
		this.scrollPanel = scrollPanel;	
	}
	
	public void setStyleName ( String style ){
		scrollPanel.setStyleName(style);
	}
	
	public void setAlwaysShowScrollBars ( Boolean alwaysShow) {
		scrollPanel.setAlwaysShowScrollBars(alwaysShow);	
	}
	
	public void setHeight ( String height ) {
		scrollPanel.setHeight(height);
	}
	
	public void setWidth ( String width ) {
		scrollPanel.setWidth (width);
	}
	
	public void add ( Widget w ){
		scrollPanel.add(w);
	}
	
	public int getHorizontalScrollPosition(){
		return scrollPanel.getHorizontalScrollPosition();
	}
	
	public void setVerticalScrollPosition(int position){
		scrollPanel.setVerticalScrollPosition(position);
	}
	
	public Element getElement(){
		return scrollPanel.getElement();
	}
	
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		    return addDomHandler(handler, ClickEvent.getType());
	}
	

}
