package tm.plugins;

import java.awt.Toolkit;
import java.io.InputStream;
import java.util.Iterator;

import junit.framework.TestCase;
import javax.swing.JTree ;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import tm.interfaces.ImageSourceInterface;

/** Some tests for the PlugInManagerDialog. */
public class TestPluginManagerDialog  extends TestCase {

    PlugInManager pim = PlugInManager.getSingleton() ;
    PlugInManagerDialog dialog  ;
    
    PlugInRegistration pir0 = new PlugInRegistration("tm.plugins.A:B",
            "tm.plugins.B1Factory", "");
    PlugInRegistration pir1 = new PlugInRegistration("tm.plugins.A:C",
            "tm.plugins.C0Factory", "");
    PlugInRegistration pir2 = new PlugInRegistration("tm.plugins.B1:D",
            "tm.plugins.D0", "") ;
    PlugInRegistration pir3 = new PlugInRegistration("tm.plugins.A:B",
            "tm.plugins.B0Factory","") ;
    
    protected void setUp() {
        pim = PlugInManager.getSingleton() ;
        // Clean it out.
        while( true ) {
            Iterator<PlugInRegistration>  it = pim.iterator() ;
            if( ! it.hasNext() ) break ;
            PlugInRegistration r = it.next() ;
            pim.deRegisterPlugIn( r ) ;
        }
        
        dialog = new PlugInManagerDialog(pim, 
                new ImageSourceInterface() {
                    public java.awt.Image fetchImage(String name) { return null ; } } ) ;
        
        // Add a basic set of plugins.
        
        
        pim.registerPlugIn( pir0 ) ;
        pim.registerPlugIn( pir1 ) ;
        pim.registerPlugIn( pir2 ) ;
        
        dialog.rebuildTree() ;
    }
    
