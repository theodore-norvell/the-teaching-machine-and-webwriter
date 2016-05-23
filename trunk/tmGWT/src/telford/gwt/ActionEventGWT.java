package telford.gwt;

import com.google.gwt.event.dom.client.ClickEvent;

public class ActionEventGWT implements telford.common.ActionEvent{
	
	ClickEvent e;
	
	ActionEventGWT(ClickEvent e){
		this.e = e;
	}
}
