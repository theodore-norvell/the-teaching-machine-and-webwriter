/**
 * PropertyTableModel.java
 * 
 * @date: Nov 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing.properties;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

/**
 * This table model reads all the @Property annotations on fields in the given
 * object, and change that into a editor.
 * @author Xiaoyu Guo
 */
public class PropertyTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -7035786389506806534L;
	private static final String[] COLUMN_NAMES = {"Property", "Value"};
	
	private Object model;
	private List<String> fields;
	private List<JComponent> editors; 
	
	public PropertyTableModel(Object object){
		model = object;
		fields = new ArrayList<String>();
		editors = new ArrayList<JComponent>();

		if(object != null){
			extractAnnotation(model);
		}
	}
	
	/**
	 * @param object
	 */
	private void extractAnnotation(Object object) {
		Class<?> objClass = object.getClass();
		Field[] fields = objClass.getDeclaredFields();
		for(Field f : fields){
			boolean present = f.isAnnotationPresent(Property.class);
			if(present){
				Property p = f.getAnnotation(Property.class);
				
				// try to get the value of the field
				Object value = null;
				try {
					value = f.get(object);
				} catch (IllegalAccessException e){
					String getter = p.getter();
					if(getter.length() == 0){
						// default getter not specified
						if(p.name().length() == 0){
							// name not specified, use field name
							getter = "get" 
									+ Character.toUpperCase(f.getName().charAt(0)) 
									+ f.getName().substring(1);
						} else {
							getter = "get" + p.name();
						}
					}
					
					try {
						Method getterMethod = objClass.getDeclaredMethod(getter);
						value = getterMethod.invoke(object);
					} catch (Exception e1) {
						value = null;
					}
				}
				
				this.fields.add(p.name());
				this.editors.add(createEditor(p, value));
				
			}
		}
	}

	/**
	 * Creates an editor with the specified parameters 
	 * @param p
	 * @param value the current value
	 * @return
	 */
	private JComponent createEditor(Property p, Object value) {
		JComponent result = null;
		// extract value
		String stringValue = (value == null) ? "null" : value.toString();
		int intValue = 0;
		try {
			intValue = (Integer)value;
		} catch (Exception e) {
		}
		
		boolean boolValue = false;
		try{
			boolValue = (Boolean)value;
		} catch (Exception e){
		}
		
		switch(p.editor()){
		case NONE:
			result = null;
			break;
		case BOOLEAN:
			JCheckBox chb = new JCheckBox();
			chb.setSelected(boolValue);
			break;
		case INT:
			result = new JSpinner(
				new SpinnerNumberModel(
					intValue,
					p.minValue(),
					p.maxValue(),
					1
				)
			);
			
			break;
		case LIST:
			JComboBox cob = new JComboBox();
			cob.setEditable(false);
			
			for(String item : p.list()){
				cob.addItem(item);
			}
			// select appropriate item
			cob.setSelectedItem(stringValue);
			
			result = cob;
			break;
		case TEXT:
		default:
			result = new JTextField(stringValue);
			break;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return fields.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column){
		if(column >= 0 && column < COLUMN_NAMES.length){
			return COLUMN_NAMES[column];
		} else {
			return "";
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(columnIndex == 0){
			return fields.get(columnIndex);
		} else if (columnIndex == 1){
			return editors.get(rowIndex);
		} else {
			return null;
		}
	}

	
	public JComponent getEditor(int row, int column){
		if(column == 1){
			Object editor = editors.get(column);
			return (JComponent) editor;
		}
		return null;
	}
}
