//     Copyright 1998--2011 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

/*
 * Created on Jun 29, 2011 by Theodore S. Norvell. 
 */
package higraph.utilities;

import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.tools.Diagnostic;

import tm.utilities.Assert;

public class Geometry {
    
    /** The length of a Shape as defined by its path iterator.
     * 
     * @param shape
     * @return the approximate length
     */
    static public double length( Shape shape ) {
        PathIterator iterator = shape.getPathIterator(null) ;

        double length = 0, x0=0, y0=0 ;
        double xmr = 0, ymr = 0 ;
        while( ! iterator.isDone() ) {

            double[] c = new double[6];
            int type = iterator.currentSegment(c);
            switch( type ) {
            case PathIterator.SEG_MOVETO:
                x0 = c[0];
                y0 = c[1];
                xmr = x0;
                ymr = y0 ;
                break;
            case PathIterator.SEG_LINETO: {
                // A straight line from (x0,y0) to (x1,y1).
                double x1 = c[0], y1=c[1], dx = x0-x1, dy = y0-y1 ;
                length += Math.sqrt(dx*dx + dy*dy) ;
                x0 = x1;
                y0 = y1;
            } break;
            case PathIterator.SEG_QUADTO: {
                double x1 = c[0], y1 = c[1], x2 = c[2], y2 = c[3] ;
                // A quadratic Bezier curve from p0=(x0,x1) to p2=(x2,y2) with control point p1=(x1,y1).
                // The locus is p(t) = (1-t)^2 p0 + 2 (1-t) t p1 + t^2 p2,  with t in [0,1]
                
                // Evaluate the polynomial at N+1 points and add the
                // lengths of the resulting N segments. This gives an
                // approximate length for the Bezier curve.
                int N = 10 ;
                double delta_t = 1.0/N ; 
                double xp = x0, yp=y0 ;
                for( double t = delta_t ; t < 1.0+delta_t/2.0 ; t += delta_t ) {
                    double xp1 = (1-t)*(1-t)*x0 + 2*t*(1-t)*x1 + t*t*x2 ;
                    double yp1 = (1-t)*(1-t)*y0 + 2*t*(1-t)*y1 + t*t*y2 ;
                    double dx = xp-xp1, dy = yp-yp1 ;
                    length += Math.sqrt(dx*dx + dy*dy) ;
                    xp = xp1;
                    yp = yp1;
                }
                x0 = x2;
                y0 = y2;
            } break;
            case PathIterator.SEG_CUBICTO: {
                double x1 = c[0], y1 = c[1], x2 = c[2], y2 = c[3], x3 = c[4], y3=c[5] ;
                // A cubic Bezier curve from p0=(x0,x1) to p3=(x3,y3) with control points p1=(x1,y1) and p2=(x2,y2)
                // The locus is p(t) = (1-t)^3 p0 + 3 (1-t)^2 t p1 + 3 (1-t) t^2 p2 + t^3 p3,  with t in [0,1]
                
                // Evaluate the polynomial at N+1 points and add the
                // lengths of the resulting N segments. This gives an
                // approximate length for the Bezier curve.
                int N = 10 ;
                double delta_t = 1.0/N ; 
                double xp = x0, yp=y0 ;
                for( double t = delta_t ; t < 1.0+delta_t/2.0 ; t += delta_t ) {
                    double xp1 = (1-t)*(1-t)*(1-t)*x0 + 3*(1-t)*(1-t)*t*x1
                    + 3*(1-t)*t*t*x2 + t*t*t*x3 ;
                    double yp1 = (1-t)*(1-t)*(1-t)*y0 + 3*(1-t)*(1-t)*t*y1
                    + 3*(1-t)*t*t*y2 + t*t*t*y3 ;
                    double dx = xp-xp1, dy = yp-yp1 ;
                    length += Math.sqrt(dx*dx + dy*dy) ;
                    xp = xp1;
                    yp = yp1;
                }
                x0 = x3;
                y0 = y3;
            } break;
            case PathIterator.SEG_CLOSE: {
                double dx = x0-xmr, dy = y0-ymr ;
                length += Math.sqrt(dx*dx + dy*dy) ;
                x0 = xmr;
                y0 = ymr;
            } break ;
            default: Assert.check(false) ;}
            iterator.next(); }
        return length ;
    }
    
