package soccer2;

public class Player extends MovingObject {
    
    private int team ;
    
    public Player( int x, int y, int team ) {
        super(x, y) ;
        this.team = team ;
    }

    public void drawToScreen() {
        /* ... */
    }

    public void kick() {
        /* ... */
    }
    
    /* ... other methods specific to Player objects ... */

}
