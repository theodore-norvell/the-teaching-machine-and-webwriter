public class MovableDisk {
	
	/** Class invariant:
	 * <ul>
	 *  <li><code>0 &lt;= radius </code>
	 *  <li><code>Double.NEGATIVE_INFINITY &lt; centreX-radius</code>
	 *  <li><code>centreX+radius &lt; Double.POSITIVE_INFINITY</code>
	 *  <li><code>Double.NEGATIVE_INFINITY &lt; centreY-radius</code>
	 *  <li><code>centreY+radius &lt; Double.POSITIVE_INFINITY</code>
	 *  </ul>
	 */
	private double centreX = 0.0 ;
	private double centreY = 0.0 ;
	private double radius = 0.0 ;
	
	/** Set the centre of the disk to (x,y)
	 * <p>
	 * <b>Precondition:</b> 
	 * <ul>
	 *  <li><code>Double.NEGATIVE_INFINITY &lt; x-radius</code>
	 *  <li><code>x+radius &lt; Double.POSITIVE_INFINITY</code>
	 *  <li><code>Double.NEGATIVE_INFINITY &lt; y-radius</code>
	 *  <li><code>y+radius &lt; Double.POSITIVE_INFINITY</code>
	 *  </ul>
	 *  <b>Effect:</b> Changes the centre point to (x,y)
	 * @param x The new value for the x coord of the centre.
	 * @param y The new value for the y coord of the centre.
	 */
	public void setCentre( double x, double y ) {
		centreX = x ; centreY = y ; }
	
	
	/** Set the radius of the disk
	 * <p>
	 * <b>Precondition:</b> 
	 * <ul>
	 *  <li><code>0 &lt;= newRadius </code>
	 *  <li><code>Double.NEGATIVE_INFINITY &lt; centreX-newRadius</code>
	 *  <li><code>centreX+newRadius &lt; Double.POSITIVE_INFINITY</code>
	 *  <li><code>Double.NEGATIVE_INFINITY &lt; centreY-newRadius</code>
	 *  <li><code>centreY+newRadius &lt; Double.POSITIVE_INFINITY</code>
	 *  </ul>
	 *  <b>Effect:</b> Changes the radius to newRadius.
	 * @param radius The new value for the radius.
	 */
	public void setRadius( double newRadius ) {
		radius = newRadius ; }
	
	/** Does the disk contain point (x,y)
	 * <p>
	 *  <b>Effect:</b> No change.
	 *  @return true if (x,y) is within the disk or on its edge;
	 *  otherwise, false.
	 * @param x -- the x coordinate of a point
	 * @param y -- the y coordinate of a point
	 */
	public boolean contains( double x, double y ) {
		double deltaX = centreX - x ;
		double deltaY = centreY - y ;
		double dist = Math.sqrt( deltaX*deltaX + deltaY*deltaY ) ;
		return dist <= radius ;
	}
}
