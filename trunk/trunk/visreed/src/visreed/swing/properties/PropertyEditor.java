/**
 * PropertyEditor.java
 * 
 * @date: Sep 27, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing.properties;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;

/**
 * @author Xiaoyu Guo
 *
 */
public class PropertyEditor extends JTable {
	private static final long serialVersionUID = -5123551319497838990L;

	private PropertyTableModel model;
	
	public PropertyEditor(PropertyTableModel tm) {
		super(tm);
		this.model = tm;
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

}
