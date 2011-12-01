/**
 * ExampleNodeToolBar.java
 * 
 * @date: Nov 30, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.example.swing.nodebar;

import visreed.extension.example.tag.ExampleTag;
import visreed.model.VisreedWholeGraph;
import visreed.swing.nodebar.VisreedNodeToolBar;
import visreed.swing.nodebar.VisreedNodeToolBarIconData;

/**
 * @author Xiaoyu Guo
 *
 */
public class ExampleNodeToolBar extends VisreedNodeToolBar {
	private static final long serialVersionUID = 5461959946471698676L;

	private static final VisreedNodeToolBarIconData[] PREDEFINED_DATA = {
		new VisreedNodeToolBarIconData("Apple", 	 "/images/toolbar_node_apple.png",     	 ExampleTag.APPLE),
		new VisreedNodeToolBarIconData("Strawberry", "/images/toolbar_node_strawberry.png",  ExampleTag.STRAWBERRY),
	};
	
	/**
	 * @param wg
	 * @param data
	 */
	public ExampleNodeToolBar(VisreedWholeGraph wg) {
		super(wg, PREDEFINED_DATA);
	}

}
