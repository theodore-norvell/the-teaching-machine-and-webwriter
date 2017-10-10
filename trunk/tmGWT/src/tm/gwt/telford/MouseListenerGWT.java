package tm.gwt.telford;

import com.google.gwt.event.dom.client.ClickEvent ;
import com.google.gwt.event.dom.client.ClickHandler ;

import telford.common.Component ;
import telford.common.MouseEvent;
import tm.gwt.display.DisplayAdapterGWT;
import tm.gwt.display.GWTContext;

public class MouseListenerGWT implements ClickHandler {

	private Component component ;

    public MouseListenerGWT(Component component) {
        this.component = component ;
    }

    @Override
    public void onClick(ClickEvent event) {
        MouseEvent me = new MouseEvent() ;
        component.fireMouseClicked( me );
    }

}