    /** Return a point along the path defined by a shape.
     * The result is approximate and is not guaranteed to be exactly on the path.
     * 
     * @param shape A Shape object that defines the path.
     * @param p A number in the closed interval [0,1] representing the distance along the path that the point is to be found as a fraction of the length of the path.
     * @return a point approximately p along the path.
     */
    static public Pair<Point2D,Double> pointAlongPath( Shape shape, double p ) {
        PathIterator iterator = shape.getPathIterator(null) ;
        
        double totalLength = length( shape ) ;
        double targetLength = totalLength * p ;

        double length = 0, x0=0, y0=0, x0p = 0, y0p = 0 ;
        double xmr = 0, ymr = 0 ; // Most recently moved to point.
        while( ! iterator.isDone() ) {

            double[] c = new double[6];
            int type = iterator.currentSegment(c);
            switch( type ) {
            case PathIterator.SEG_MOVETO:
                x0p = x0 ; y0p = y0 ;
                x0 = c[0];
                y0 = c[1];
                xmr = x0;
                ymr = y0 ;
                break;
            case PathIterator.SEG_LINETO: {
                // A straight line from (x0,y0) to (x1,y1).
                double x1 = c[0], y1=c[1], dx = x0-x1, dy = y0-y1 ;
                double segLen = Math.sqrt(dx*dx + dy*dy) ;
                double newLen = length + segLen ;
                if( newLen >= targetLength ) {
                    Point2D locn =  interpolate(x0, y0, x1, y1, (targetLength - length), segLen ) ;
                    double angle = direction( x0, y0, x1, y1 ) ;
                    return new Pair<Point2D,Double>( locn, angle ) ;
                }
                length = newLen ;
                x0p = x0 ; y0p = y0 ;
                x0 = x1;
                y0 = y1;
            } break;
            case PathIterator.SEG_QUADTO: {
                double x1 = c[0], y1 = c[1], x2 = c[2], y2 = c[3] ;
                // A quadratic Bezier curve from p0=(x0,x1) to p2=(x2,y2) with control point p1=(x1,y1).
                // The locus is p(t) = (1-t)^2 p0 + 2 (1-t) t p1 + t^2 p2,  with t in [0,1]

                // Evaluate the polynomial at N+1 points and add the
                // lengths of the resulting N segments. This gives an
                // approximate length for the bezier curve.
                // If along the way we pass or equal the target length, then a linear
                // interpolation gives the approximate position of the point.
                int N = 10 ;
                double delta_t = 1.0/N ; 
                double xp = x0, yp=y0 ;
                for( double t = delta_t ; t < 1.0+delta_t/2.0 ; t += delta_t ) {
                    double xp1 = (1-t)*(1-t)*x0 + 2*t*(1-t)*x1 + t*t*x2 ;
                    double yp1 = (1-t)*(1-t)*y0 + 2*t*(1-t)*y1 + t*t*y2 ;
                    double dx = xp-xp1, dy = yp-yp1 ;
                    double segLen = Math.sqrt(dx*dx + dy*dy) ;
                    double newLen = length + segLen ;
                    if( newLen >= targetLength ) {
                        Point2D locn =  interpolate(xp, yp, xp1, yp1, (targetLength - length), segLen ) ;
                        double angle = direction( xp, yp, xp1, yp1 ) ;
                        return new Pair<Point2D,Double>( locn, angle ) ;
                    }
                    length = newLen ;
                    xp = xp1;
                    yp = yp1;
                }
                x0 = x2;
                y0 = y2;
            } break;
            case PathIterator.SEG_CUBICTO: {
                double x1 = c[0], y1 = c[1], x2 = c[2], y2 = c[3], x3 = c[4], y3=c[5] ;
                // A cubic Bezier curve from p0=(x0,x1) to p3=(x3,y3) with control points p1=(x1,y1) and p2=(x2,y2)
                // The locus is p(t) = (1-t)^3 p0 + 3 (1-t)^2 t p1 + 3 (1-t) t^2 p2 + t^3 p3,  with t in [0,1]
                
                // Evaluate the polynomial at N+1 points and add the
                // lengths of the resulting N segments. This gives an
                // approximate length for the bezier curve.
                // If along the way we pass or equal the target length, then a linear
                // interpolation gives the approximate position of the point.
                int N = 10 ;
                double delta_t = 1.0/N ; 
                double xp = x0, yp=y0 ;
                //  System.out.println( "Cubic staring at ("+x0+", "+y0+")" ) ;
                //  System.out.println( "target len. is "+targetLength ) ;
                for( double t = delta_t ; t < 1.0+delta_t/2.0 ; t += delta_t ) {
                    
                    //  System.out.println( "  Point is ("+xp+", "+yp+")" ) ;
                    double xp1 = (1-t)*(1-t)*(1-t)*x0 + 3*(1-t)*(1-t)*t*x1
                    + 3*(1-t)*t*t*x2 + t*t*t*x3 ;
                    double yp1 = (1-t)*(1-t)*(1-t)*y0 + 3*(1-t)*(1-t)*t*y1
                    + 3*(1-t)*t*t*y2 + t*t*t*y3 ;
                    //  System.out.println( "    Next point is ("+xp1+", "+yp1+")" ) ;
                    
                    double dx = xp-xp1, dy = yp-yp1 ;
                    double segLen = Math.sqrt(dx*dx + dy*dy) ;
                    //  System.out.println( "    Seg len is "+segLen ) ;
                    double newLen = length + segLen ;
                    //  System.out.println( "    New len is "+newLen ) ;
                    if( newLen >= targetLength ) {
                        Point2D locn =  interpolate(xp, yp, xp1, yp1, (targetLength - length), segLen ) ;
                        double angle = direction( xp, yp, xp1, yp1 ) ;
                        //  System.out.println( "  locn is ("+locn.getX()+", "+locn.getY()+")" ) ;
                        //  System.out.println( "  angle is "+angle ) ;
                        return new Pair<Point2D,Double>( locn, angle ) ;
                    }
                    length = newLen ;
                    xp = xp1;
                    yp = yp1;
                }
                x0 = x3;
                y0 = y3;
            } break;
            case PathIterator.SEG_CLOSE: {
                // A line back to the most recently moved-to point (xmr,ymr) 
                double dx = x0-xmr, dy = y0-ymr ;
                double segLen = Math.sqrt(dx*dx + dy*dy) ;
                double newLen = length + segLen ;
                if( newLen >= targetLength ) {
                        Point2D locn =  interpolate(x0, y0, xmr, ymr, (targetLength - length), segLen ) ;
                        double angle = direction( x0, y0, xmr, ymr ) ;
                        return new Pair<Point2D,Double>( locn, angle ) ;
                }
                length = newLen ;
                x0 = xmr;
                y0 = ymr;
            } break ;
            default: Assert.check(false) ;}
            iterator.next(); }
        // If we still haven't got the point, use the last point on the path
        Point2D locn = new Point2D.Double(x0, y0) ;
        double angle = 0 ;
        return new Pair<Point2D,Double>( locn, angle ) ; 
    }

