package soccer1;

class MovingObjectDelegate {
    private int x  ;
    
    private int y ;
    
    MovingObjectDelegate(int x, int y ) {
        this.x = x ;
        this.y = y ;
    }

    int getXPosition() {
        return x ;
    }

    int getYPosition() {
        return y ;
    }

    void updatePosition(int x, int y) {
        this.x = x ;
        this.y = y ;
    }

    double distanceTo(MovingObject other) {
        int deltaX = (x - other.getXPosition()) ;
        int deltaY = (y - other.getYPosition()) ;
        return Math.sqrt( deltaX*deltaX + deltaY*deltaY ) ;
    }
}
