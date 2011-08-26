package soccer2;

public class Ball extends MovingObject {

    public Ball( int x, int y ) {
        super(x, y) ;
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
