package soccer1;

public class Ball implements MovingObject {
    private MovingObjectDelegate delegate ;
    
    public Ball(int x, int y ) {
        delegate = new MovingObjectDelegate(x,y) ;
    }

    public int getXPosition() {
        return delegate.getXPosition() ;
    }

    public int getYPosition() {
        return delegate.getYPosition();
    }

    public void updatePosition(int x, int y) {
        delegate.updatePosition(x, y ) ;
    }

    public double distanceTo(MovingObject other) {
        return delegate.distanceTo( other ) ;
    }

    public void drawToScreen() {
        /* ... */
    }

    public boolean isOnField() {
        boolean result = false ;
        /* ... */
        return result ;
    }
    
    /* ... other methods specific to Ball objects ... */
}
