package ratRace.model;

import java.util.LinkedList;
import java.util.Queue;

import telford.common.Kit;
import telford.common.Random;

class Maze {
	private Random r = Kit.getKit().getRandom() ;
	private final int ROWS, COLS ;
	private final int MAXWALLLEN ;
	private final int MINWALLLEN ;
	private final int goal_i, goal_j ;
	private boolean[][] hwall ;
	private boolean[][] vwall ;
	private Rat rat ;
	
	// The maze is a set of blocks in a grid that is ROWS by COLS. The
	// blocks are numbered from (0,0) to (ROWS-1, COLS-1)
	// To the left, right, above and below each block is a potential wall.
	// Above and below of blocks are horizontal wall segments numbered from (0,0) (above block (0,0)) to
	// (ROWS, COLS-1) (below block (ROWS-1,COLS-1)).  
	//   hwall[i][j] means there is a horizontal wall segment above of block (i,j)
	//               and/or beneath block(i-1,j)
	// To the left and right blocks are vertical wall segments, numbered from (0,0) left of block (0,0) to
	// (ROWS-1,COLS) (right of block (ROWS-1,COLS-1)
	//   vwall[i][j] means there is a vertical wall segment left of block (i,j)
	//              and/or to the right of block(i,j-1)  
	// (goal_i, goal_j) represent the block containing the goal.
	// rat is the Rat object.
	
	// Invariant: solvable(), i.e. there exists a path from the rat to the goal.
	// Invariant: there are always 4 boundary walls that surround the set of blocks.
	// Since the rat can not go through a wall, the second invariant keeps the rat in the maze.
		
	int getMazeWidth()  { return COLS ; }
	
	int getMazeHeight() { return ROWS ; }
	
	int goal_i() { return goal_i ; }
	
	int goal_j() { return goal_j ; }
	
	public boolean hasHWall(int i, int j) {
		return hwall[i][j] ;
	}
	
	public boolean hasVWall(int i, int j) {
		return vwall[i][j] ;
	}
	
	private class Block{
		int i, j ;
		boolean reachable = false ;
		Block( int i, int j) { this.i = i ; this.j = j ; }
	}
	
	private Block[][] block;
	
	private Block getRatNode() {
		int i = (int) rat.get_i() ;
		int j = (int) rat.get_j() ;
		return block[i][j] ;
	}

	
	private boolean solvable() {
		// Is there a clear path from the goal to the rat?
		Queue<Block> queue = new LinkedList<Block>() ;
		for( int i = 0 ; i < ROWS; ++i ) 
			for( int j = 0 ; j < COLS ; ++j ) 
				block[i][j].reachable = false ;
		Block goalNode = block[ goal_i][ goal_j ] ;
		Block ratNode = getRatNode() ;
		// Start at the goal and flood the graph until the rat is reached or no more flooding
		// is possible.
		// Note I'm relying on the 4 boundary walls to keep the flood within the maze
		// and thus avoid subscript out of bounds errors.
		goalNode.reachable = true ;
		queue.add( goalNode ) ;
		// Invariant:
		//   (0) For every block x, if x.reachable, then x is reachable from the goal
		//   (1) For every block x, if x is on the queue, then x.reachable
		//   (2) For every block x, if x.reachable then either x is on the queue or for every
		//     neighbour y of x so that there is no wall between x and y, y.reachable.
		//   Therefore: if the queue is empty, and z is such that z.reachable is false,
		//     then there is no path from goal to z.  Since, if there were, the path
		//     would have to look like this  goal,... x, y,... z where x.reachable, but
		//     not y.reachable, and invariant (2) says x is on the queue.
		//  And so, considering invariant (0), if the queue is empty then
		//     For all x, x.reachable == (there is a path from goal to x)
		while( ! queue.isEmpty() && ! ratNode.reachable  ) {
			Block x = queue.remove() ;
			// For each neighbour, y, if y was not previously reached, it is now.
			// Up
			if( ! hwall[x.i][x.j] ) {
				Block y = block[ x.i-1 ][ x.j ] ;
				if( !y.reachable ) {
					y.reachable = true ;
					queue.add( y ) ; } }
			// Down
			if( ! hwall[x.i+1][x.j] ) {
				Block y = block[ x.i+1 ][ x.j ] ;
				if( !y.reachable ) {
					y.reachable = true ;
					queue.add( y ) ; } }
			// Left
			if( ! vwall[x.i][x.j] ) {
				Block y = block[ x.i ][ x.j-1 ] ;
				if( !y.reachable ) {
					y.reachable = true ;
					queue.add( y ) ; } }
			// Right
			if( ! vwall[x.i][x.j+1] ) {
				Block y = block[ x.i ][ x.j+1 ] ;
				if( !y.reachable ) {
					y.reachable = true ;
					queue.add( y ) ; } }
		}
		return ratNode.reachable ;
	}
	
