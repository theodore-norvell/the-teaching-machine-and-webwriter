package telford.common;

public class MouseEvent {
	
	int x,y;
	int clickCount = 0 ;
	private Object mouseEventSource;
	
	/** The x coordinate relative to the target component. */
	public int getX() {
		return x;
	}

	/** The y coordinate relative to the target component. */
    public int getY() {
		return y;
	}

	public MouseEvent(int x, int y){
		this.x = x;
		this.y = y;
	}

    public MouseEvent(int x, int y, int clickCount ){
        this.x = x;
        this.y = y;
        this.clickCount = clickCount ;
    }
   
	public void setSource(Object source){
		mouseEventSource = source;
	}
	
	public Object getSource(){
		return mouseEventSource;
	}
}
