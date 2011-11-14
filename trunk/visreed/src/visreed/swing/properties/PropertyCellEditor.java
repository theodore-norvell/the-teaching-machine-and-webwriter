/**
 * SpinnerCellEditor.java
 * 
 * @date: Nov 8, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing.properties;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

/**
 * @author Xiaoyu Guo
 *
 */
public class PropertyCellEditor extends DefaultCellEditor{
	private static final long serialVersionUID = 6613746311366108278L;
	private DefaultCellEditor defaultEditor;
	
	public PropertyCellEditor(){
		 super(new JTextField());
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(
		JTable table, 
		Object value,
		boolean isSelected, 
		int row, 
		int column
	) {
		TableModel tm = table.getModel();
		Component result = null;
		Component editor = null;
		if(tm instanceof PropertyTableModel){
			editor = ((PropertyTableModel)tm).getEditor(row, column);
			
		}
		
		if(result == null){
			result = super.getTableCellEditorComponent(table, value, isSelected, row, column);
		}
		return result;
	}

}
