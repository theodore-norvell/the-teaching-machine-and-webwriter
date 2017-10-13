package tm.gwt.telford;

import com.google.gwt.event.dom.client.ClickEvent ;
import com.google.gwt.event.dom.client.ClickHandler ;

import telford.common.Component ;
import telford.common.MouseEvent;
import telford.common.MouseListener;

public class MouseListenerGWT implements ClickHandler, MouseListener {

	private Component component ;

    public MouseListenerGWT(Component component) {
        this.component = component ;
    }

    @Override
    public void onClick(ClickEvent event) {
        MouseEvent me = new MouseEvent(event.getX(), event.getY()) ;
        component.fireMouseClicked( me );
    }

	@Override
	public void mouseMoved(MouseEvent e) {
	
	}

	@Override
	public void mouseClick(MouseEvent e) {
		component.MouseJustClicked(e);
	}

}
