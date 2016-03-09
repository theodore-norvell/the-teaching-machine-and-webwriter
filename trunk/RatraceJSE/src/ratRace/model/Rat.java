package ratRace.model;


public class Rat {
	Maze maze ;
	// The location in the maze as a real number
	// The rat starts in the middle of the upper left block.
	double i = 0.5, j = 0.5 ;
	// Where is the mouse.
	double mouse_i = 100, mouse_j = 100 ;
	final double radius = 0.25 ;
	
	Rat( Maze maze ) {
		this.maze = maze ;
		this.maze.setRat(this) ;
	}
	
	/** Where is the rat? */
	double get_i() { return i ; }
	
	/** Where is the rat? */
	double get_j() { return j ; }
	
	/** What block is the rat in? */ 
	int getBlock_i( double i) { return (int) Math.floor(i) ; }
	
	/** What block is the rat in? */ 
	int getBlock_j(double j) { return (int) Math.floor(j) ; }
	
	/** React to a change in time */
	public void pulse(long delta_t) {

		double dir_i = mouse_i - i;
		double dir_j = mouse_j - j;
		double hypot = Math.sqrt(dir_i*dir_i+dir_j*dir_j) ;
		// For each block, the mouse is away from the
		// rat, the velocity is increased by 0.001 blocks per
		// millisecond (i.e. 1 block per second).
		double velocity = hypot * 0.001 ;
		// Cap the velocity at 10 blocks per second.
		velocity = Math.min(velocity, 0.01) ;
		
		// Compute the new i and j values
		double distToMove = delta_t * velocity ;
		double scale = distToMove/Math.max(hypot,0.0001) ;
		double delta_i = scale * dir_i ;
		double delta_j = scale * dir_j ;
		double new_i = i + delta_i ;
		double new_j = j + delta_j ;
		
		// Is it ok to move to the new block?
		// If so move, otherwise stay.
		int block_i = getBlock_i(i) ; 	
		int block_j = getBlock_j(j) ;
		// wall above
		boolean hBlocked = delta_i<0 && getBlock_i( new_i-radius )<block_i 
				&& maze.hasHWall(block_i, block_j);
		// wall bellow
		hBlocked = hBlocked || delta_i>0 && getBlock_i( new_i+radius )>block_i
				&& maze.hasHWall(block_i+1, block_j);
		// wall to left
		boolean vBlocked = delta_j<0 && getBlock_j( new_j-radius )<block_j 
				&& maze.hasVWall(block_i,  block_j) ;
		// wall to right
		vBlocked = vBlocked || delta_j>0 && getBlock_j( new_j+radius)>block_j
				&& maze.hasVWall(block_i, block_j+1 ) ;
		
		if( !hBlocked ) {
			i = new_i ;  }
		if( ! vBlocked ) {
			j = new_j ; }
	}

	public void pointAt(double mouse_i, double mouse_j) {
		this.mouse_i = mouse_i ;
		this.mouse_j = mouse_j ;
	}
}