    /** Test the original setup is what I think it is */
    public void test0() {
        // After set up there are three plugin's
        TreeModel treeModel = dialog.theTree().getModel() ;
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot() ;
        
        // Check the level 1 nodes
        assertEquals( root.getChildCount(), 3 ) ;
        DefaultMutableTreeNode ch0 = (DefaultMutableTreeNode) root.getChildAt( 0 ) ;
        DefaultMutableTreeNode ch1 = (DefaultMutableTreeNode) root.getChildAt( 1 ) ;
        DefaultMutableTreeNode ch2 = (DefaultMutableTreeNode) root.getChildAt( 2 ) ;
        assertEquals( ch0.getUserObject(), "tm.plugins.A:B" ) ;
        assertEquals( ch1.getUserObject(), "tm.plugins.A:C" ) ;
        assertEquals( ch2.getUserObject(), "tm.plugins.B1:D" ) ;
        
        // Check the level 2 nodes
        
        assertEquals( ch0.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch0_0 = (DefaultMutableTreeNode) ch0.getChildAt( 0 ) ;
        assertEquals( ch0_0.getUserObject(), pir0 ) ;
        
        assertEquals( ch1.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch1_0 = (DefaultMutableTreeNode) ch1.getChildAt( 0 ) ;
        assertEquals( ch1_0.getUserObject(), pir1 ) ;
        
        assertEquals( ch2.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch2_0 = (DefaultMutableTreeNode) ch2.getChildAt( 0 ) ;
        assertEquals( ch2_0.getUserObject(), pir2 ) ;
        
        // Check the level 3 nodes
        assertEquals( ch0_0.getChildCount(), 1 ) ;
    }
    

    /** Delete first top-level node. */
    public void test1() {
        // After set up there are three plugin's
        // Delete the first.
        pim.deRegisterPlugIn( pir0 ) ;
        dialog.rebuildTree() ;
        TreeModel treeModel = dialog.theTree().getModel() ;
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot() ;
        
        // Check the level 1 nodes
        assertEquals( root.getChildCount(), 2 ) ;
        DefaultMutableTreeNode ch0 = (DefaultMutableTreeNode) root.getChildAt( 0 ) ;
        DefaultMutableTreeNode ch1 = (DefaultMutableTreeNode) root.getChildAt( 1 ) ;
        assertEquals( ch0.getUserObject(), "tm.plugins.A:C" ) ;
        assertEquals( ch1.getUserObject(), "tm.plugins.B1:D" ) ;
        
        // Check the level 2 nodes
        
        assertEquals( ch0.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch0_0 = (DefaultMutableTreeNode) ch0.getChildAt( 0 ) ;
        assertEquals( ch0_0.getUserObject(), pir1 ) ;
        
        assertEquals( ch1.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch1_0 = (DefaultMutableTreeNode) ch1.getChildAt( 0 ) ;
        assertEquals( ch1_0.getUserObject(), pir2 ) ;
    }
   
    /** Delete second top-level node. */
    public void test2() {
        // After set up there are three plugin's
        // Delete the second.
        pim.deRegisterPlugIn( pir1 ) ;
        dialog.rebuildTree() ;
        TreeModel treeModel = dialog.theTree().getModel() ;
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot() ;
        
//      Check the level 1 nodes
        assertEquals( root.getChildCount(), 2 ) ;
        DefaultMutableTreeNode ch0 = (DefaultMutableTreeNode) root.getChildAt( 0 ) ;
        DefaultMutableTreeNode ch1 = (DefaultMutableTreeNode) root.getChildAt( 1 ) ;
        assertEquals( ch0.getUserObject(), "tm.plugins.A:B" ) ;
        assertEquals( ch1.getUserObject(), "tm.plugins.B1:D" ) ;
        
        // Check the level 2 nodes
        
        assertEquals( ch0.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch0_0 = (DefaultMutableTreeNode) ch0.getChildAt( 0 ) ;
        assertEquals( ch0_0.getUserObject(), pir0 ) ;
        
        assertEquals( ch1.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch1_0 = (DefaultMutableTreeNode) ch1.getChildAt( 0 ) ;
        assertEquals( ch1_0.getUserObject(), pir2 ) ;
        
        // Check the level 3 nodes
        assertEquals( ch0_0.getChildCount(), 1 ) ;
    }

    
    /** Delete third top-level node. */
    public void test3() {
        // After set up there are three plugin's
        // Delete the third.
        pim.deRegisterPlugIn( pir2 ) ;
        dialog.rebuildTree() ;
        TreeModel treeModel = dialog.theTree().getModel() ;
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot() ;
        
//      Check the level 1 nodes
        assertEquals( root.getChildCount(), 2 ) ;
        DefaultMutableTreeNode ch0 = (DefaultMutableTreeNode) root.getChildAt( 0 ) ;
        DefaultMutableTreeNode ch1 = (DefaultMutableTreeNode) root.getChildAt( 1 ) ;
        assertEquals( ch0.getUserObject(), "tm.plugins.A:B" ) ;
        assertEquals( ch1.getUserObject(), "tm.plugins.A:C" ) ;
        
        // Check the level 2 nodes
        
        assertEquals( ch0.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch0_0 = (DefaultMutableTreeNode) ch0.getChildAt( 0 ) ;
        assertEquals( ch0_0.getUserObject(), pir0 ) ;
        
        assertEquals( ch1.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch1_0 = (DefaultMutableTreeNode) ch1.getChildAt( 0 ) ;
        assertEquals( ch1_0.getUserObject(), pir1 ) ;
        
        // Check the level 3 nodes
        assertEquals( ch0_0.getChildCount(), 1 ) ;
    }

    
    /** Delete 2 nodes */
    public void test4() {
        // After set up there are three plugin's
        
        // Delete 2
        pim.deRegisterPlugIn( pir0 ) ;
        pim.deRegisterPlugIn( pir1 ) ;
        dialog.rebuildTree() ;
        
        TreeModel treeModel = dialog.theTree().getModel() ;
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot() ;
        
        // Check the level 1 nodes
        assertEquals( root.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch0 = (DefaultMutableTreeNode) root.getChildAt( 0 ) ;
        assertEquals( ch0.getUserObject(), "tm.plugins.B1:D" ) ;
        
        // Check the level 2 nodes
        
        assertEquals( ch0.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch0_0 = (DefaultMutableTreeNode) ch0.getChildAt( 0 ) ;
        assertEquals( ch0_0.getUserObject(), pir2 ) ;
    }
    
    /** Delete 2 nodes */
    public void test5() {
        // After set up there are three plugin's
        // Delete 2
        pim.deRegisterPlugIn( pir1 ) ;
        pim.deRegisterPlugIn( pir2 ) ;
        dialog.rebuildTree() ;
        
        TreeModel treeModel = dialog.theTree().getModel() ;
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot() ;
        
        // Check the level 1 nodes
        assertEquals( root.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch0 = (DefaultMutableTreeNode) root.getChildAt( 0 ) ;
        assertEquals( ch0.getUserObject(), "tm.plugins.A:B" ) ;
        
        // Check the level 2 nodes
        
        assertEquals( ch0.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch0_0 = (DefaultMutableTreeNode) ch0.getChildAt( 0 ) ;
        assertEquals( ch0_0.getUserObject(), pir0 ) ;
    }
    
    /** Test the original setup is what I think it is */
    public void test6() {
        // After set up there are three plugin's
        // Delete 2
        pim.deRegisterPlugIn( pir0 ) ;
        pim.deRegisterPlugIn( pir2 ) ;
        dialog.rebuildTree() ;
        
        TreeModel treeModel = dialog.theTree().getModel() ;
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot() ;
        
        // Check the level 1 nodes
        assertEquals( root.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch0 = (DefaultMutableTreeNode) root.getChildAt( 0 ) ;
        assertEquals( ch0.getUserObject(), "tm.plugins.A:C" ) ;
        
        // Check the level 2 nodes
        
        assertEquals( ch0.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch0_0 = (DefaultMutableTreeNode) ch0.getChildAt( 0 ) ;
        assertEquals( ch0_0.getUserObject(), pir1 ) ;
    }
    
    /** Delete all nodes */
    public void test7() {
        // After set up there are three plugin's

        // Delete all
        pim.deRegisterPlugIn( pir0 ) ;
        pim.deRegisterPlugIn( pir1 ) ;
        pim.deRegisterPlugIn( pir2 ) ;
        dialog.rebuildTree() ;
        
        TreeModel treeModel = dialog.theTree().getModel() ;
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot() ;
        
        // Check the level 1 nodes
        assertEquals( root.getChildCount(), 0 ) ;
    }
    
    /** Delete all nodes. Then put them all back.*/
    public void test8() {
        // After set up there are three plugin's

        // Delete all
        pim.deRegisterPlugIn( pir0 ) ;
        pim.deRegisterPlugIn( pir1 ) ;
        pim.deRegisterPlugIn( pir2 ) ;
        dialog.rebuildTree() ;
        
        // Put them back
        pim.registerPlugIn( pir2 ) ;
        pim.registerPlugIn( pir1 ) ;
        pim.registerPlugIn( pir0 ) ;
        dialog.rebuildTree() ;
        
        TreeModel treeModel = dialog.theTree().getModel() ;
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot() ;
        
        // Check the level 1 nodes
        assertEquals( root.getChildCount(), 3 ) ;
        DefaultMutableTreeNode ch0 = (DefaultMutableTreeNode) root.getChildAt( 0 ) ;
        DefaultMutableTreeNode ch1 = (DefaultMutableTreeNode) root.getChildAt( 1 ) ;
        DefaultMutableTreeNode ch2 = (DefaultMutableTreeNode) root.getChildAt( 2 ) ;
        assertEquals( ch0.getUserObject(), "tm.plugins.A:B" ) ;
        assertEquals( ch1.getUserObject(), "tm.plugins.A:C" ) ;
        assertEquals( ch2.getUserObject(), "tm.plugins.B1:D" ) ;
        
        // Check the level 2 nodes
        
        assertEquals( ch0.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch0_0 = (DefaultMutableTreeNode) ch0.getChildAt( 0 ) ;
        assertEquals( ch0_0.getUserObject(), pir0 ) ;
        
        assertEquals( ch1.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch1_0 = (DefaultMutableTreeNode) ch1.getChildAt( 0 ) ;
        assertEquals( ch1_0.getUserObject(), pir1 ) ;
        
        assertEquals( ch2.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch2_0 = (DefaultMutableTreeNode) ch2.getChildAt( 0 ) ;
        assertEquals( ch2_0.getUserObject(), pir2 ) ;
        
        // Check the level 3 nodes
        assertEquals( ch0_0.getChildCount(), 1 ) ;
    }
    
    /** Two nodes at level 2. */
    public void test9() {
        // After set up there are three plugin's

        // Delete all
        pim.deRegisterPlugIn( pir0 ) ;
        pim.deRegisterPlugIn( pir1 ) ;
        pim.deRegisterPlugIn( pir2 ) ;
        dialog.rebuildTree() ;
        
        // Add two registrations with the same name.
        pim.registerPlugIn( pir0 ) ;
        pim.registerPlugIn( pir3 ) ;
        dialog.rebuildTree() ;
        
        TreeModel treeModel = dialog.theTree().getModel() ;
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot() ;
        
        // Check the level 1 nodes
        assertEquals( root.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch0 = (DefaultMutableTreeNode) root.getChildAt( 0 ) ;
        assertEquals( ch0.getUserObject(), "tm.plugins.A:B" ) ;
        
        // Check the level 2 nodes
        
        assertEquals( ch0.getChildCount(), 2 ) ;
        DefaultMutableTreeNode ch0_0 = (DefaultMutableTreeNode) ch0.getChildAt( 0 ) ;
        DefaultMutableTreeNode ch0_1 = (DefaultMutableTreeNode) ch0.getChildAt( 1 ) ;
        assertEquals( ch0_0.getUserObject(), pir3 ) ;
        assertEquals( ch0_1.getUserObject(), pir0 ) ;
        
        // Check the level 3 nodes
        assertEquals( ch0_0.getChildCount(), 0) ;
        assertEquals( ch0_1.getChildCount(), 1) ;
    }
    
    /** Two Switch nodes at level 3 */
    public void test10() {
        // After set up there are three plugin's

        // Delete all
        pim.deRegisterPlugIn( pir0 ) ;
        pim.deRegisterPlugIn( pir1 ) ;
        pim.deRegisterPlugIn( pir2 ) ;
        dialog.rebuildTree() ;
        
        TreeModel treeModel = dialog.theTree().getModel() ;
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot() ;
        
        // Add a registration
        pim.registerPlugIn( pir0 ) ;
        dialog.rebuildTree() ;
                
        // Check the level 1 nodes
        assertEquals( root.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch0 = (DefaultMutableTreeNode) root.getChildAt( 0 ) ;
        assertEquals( ch0.getUserObject(), "tm.plugins.A:B" ) ;
        
        // Check the level 2 nodes
        
        assertEquals( ch0.getChildCount(), 1 ) ;
        DefaultMutableTreeNode ch0_0 = (DefaultMutableTreeNode) ch0.getChildAt( 0 ) ;
        assertEquals( ch0_0.getUserObject(), pir0 ) ;
        
        
        // Now replace that registration with another of the same name.
        pim.registerPlugIn( pir3 ) ;
        pim.deRegisterPlugIn( pir0 ) ;
        dialog.rebuildTree() ;
        
        // Check the level 1 nodes
        assertEquals( root.getChildCount(), 1 ) ;
        ch0 = (DefaultMutableTreeNode) root.getChildAt( 0 ) ;
        assertEquals( ch0.getUserObject(), "tm.plugins.A:B" ) ;
        
        // Check the level 2 nodes
        
        assertEquals( ch0.getChildCount(), 1 ) ;
        ch0_0 = (DefaultMutableTreeNode) ch0.getChildAt( 0 ) ;
        assertEquals( ch0_0.getUserObject(), pir3 ) ;
        
        // Now put the original back
        
        pim.registerPlugIn( pir0 ) ;
        dialog.rebuildTree() ;
 
        // Check the level 1 nodes
        assertEquals( root.getChildCount(), 1 ) ;
        ch0 = (DefaultMutableTreeNode) root.getChildAt( 0 ) ;
        assertEquals( ch0.getUserObject(), "tm.plugins.A:B" ) ;
        
        // Check the level 2 nodes
        
        assertEquals( ch0.getChildCount(), 2 ) ;
        ch0_0 = (DefaultMutableTreeNode) ch0.getChildAt( 0 ) ;
        DefaultMutableTreeNode ch0_1 = (DefaultMutableTreeNode) ch0.getChildAt( 1 ) ;
        assertEquals( ch0_0.getUserObject(), pir3 ) ;
        assertEquals( ch0_1.getUserObject(), pir0 ) ;
        
        // Check the level 3 nodes
        assertEquals( ch0_0.getChildCount(), 0) ;
        assertEquals( ch0_1.getChildCount(), 1) ;
    }

}
