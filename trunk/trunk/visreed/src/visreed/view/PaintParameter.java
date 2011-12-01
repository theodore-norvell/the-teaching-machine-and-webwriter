/**
 * PaintParameter.java
 * 
 * @date: Nov 21, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;

/**
 * PaintParameter defines a set of parameters in painting.
 * @author Xiaoyu Guo
 */
public class PaintParameter {
	private Color foreColor;
	private Color backColor;
	private Font font;
	private Stroke stroke;
	
	public Color getForeColor() {
		return foreColor;
	}
	public void setForeColor(Color foreColor) {
		this.foreColor = foreColor;
	}
	public Color getBackColor() {
		return backColor;
	}
	public void setBackColor(Color backColor) {
		this.backColor = backColor;
	}
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	public Stroke getStroke() {
		return stroke;
	}
	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}
}
