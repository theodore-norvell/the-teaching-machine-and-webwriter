package tm.gwt.telford;

import com.google.gwt.user.client.ui.FocusWidget;

import telford.jse.MouseListenerJSE ;

public class UtilGWT {
    
    public static void addMouseListener(FocusWidget widget, telford.common.Component component){
            int telfordCount = component.mouseListenerCount() ;
            int oldCount = widget.getClickHandlerCount() ; 
            if( oldCount == 0 && telfordCount > 0 ) {
                MouseListenerGWT ml =  new MouseListenerGWT(component) ;
                widget.addClickHandler( ml ) ;
            }
            else if( oldCount > 0 && telfordCount == 0 ) {
                // TODO. get ride of any handlers.
            }
    }

}
