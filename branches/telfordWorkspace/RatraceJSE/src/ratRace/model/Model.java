package ratRace.model;

import java.util.Observable;


public class Model extends Observable {
	private Maze maze ;
	private Rat rat ;
	private final long NOTIME = -1 ;
	private boolean paused = true ;
	private long timeOfLastPulse = NOTIME;
	private boolean gameOver;
	private final int rows ;
	private final int cols ;
	private final long startTime ;
	private long elapsedTime ;
	
	public Model(int rows, int cols) {
		this.rows = rows ; 
		this.cols = cols ;
		this.maze =  new Maze(rows, cols) ;
		this.rat = new Rat( maze ) ;
		this.maze.drawInitialWalls( 40 ) ;
		this.startTime = System.currentTimeMillis() ;
	}
	
	public boolean gameOver( ) { return gameOver ; }
	
	public int getMazeWidth()  { return maze.getMazeWidth() ; }
	
	public int getMazeHeight() { return maze.getMazeHeight() ; }
	
	public double getRat_i() { return rat.get_i() ; }
	
	public double getRat_j() { return rat.get_j() ; }

	public double getRatRadius() { return rat.radius ; }

	public int getGoal_i() { return maze.goal_i() ; }
	
	public int getGoal_j() { return maze.goal_j() ; }
	
	public boolean hasHWall(int i, int j) { return maze.hasHWall(i, j) ; }

	public boolean hasVWall(int i, int j) { return maze.hasVWall(i, j) ; }
	
	public double elapsedTime() { return elapsedTime/1000.0 ; }

	public void pointRatAt(double i, double j) {
		rat.pointAt(i,j) ;
	} 
	
	public void setPaused( boolean paused ) {
		this.paused = paused ;
		timeOfLastPulse = NOTIME ;
		setChanged() ;
		notifyObservers() ;
	}
	
	public boolean getPaused() { return paused ; }
	
	public void pulse() {
		if( paused ) return ;
		long time = System.currentTimeMillis() ;
		if( ! gameOver ) this.elapsedTime = time - startTime ;
		long delta_t = timeOfLastPulse == NOTIME ? 0 : time - timeOfLastPulse ;
		timeOfLastPulse = time ;
		maze.pulse( delta_t ) ;
		rat.pulse( delta_t ) ;
		if( rat.getBlock_i(rat.get_i())==maze.goal_i()
				&& rat.getBlock_j(rat.get_j())==maze.goal_j() ) {
			gameOver = true ;
		}
		setChanged() ;
		//notifyObservers() ;
	}
}
