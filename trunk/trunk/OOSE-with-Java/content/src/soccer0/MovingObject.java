package soccer0;

public interface MovingObject {
    public int getXPosition() ;
    public int getYPosition() ;
    public void updatePosition(int x, int y ) ;
    public double distanceTo( MovingObject other ) ;
    public void drawToScreen() ;
}
