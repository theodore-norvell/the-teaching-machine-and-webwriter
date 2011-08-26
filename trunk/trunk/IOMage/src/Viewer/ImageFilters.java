/**
 * Create on October 22, 2006 
 */
package Viewer;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @author Yan Zhang
 *
 */
public class ImageFilters extends FileFilter{
	private String postFix;
	
	/**
	 * Create a file filter for select a certain format of the image files
	 */
	public ImageFilters() {
		postFix = new String();
	}

	/**
	 * Get the file extensions
	 * @param f selected file
	 * @return the extension of this file
	 */
	public String getPostFix(File f) {
		String name = f.getName();
		String postfix = null;
		int i = name.lastIndexOf(".");
		if (i > 0 && i < name.length() - 1) {
			postfix = name.substring(i + 1).toLowerCase();
		}
		return postfix;
	}

	@Override
	/**
	 * Accept the file with the certain extensions or with the null extension
	 */
	public boolean accept(File arg0) {
		// TODO Auto-generated method stub
		if (arg0.isDirectory()) {
			return true;
		}
		if (postFix==null) {
			return true;
		}
		String str = getPostFix(arg0);
		if (str!=null && str.equals(postFix) ) {
			return true;
		}
		return false;
	}

	@Override
	/**
	 * Return the description of the file chooser
	 */
	public String getDescription() {
		// TODO Auto-generated method stub
		return "All "+postFix+ " Files";
	}
	
	/**
	 * Add additional type 
	 * @param str added postfix for filechooser 
	 */
	public void addPostFix(String str) {
		postFix=str;
	}

}
