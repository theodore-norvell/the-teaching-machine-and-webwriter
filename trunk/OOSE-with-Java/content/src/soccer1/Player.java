package soccer1;

public class Player implements MovingObject {
    
    private MovingObjectDelegate delegate ;
    
    private int team ;
    
    Player(int x, int y, int team ) {
        delegate = new MovingObjectDelegate(x,y) ;
        this.team = team ;
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

    public void kick() {
        /* ... */
    }
    
    /* ... other methods specific to Player objects ... */
}
