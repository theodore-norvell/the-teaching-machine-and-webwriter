////     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
////
//// Licensed under the Apache License, Version 2.0 (the "License");
//// you may not use this file except in compliance with the License. 
//// You may obtain a copy of the License at 
////
////     http://www.apache.org/licenses/LICENSE-2.0 
////
//// Unless required by applicable law or agreed to in writing, 
//// software distributed under the License is distributed on an 
//// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
//// either express or implied. See the License for the specific language 
//// governing permissions and limitations under the License.
//
//package tm.displayEngine;
//
///*import java.util.*;*/
//import javax.swing.JCheckBoxMenuItem;
//import javax.swing.JMenu ;
//import java.awt.event.*;
//import java.awt.*;
//
//
//public class ViewStateManager /*implements Configurable */{
//
//
//    private static JMenu viewMenu;          // The view subMenu is controlled by this class
//    private static DisplayManager displayManager;
//    private static CodeDisplay codeDisplay;
//
//
//    private static ViewStateManager theOne;    // The only object allowed (singleton class) 
//
//// All displays. These parameters are configurable
//    private Color highlightColor = Color.yellow;
//    private Font theFont = new Font("Monospaced", Font.PLAIN, 12);
//
//// Individual display configurability is handled at the individual display level    
//// Code display 
//    private JCheckBoxMenuItem lineNumbering;
//    private Color cursorColor = Color.green;
//    
///* Singleness is enforced by making the constructor private and having a separate
//create method which only calls the constructor if no object exists already.
//This technique enforces singleness without having to throw an exception.
//*/
//    private ViewStateManager(JMenu view) {
//        if (viewMenu == null) {
//            viewMenu = view;
//    	    lineNumbering = new JCheckBoxMenuItem("Line Numbers", false);
//	        lineNumbering.addItemListener( new LineNumberingListener(lineNumbering) ) ;
//	        viewMenu.add( lineNumbering ) ;
//	    }
//            
//    }
//    public static void createViewStateManager(JMenu view){
//        if (theOne == null) theOne = new ViewStateManager(view);
//    }
//
///*  If the constructor took no argument we could do the creation here. However, we need the
//view menu. Individual displays will want a reference to this class but don't need to know
//about the view menu. Hence the separate create method above.
//*/
//    public static ViewStateManager getViewStateManager(){
//        return theOne;
//    }
//    
//    public static void setDisplays(DisplayManager dm, CodeDisplay cd){
//        if (theOne != null) {
//            displayManager = dm;
//            codeDisplay = cd;
//        }
//    }
//
///*  These routines are handcoded. I'm fairly sure there is a way to handle this generically
//by registration as we do with the ConfigurationServer.
//*/
//
//    public Color getHighlightColor(){
//        return highlightColor;
//    }
//    
//    public Color getCursorColor(){
//        return cursorColor;
//    }
//    
//    
//    
//    public void notifyLineNumbering(){
//        codeDisplay.setLineNumbering(lineNumbering.getState());
//    }
//    
//    public void setLineNumbering(boolean ln){
//        lineNumbering.setState(ln);
//    }
//        
//
//
//
//
//    class LineNumberingListener implements ItemListener{
//        JCheckBoxMenuItem myCheckbox;
//	    
//        public LineNumberingListener(JCheckBoxMenuItem c){
//            myCheckbox = c;
//        }
//
//        public void itemStateChanged( ItemEvent e ) {
//            notifyLineNumbering();
//        }
//    }
//
//}

