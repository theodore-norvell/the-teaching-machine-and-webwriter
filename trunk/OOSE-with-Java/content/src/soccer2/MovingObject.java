package soccer2;

public abstract class MovingObject {
    private int x  ;
    
    private int y ;
    
    MovingObject(int x, int y ) {
        this.x = x ;
        this.y = y ;
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
    
    public abstract void drawToScreen() ;

}
