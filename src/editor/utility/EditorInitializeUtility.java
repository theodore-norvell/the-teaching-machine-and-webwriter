//     Copyright 2007 Hao Sun
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

/*
 * Created on 2006-9-29
 * project: FinalProject
 */
package editor.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import editor.controller.actions.MenuBarActions;
import editor.controller.actions.MutipleEditorRedoAction;
import editor.controller.actions.MutipleEditorUndoAction;
import editor.model.EditorModel;
import editor.model.IMenuModel;
import editor.model.MenuModel;
import editor.model.IMenuModel.Type;

/**
 * Editor Initialize Utility
 * @author hao sun
 * @version $Revision: 1.1 $
 * @since version 1.1
 */

public class EditorInitializeUtility {
    /**
     * Generate the menu model list
     * 
     * @return List of the menu model
     */
    public static List<IMenuModel> getMenuModelList(EditorModel editorModel) {
        List<IMenuModel> tempList = new ArrayList<IMenuModel>();
        MenuModel tempMenu = new MenuModel("New", 0, Type.BOTH);
        tempMenu.setAction(new MenuBarActions.OpenNewFileDialogAction("New file",
                editorModel.getView()));
        tempMenu.setEnabled(false);
        MenuModel tempMenu2 = new MenuModel("File", 0, Type.MENU);
        tempMenu2.addChildNode(tempMenu);
        tempMenu = new MenuModel("Open", 0, Type.MENU);
        tempMenu.setEnabled(false);
        tempMenu2.addChildNode(tempMenu);
        tempList.add(tempMenu2);
        tempMenu=new MenuModel("Save", 0, Type.BOTH);
        tempMenu.setAction(new MenuBarActions.SaveTextPanelAction("Save",
                editorModel.getView()));
        tempMenu2.addChildNode(tempMenu);

        tempMenu=new MenuModel("Close", 0, Type.BOTH);
        tempMenu.setAction(new MenuBarActions.CloseTextPanelAction("close",
                editorModel.getView()));
        tempMenu2.addChildNode(tempMenu);
        
        tempMenu2 = new MenuModel("Edit", 0, Type.MENU);
        tempMenu = new MenuModel("Redo", 0, Type.MENU);
        tempMenu.setAction(new MutipleEditorRedoAction(editorModel.getView()));
        
        tempMenu2.addChildNode(tempMenu);
        tempMenu = new MenuModel("Undo", 0, Type.MENU);
        tempMenu.setAction(new MutipleEditorUndoAction(editorModel.getView()));
        
        tempMenu2.addChildNode(tempMenu);
        tempList.add(tempMenu2);
        
        tempMenu2 = new MenuModel("Help", 0, Type.MENU);
        tempMenu = new MenuModel("About Editor", 0, Type.MENU);
        tempMenu.setAction(new MenuBarActions.OpenEditorAboutAction("about",editorModel.getView()));
        
        tempMenu2.addChildNode(tempMenu);
        tempList.add(tempMenu2);
        
        return tempList;
    }

    /**
     * Convert menu model to view(menu bar)
     * 
     * @param menuList
     * @return menubar for view
     */
    public static JMenuBar convertToMenuBar(List<IMenuModel> menuList) {
        if(menuList==null)
            return null;
        JMenuBar tempMenuBar = new JMenuBar();
        for (int i = 0; i < menuList.size(); i++) {
            if (menuList.get(i).getType() == IMenuModel.Type.BOTH
                    || menuList.get(i).getType() == IMenuModel.Type.MENU|| menuList.get(i).getType() == IMenuModel.Type.ALL) {
                JMenu tempMenu = new JMenu(menuList.get(i).getMenuName());
                convertMenuBar(tempMenu, menuList.get(i).getChildNode());
                tempMenuBar.add(tempMenu);
            }
        }
        return tempMenuBar;
    }
    
    /**
     * Convert menu model to view(popup menu)
     * 
     * @param menuList
     * @return popup menu for view
     */
    public static JPopupMenu convertToPopUpMenu(List<IMenuModel> menuList){
        if(menuList==null)
            return null;
        JPopupMenu tempPopupMenu = new JPopupMenu();
        for (int i = 0; i < menuList.size(); i++) {
            if (menuList.get(i).getType() == IMenuModel.Type.POPUP
                    || menuList.get(i).getType() == IMenuModel.Type.ALL) {
                JMenu tempMenu = new JMenu(menuList.get(i).getMenuName());
                convertToMenu(tempMenu, menuList.get(i));
                tempPopupMenu.add(tempMenu);
            }
        }
        return tempPopupMenu;
    }
    
    /**
     * Convert menu model to view(tool bar)
     * 
     * @param menuList
     * @return menubar for view
     */
    public static JToolBar convertToToolBar(JToolBar toolBar,List<IMenuModel> menuList) {
        if(menuList==null)
            return null;
        if(toolBar==null)
            toolBar=new JToolBar();
        for (int i = 0; i < menuList.size(); i++) {
            if (menuList.get(i).getType() == IMenuModel.Type.BOTH
                    || menuList.get(i).getType() == IMenuModel.Type.TOOLBAR|| menuList.get(i).getType() == IMenuModel.Type.ALL) {
                JButton tempButton=new JButton();
                tempButton.setAction(menuList.get(i).getAction());
                tempButton.setToolTipText(menuList.get(i).getMenuName());
                
                tempButton.setText(menuList.get(i).getMenuName());
                tempButton.setEnabled(menuList.get(i).isEnabled());
                toolBar.add(tempButton);
                if(menuList.get(i).getChildNode()!=null)
                    convertToToolBar(toolBar,menuList.get(i).getChildNode());
            }else if(menuList.get(i).getChildNode()!=null){
                convertToToolBar(toolBar,menuList.get(i).getChildNode());
            }
        }
        return toolBar;
    }

    /**
     * convert menu for menubar
     * 
     * @param menu
     * @param menuList
     */
    private static void convertMenuBar(JMenu menu, List<IMenuModel> menuList) {
        for (int i = 0; i < menuList.size(); i++) {
            convertToMenu(menu, menuList.get(i));
        }
    }

    /**
     * Convert model to menu
     * 
     * @param menu
     * @param menuModel
     */
    private static void convertToMenu(JMenu menu, IMenuModel menuModel) {
        List menuList = menuModel.getChildNode();
        if (menuList == null) {
            JMenuItem menuItem = new JMenuItem();
            menuItem.setText(menuModel.getMenuName());
            menuItem.setName(menuModel.getMenuName());
            // add action
            menuItem.addActionListener(menuModel.getAction());
            menuItem.setEnabled(menuModel.isEnabled());
            menu.add(menuItem);
        } else {
            JMenu tempMenu = new JMenu(menuModel.getMenuName());
            for (int i = 0; i < menuList.size(); i++) {
                convertToMenu(tempMenu, (MenuModel) menuList.get(i));
            }
            menu.add(tempMenu);
        }
    }
    
    public static HashMap<KeyStroke, AbstractAction> getEditorPanelKeyActionTable(){
        HashMap<KeyStroke, AbstractAction> result=new HashMap<KeyStroke, AbstractAction>();
        return result;
    }
}