    public static double direction(double x0, double y0, double x1, double y1) {
        return Math.atan2(y1-y0, x1-x0);
    }

    /** Interpolate between (x0,y0) and (x1,y1) to a point dist/segLen along the line. */
    private static Point2D interpolate(double x0, double y0, double x1,
            double y1, double dist, double segLen) {
        // System.out.println("interpolate("+x0+", "+y0+", "+x1+", "+y1+", "+dist+", "+segLen+")") ;
        if( segLen == 0 ) return new Point2D.Double(x0, y0) ;
        double x = x0 + (x1-x0)*dist/segLen ;
        double y = y0 + (y1-y0)*dist/segLen ;
        // System.out.println("<<interpolate ("+x+", "+y+")" ) ;
        return new Point2D.Double(x,y) ;
    }   
    
    /** Suppose we have a rectangle with its centre point on the x axis.
     * It is titled so that its top and bottom sides are at an angle of alpha to the x axis.
     * How far up (in direction of increasing "y") do we need to raise it so that it is just clear of the x axis.
     * @param height The (positive) height of the rectangle, i.e., the distance from bottom side to top side.
     * @param width The (positive) width of the rectangle, i.e., the length of the top and bottom side.
     * @return A positive number representing the amount to raise the rectangle so that its lowest corner is on the x-axis.
     */
    public static double raiseBoxToClearLine( double height, double width, double alpha ) {
        // Note on coords: The term upper here means largest "y". Because of symmetry, this makes
        // no difference to the answer, but, it explains the terminology of comments & var names.
        
        // Let beta be the angle of the line drawn from the lower left corner to the
        // upper-right corner, if the rectangle were horizontal.
        double beta = Math.atan2(height, width) ;
        // But the rectangle is at an angle of theta. So its diagonal from lower left corner to
        // the upper-right corner is really at an angle of
        double gamma = alpha + beta ;
        // And the line from the upper left corner to the lower right corner is at an angle of
        double delta = alpha - beta ;
        //The length of each diagonal is 
        double diag = Math.sqrt( height*height + width*width ) ;
        // Now the height of the upper right corner is
        double hur = Math.sin(gamma) * 0.5 * diag ;
        // The height of the lower left corner is
        double hll = - hur ;
        // The height of the lower right corner is
        double hlr = Math.sin(delta) * 0.5 * diag ;
        // The height of the upper left corner is
        double hul = - hlr ;
        // The smallest height is
        double min = Math.min(Math.min(hur,hll), Math.min(hlr,hul)) ;
        // , so we need to raise the box by
        return -min ;
    }
    
    
    /** Consider a ray rooted at inPt and passing through another point outPt.
     *  If inPt is contained within a closed shape, the ray must pass through the boundary
     *  of the shape at least once. This routine finds a point within one pixel of such a boundary point.
     *  Note that if outPt is very close to inPt, outPt is ignored and a random direction is chosen for the ray.
     * 
     * @param closedShape a closed shape.
     * @param inPt  a point contained in closedShape
     * @param outPt another point
     * @return
     */
    public static Point2D.Double getIntersection(Shape closedShape, Point2D.Double inPt, Point2D.Double outPt) {
        // To be on the safe side, copy the argument points so we can change them
        inPt = new Point2D.Double(inPt.x, inPt.y) ;
        outPt = new Point2D.Double(outPt.x, outPt.y) ; 
        Assert.check(closedShape.contains(inPt), "inPt is not inside the shape");
        final double FUZZ = 0.0000001 ;
        
        // Let delta = outPt - inPt . So Delta defines the direction of the ray and
        // its magnitude is the distance from inPt to outPt. This relationship
        // remains invariant from here on.
        Point2D.Double delta = new Point2D.Double(outPt.x - inPt.x, outPt.y - inPt.y);
        while( Math.abs(delta.x) <= FUZZ && Math.abs(delta.y) <= FUZZ ) {
            // The second point is too close. Take another point in a random direction.
            delta.x = 2.0*Math.random() - 1.0 ;
            delta.y = 2.0*Math.random() - 1.0 ;
            outPt.x = inPt.x + delta.x ;
            outPt.y = inPt.y + delta.y ;
        }
        
        // If outPt is already on the outside, then all is good.
        // But if outPt is on the inside then we need need to move it along the ray until it
        // is outside. We do this by repeatedly doubling the distance from inPt to outPt.
        // Invariant inPt + delta equals outPt
        while( closedShape.contains(outPt) ) {
            delta.x = 2*delta.x ;
            delta.y = 2*delta.y ;
            outPt.x = inPt.x + delta.x ;
            outPt.y = inPt.y + delta.y ; }
        // Now outPt is on the outside. Use binary search to find a point close to the boundary.
        // Note that if the ray crosses the boundary more than once, the result is not guaranteed
        // to be close to any particular crossing.
        // Invariant: closedShape.contains(inPt) && ! closedShape.contains(outPt) && inPt + delta equals outPt
        while( Math.abs( delta.x ) > 1.0 || Math.abs(delta.y) > 1.0 ) {
            // Find the midpoint
            Point2D.Double midpoint = new Point2D.Double( inPt.x + delta.x/2.0, inPt.y + delta.y/2.0  ) ;
            if( closedShape.contains(midpoint) ) {
                inPt = midpoint ; }
            else {
                outPt = midpoint ; }
            delta.x = outPt.x - inPt.x ;
            delta.y = outPt.y - inPt.y ; }
        return outPt ;
    }
}
