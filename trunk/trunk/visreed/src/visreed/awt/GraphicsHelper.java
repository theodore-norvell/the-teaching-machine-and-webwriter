/**
 * GraphicsHelper.java
 * 
 * @date: 2011-5-30
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.awt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * This is a helper class of dealing with AWT graphics
 * @author Xiaoyu Guo
 */
public class GraphicsHelper {
    
    /**
     * To compare two pixels
     */
    private static double COMPARISON_EPSILON = 0.1;
    
    /** The minimum radius of the curve in connections */
    private static double CONNECTION_CURVE_RADIUS_PIXEL = 10;
    
    private static double ARROWHEAD_LENGTH_PIXEL = 10;
    
    /**
     * To determine whether two coords are different (in visual)
     * @param a one coord
     * @param b another coord
     * @return true if the two values are near enough, false otherwise.
     */
    private static boolean different(double a, double b){
        if(a - b > COMPARISON_EPSILON || a - b < -COMPARISON_EPSILON){
            return true;
        }
        return false;
    }
    
    /**
     * Turn on anti aliasing feature
     * @param g the {@link java.awt.Graphics2D} object
     */
    public static Graphics2D getAntiAliasingGraphic(Graphics2D g){
        Graphics2D gs = (Graphics2D)g;
        gs.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        gs.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );
        return gs;
    }
    
    /**
     * Paint a centered string.
     * <strong>Note: the font and color must be set before calling this</strong>
     * @param g the {@link java.awt.Graphics2D} to paint
     * @param text the text to paint
     * @param centerX the center of the x axis
     * @param centerY the center of the y axis
     */
    public static void paintCenteredString(
        Graphics2D g,
        String text, 
        double centerX, double centerY
    ){
        FontRenderContext frc = g.getFontRenderContext();
        Rectangle2D bounds = g.getFont().getStringBounds(text, frc);
        float actualWidth = (float) bounds.getWidth();
        float actualHeight = (float) bounds.getHeight();
        
        g.drawString(
            text, 
            (float)(centerX - actualWidth / 2.0F),
            (float)(centerY + actualHeight / 4.0F)
        );
    }
    
    
    /**
     * Fill a oval.
     * <strong>Note: the color must be set before calling this</strong>
     * @param g the {@link java.awt.Graphics2D} to paint
     * @param center the center of the oval {@link java.awt.geom.Point2D} 
     * @param radius the radius of the oval (in pixels)
     */
    public static void fillOval(
        Graphics2D g,
        Point2D center,
        double radius
    ){
        g.fillOval(
            (int)(center.getX() - radius),
            (int)(center.getY() - radius),
            (int)(radius * 2),
            (int)(radius * 2)
        );
    }
    
    /**
     * Gets the gradient color used in GradientPaint <br />
     * Rule: <br /> 
     * white (255, 255, 255) -> white (255, 255, 255)<br />
     * black (  0,   0,   0) -> grey  ( 50,  50,  50)<br />
     * blue  (  0,   0, 255) -> brighter blue (75, 75, 255)
     * @param baseColor the base color
     * @return
     */
    public static Color getGradientColor(Color baseColor){
        if(baseColor == null){
            return null;
        }
        int newRed = Math.min(baseColor.getRed() + 100, 255);
        int newGreen = Math.min(baseColor.getGreen() + 100, 255);
        int newBlue = Math.min(baseColor.getBlue() + 100, 255);
        
        Color result = new Color(newRed, newGreen, newBlue);
        return result;
    }
    
    /**
     * Paint a string at the specified top left point
     * @param g the {@link java.awt.Graphics2D} to paint
     * @param text the text to paint
     * @param leftX the top of the x axis
     * @param topY the left of the y axis
     */
    public static void paintStringOnTopLeft(
        Graphics2D g,
        String text,
        double leftX, double topY
    ){
        FontRenderContext frc = g.getFontRenderContext();
        Rectangle2D bounds = g.getFont().getStringBounds(text, frc);
        float actualHeight = (float) bounds.getY();
        
        g.drawString(
            text, 
            (float)(leftX),
            (float)(topY - actualHeight)
        );
    }
    
    /**
     * Paint an arrowhead 
     * @param g the {@link java.awt.Graphics2D} to paint
     * @param style the arrow style
     * @param angle the direction of the arrow, number in degrees.
     * @param end the ending point
     */
    public static void drawArrow(
        Graphics2D g,
        ArrowStyle style,
        int angle,
        Point2D end
    ){
        Path2D.Double path = createArrow(style);
        AffineTransform at = AffineTransform.getTranslateInstance(
            end.getX(), 
            end.getY()
        );
        at.rotate(Math.toRadians(angle));
        Shape arrowShape = at.createTransformedShape(path);
        g.fill(arrowShape);
    }
    
    /**
     * Paint an arrowhead 
     * @param g the {@link java.awt.Graphics2D} to paint
     * @param style the arrow style
     * @param direction the {@link visreed.awt.ArrowDirection} of the arrow
     * @param end the ending point
     */
    public static void drawArrow(
        Graphics2D g,
        ArrowStyle style,
        ArrowDirection direction,
        Point2D end
    ){
        Path2D.Double path = createArrow(style);
        AffineTransform at = AffineTransform.getTranslateInstance(
            end.getX(), 
            end.getY()
        );
        at.quadrantRotate(
            direction.getNumOfQuadrants()
        );
        Shape arrowShape = at.createTransformedShape(path);
        g.fill(arrowShape);
    }
    
    /**
     * Create an arrowhead facing RIGHT
     * @param style
     * @return
     */
    private static Path2D.Double createArrow(ArrowStyle style){
        Path2D.Double result = new Path2D.Double();
        result.moveTo(0.0, 0.0);
        double offsetX, offsetY;
        
        switch(style){
        case NONE:
            break;
        case DEFAULT:
            // DEFAULT is using ARROW style
        case ARROW:
            offsetX = ARROWHEAD_LENGTH_PIXEL * Math.cos(Math.toRadians(165));
            offsetY = ARROWHEAD_LENGTH_PIXEL * Math.sin(Math.toRadians(150));
            result.lineTo(offsetX, offsetY);
            result.lineTo(-ARROWHEAD_LENGTH_PIXEL / 1.2, 0);
            result.lineTo(offsetX, -offsetY);
            result.lineTo(0.0, 0.0);
            break;
        case THIN:
            // a thinner triangle
            offsetX = -ARROWHEAD_LENGTH_PIXEL * 1.5;
            offsetY = ARROWHEAD_LENGTH_PIXEL / 3.0;
            result.lineTo(offsetX, offsetY);
            result.lineTo(offsetX, -offsetY);
            result.lineTo(0.0, 0.0);
            break;
        case TRIANGLE:
            // a solid triangle
            offsetX = ARROWHEAD_LENGTH_PIXEL * Math.cos(Math.toRadians(150));
            offsetY = ARROWHEAD_LENGTH_PIXEL * Math.sin(Math.toRadians(150));
            result.lineTo(offsetX, offsetY);
            result.lineTo(offsetX, -offsetY);
            result.lineTo(0.0, 0.0);
            break;
        default:
            break;
        }
        return result;
    }
    
    /**
     * Draws a horizontal curve from the start point to the end point
     * This likes:
     * <pre>
     *     o----
     *     |
     * ----| 
     * </pre>
     * @param g the {@link java.awt.Graphics2D} to paint on
     * @param startX the X coord of the starting point
     * @param startY
     * @param endX the X coord of the ending point
     * @param endY
     */
    public static void drawHorizontalConnectionCurve(
        Graphics2D g,
        double startX, double startY,
        double endX, double endY
    ){
        // if y coords are (visually) the same, then draw a line
        if(!different(startY, endY)){
            g.drawLine(
                (int)startX,
                (int)startY,
                (int)endX,
                (int)endY
            );
            return;
        }
        
        // decide the curve radius
        double radius = CONNECTION_CURVE_RADIUS_PIXEL;
        
        // use offset values to normalize the algorithm to TOPLEFT->BOTTOMRIGHT
        double middleX = (startX + endX) / 2.0;
        double offsetX = radius;
        double offsetY = radius;
        int startAngle = 180;
        int drawAngle = -90;
        double leftTopX = middleX;
        double leftTopY = endY;
        if(startX > endX){
            // right to left
            offsetX = -radius;
            startAngle = 180 - startAngle;
            drawAngle = - drawAngle;
            leftTopX = middleX - radius;
        }
        if(startY < endY){
            offsetY = -radius;
            startAngle = 360 - startAngle;
            drawAngle = -drawAngle;
            leftTopY = endY - radius;
        }
        
        offsetX /= 2.0;
        offsetY /= 2.0;
        
        // draw the starting line
        g.drawLine(
            (int)startX,
            (int)startY,
            (int)middleX,
            (int)startY
        );
        
        // draw the conneting line
        g.drawLine(
            (int)middleX,
            (int)startY,
            (int)middleX,
            (int)(endY + offsetY)
        );
        
        // draw the arc
        g.drawArc(
            (int)leftTopX,
            (int)leftTopY,
            (int)radius,
            (int)radius,
            startAngle,
            drawAngle
        );
        
        // draw the ending line
        g.drawLine(
            (int)(middleX + offsetX),
            (int)endY,
            (int)endX,
            (int)endY
        );
    }
    
    /**
     * Draws a vertical curve from the start point to the end point
     * This likes:
     * <pre>
     *         |
     *  o------|
     * | 
     * </pre>
     * @param g the {@link java.awt.Graphics2D} to paint on
     * @param startX the X coord of the starting point
     * @param startY
     * @param endX the X coord of the ending point
     * @param endY
     */
    public static void drawVerticalConnectionCurve(
        Graphics2D g,
        double startX, double startY,
        double endX, double endY
    ){
        // decide the curve radius
        double radius = CONNECTION_CURVE_RADIUS_PIXEL;
        
        // use offset values to normalize the algorithm to TOPLEFT->BOTTOMRIGHT
        double middleY = (startY + endY) / 2.0;
        double offsetX = radius;
        double offsetY = radius;
        int startAngle = 90;
        int drawAngle = -90;
        double leftTopX = endX - radius;
        double leftTopY = middleY;
        if(startX > endX){
            // right to left
            offsetX = -radius;
            drawAngle = - drawAngle;
            leftTopX = endX;
        }
        if(startY < endY){
            // bottom to top
            offsetY = -radius;
            startAngle = 360 - startAngle;
            drawAngle = -drawAngle;
            leftTopY = middleY;
        }
        
        offsetX /= 2.0;
        offsetY /= 2.0;
        
        // draw the starting line
        g.drawLine(
            (int)startX,
            (int)startY,
            (int)middleY,
            (int)startY
        );
        
        // draw the conneting line
        g.drawLine(
            (int)middleY,
            (int)startY,
            (int)middleY,
            (int)(endY + offsetY)
        );
        
        // draw the arc
        g.drawArc(
            (int)leftTopX,
            (int)leftTopY,
            (int)radius,
            (int)radius,
            startAngle,
            drawAngle
        );
        
        // draw the ending line
        g.drawLine(
            (int)(middleY + offsetX),
            (int)endY,
            (int)endX,
            (int)endY
        );
    }
    
    /**
     * Gets an offset point according to 
     * @param src
     * @param px
     * @param py
     * @return
     */
    public static Point2D getOffsetPoint(Point2D src, double px, double py){
        if(src == null){ return null; }
        return new Point2D.Double(
            src.getX() + px,
            src.getY() + py
        );
    }
}
