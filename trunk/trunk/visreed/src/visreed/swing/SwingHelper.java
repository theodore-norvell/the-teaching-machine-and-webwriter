/**
 * SwingHelper.java
 * 
 * @date: 2011-6-16
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * SwingHelper provides several common implementations of swing.
 * @author Xiaoyu Guo
 */
public class SwingHelper {
    /**
     * Apply System Look and Feel to current program. 
     */
    public static void setSystemLookAndFeel(){
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName()
            );
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (UnsupportedLookAndFeelException e) {
        }
    }
    
    /**
     * Loads an icon from the resource.
     * @param filename the file name of the icon
     * @return an {@link java.swing.ImageIcon} object, or {@value null}
     */
    public static ImageIcon loadIcon(String filename){
        return new ImageIcon(SwingHelper.class.getResource(filename));
    }
    
    /**
     * Loads an icon from the resource.
     * @param filename the file name of the icon
     * @param desc the description
     * @return an {@link java.swing.ImageIcon} object, or {@value null}
     */
    public static ImageIcon loadIcon(String filename, String desc){
        return new ImageIcon(SwingHelper.class.getResource(filename), desc);
    }
}
