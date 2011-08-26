package soccer0;

public class Player implements MovingObject {
    
    private int x  ;
    
    private int y ;
    
    private int team ;
    
    public Player(int x, int y, int team ) {
        this.x = x ;
        this.y = y ;
        this.team = team ;
    }

    public int getXPosition() {
        return x ;
    }

    public int getYPosition() {
        return y ;
    }

    public void updatePosition(int x, int y) {
        this.x = x ;
        this.y = y ;
    }

    public double distanceTo(MovingObject other) {
        int deltaX = (x - other.getXPosition()) ;
        int deltaY = (y - other.getYPosition()) ;
        return Math.sqrt( deltaX*deltaX + deltaY*deltaY ) ;
    }

    public void drawToScreen() {
        /* ... */
    }

    public void kick() {
        /* ... */
    }
    
    /* ... other methods specific to Player objects ... */
}
