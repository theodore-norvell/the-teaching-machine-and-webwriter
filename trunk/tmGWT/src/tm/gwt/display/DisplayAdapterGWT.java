package tm.gwt.display;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.ClickEvent ;
import com.google.gwt.event.dom.client.ClickHandler ;

import telford.common.MouseEvent;
import tm.gwt.telford.MouseListenerGWT;
import tm.portableDisplays.PortableDisplayer;

public class DisplayAdapterGWT extends WorkAreaGWT {
	public PortableDisplayer displayer ;
	
	private int verticalScale, horizontalScale;
	private MouseEvent mouseEvent;
	private MouseListenerGWT mouseListenerGWT = new MouseListenerGWT();
	
	public DisplayAdapterGWT(PortableDisplayer displayer, String rootName, String title, int canvasWidth, int canvasHeight) {
	    super(title, rootName);
	    this.displayer = displayer ;
	    displayer.resetSize(canvasWidth, canvasHeight);
	    Canvas rep = (Canvas)displayer.getPeer().getRepresentative() ;
	    myWorkPane.add( rep );
	    
	    rep.addClickHandler(new ClickHandler() {
	        @Override public void onClick(ClickEvent event) {
	        	mouseEvent = new MouseEvent(event.getClientX(), event.getClientY());
	        	notifiesMouseListener();
	        	mouseEvent.setSource(this);
	        }
	    });

/*	   
	    addGWTMouseListener(new MouseListenerGWT(){
			@Override
			public void mouseClick(MouseEvent e) {
				super.mouseClick(e);	
			} });
*/
	    
	    verticalScale = 1;
	    horizontalScale = 1;
	}
	
	public void notifiesMouseListener(){
		mouseListenerGWT.mouseClick(mouseEvent);
	}
/*	
	public void addGWTMouseListener(MouseListenerGWT mouseListener){
		mouseListenerGWT = mouseListener;
	}
*/	
	
	public void refresh() {
//		displayer.refresh();
        displayer.repaint();
    }
	
	public void setScale(int hScale, int vScale){
	    horizontalScale = hScale;
	    verticalScale = vScale;
	}
	
	public int getHScale(){ return horizontalScale;}
	
	public int getVScale(){ return verticalScale;}
    
    public void MouseJustClicked(MouseEvent event){
        
    }
}
