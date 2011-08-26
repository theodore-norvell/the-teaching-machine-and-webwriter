/*#H*/package soccer2;

import java.util.ArrayList;
import java.util.List;

public class ArrayPassingExample {
/*#DA*/
	public void moveAll( MovingObject[] movingObjects ) {
		for( int i=0 ; i < movingObjects.length ; ++i )
			movingObjects[i].drawToScreen() ;
	}
/*#HA*/
/*#DB*/
	public void moveAllWithList( List<MovingObject> movingObjects ) {
		for( MovingObject obj : movingObjects )
			obj.drawToScreen() ;
	}
/*#HB*/	
	public void test() {
		MovingObject[] array = new MovingObject[10] ;
		// ... fill the array ...
		moveAll( array ) ;
		
		List<MovingObject> list = new ArrayList<MovingObject>() ;
		// ... fill the list ...
		moveAllWithList( list ) ;
		

/*#DC*/
		Player[] playerArray = new Player[10] ;
		// ... fill the array ...
		moveAll( playerArray ) ; // This is allowed!
/*#HC*/
/*#DD*/	
		List<Player> playerList = new ArrayList<Player>() ;
		// ... fill the list ...
		moveAllWithList( playerList ) ; // Compilation error
	}
/*#HD*/
}