	double gaussian( double x0, double x1) {
		double stdDev = (x1-x0) / 2 ;
		double mean = (x1+x0) / 2 ;
		return r.nextGaussian() * stdDev + mean ;
	}
	
	private void drawAWall() {
		int count = 5 ;
		// Try at most 'count' times to build a wall that does not make the maze unsolvable.
		do { // invariant solvable()
			int rat_i = (int) rat.get_i() ;
			int rat_j = (int) rat.get_j() ;
			// Define a box based on the goal and the rat.
			int box_top = Math.min(rat_i, goal_i ) ;
			int box_bot = Math.max(rat_i, goal_i ) ;
			int box_left = Math.min(rat_j, goal_j ) ;
			int box_right = Math.max(rat_j, goal_j);
			// Two random numbers likely to represent a point in the box.
			double y_start = gaussian( box_top, box_bot) ;
			double x_start = gaussian( box_left, box_right) ;

			// The following variables define a wall. See preconditions
			// of method 'wall' for constraints.
			int i_start ;
			int j_start ;
			int di, dj ;
			boolean horiz = r.nextBoolean() ;
			int len = r.nextInt(MAXWALLLEN - MINWALLLEN + 1) + MINWALLLEN;
			
			if( horiz ) {
				i_start = Math.max(1, Math.min(ROWS-1, (int)y_start ));
				j_start = Math.max(0,Math.min(COLS-1, (int)x_start ));
				di = 0;
				dj = 1 - r.nextInt(1) * 2;
			} else {
				i_start = Math.max(0, Math.min(ROWS-1, (int)( y_start )));
				j_start = Math.max(1,Math.min(COLS-1, (int)(x_start)));
				di = 1 - r.nextInt(1) * 2;
				dj = 0; }
			
			// Build a wall from (i_start, j_start).
			// This might overlap segments of a preexisting wall.
			wall( i_start, j_start, len, di, dj, horiz, true ) ;
			if( ! solvable() ) {
				// Tear down the wall just built.
				// This will remove any wall segment that overlapped.
				wall( i_start, j_start, len, di, dj, horiz, false ) ;
			} else {
				break ;
			}
		} while( --count > 0 ) ;
		//if( ! solvable() ) throw new AssertionError("Maze is unsolvable") ;
	}
	
	void wall( int i, int j, int len, int di, int dj, boolean horiz, boolean build ) {
		// Pre: if horiz then 0 < i && i < ROWS
		// Pre: if horiz then di==0 && dj in {-1,+1}
		// Pre: if !horiz then 0 < j && j < COLS
		// Pre: if !horiz then dj==0 && di in {-1,+1}
		// These preconditions ensure we do not destroy the 4 boundary walls.
		// Effect: Builds or destroys a wall starting at the top left corner of 
		// block (i,j) and extending in direction (di,dj)
		// until it meets a boundary wall or until its length is len whichever comes first.
		int k = 0 ;
		while( k < len &&  (horiz ? (0<=j && j < COLS) : (0<=i && i < ROWS) ) ) {
			if( horiz ) hwall[i][j] = build ;
			else vwall[i][j] = build ;
			i += di ; j += dj ; k += 1 ; }
	}
	
	void drawInitialWalls(int initWalls){
		for( int i=0 ; i<initWalls ; ++i ) drawAWall() ;
	}
	
	void setRat(Rat rat) { this.rat = rat ; }
	
	Maze(int rows, int cols) {
		this.ROWS = rows ;
		this.COLS = cols ;
		
		this.block = new Block[ROWS][COLS] ;
		for( int i = 0 ; i < ROWS ; ++i )
			for( int j = 0 ; j < COLS ; ++j ) block[i][j] = new Block(i,j) ;
		
		this.MAXWALLLEN = Math.min(rows/2, cols/2) ;
		this.MINWALLLEN = 1 ;
		
		this.goal_i = (int)((rows - 1) * 0.8) ;
		this.goal_j = (int)((cols - 1) * 0.8);
		hwall = new boolean[ROWS+1][COLS] ;
		vwall = new boolean[ROWS][COLS+1] ;
		
		// Build the 4 boundary walls.
		for( int j=0 ; j < COLS ; ++j ) hwall[0][j] = hwall[ROWS][j] = true ;
		for( int i=0 ; i < ROWS ; ++i ) vwall[i][0] = vwall[i][COLS] = true ;
	}
	
	private long local_time = 0 ; 
	
	void pulse( long delta_t ) {
		local_time += delta_t ;
		if( local_time > 300 ) {
			drawAWall() ;
			local_time = 0 ;  }
	}
}
