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
package editor.model;

import java.util.List;

import javax.swing.AbstractAction;

/**
 * Interface for a basic menu or tool bar item.
 * It offers the basic functions for a menu or tool bar item
 * 
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public interface IMenuModel {
    /**
     * Type for menu
     * @author hsun
     *
     */
    public enum Type{
        MENU,
        TOOLBAR,
        BOTH,   //MENU AND TOOLBAR
        POPUP,
        ALL     //MENU , TOOLBAR AND POPUP
    }
    
    /**
     * Get parent element for this menu
     * @return parent element if there has one
     */
    public IMenuModel getParentNode();
    
    /**
     * Get child element for this menu if there has one
     * @return
     */
    public List<IMenuModel> getChildNode();
    
    /**
     * Set child elements for this menu
     * @param child
     */
    public void setChildNode(List<IMenuModel> child);
    
    /**
     * Get menu name
     * @return
     */
    public String getMenuName();
    
    /**
     * Set menu name
     *
     */
    public void setMenuName(String menuName);
    
    /**
     * Set action for this menu
     * @param action
     */
    public void setAction(AbstractAction action);
    
    /**
     * Get action
     * @return the action for this menu model
     */
    public AbstractAction getAction();
    
    /**
     * Set type for this menu
     * @param type
     */
    public void setType(Type type);
    
    /**
     * Get the menu's type
     * @return
     */
    public Type getType();
    
    /**
     * Get this menu is endabled or not
     * @return true if enabled
     */
    public boolean isEnabled();
    
    /**
     * Set this menu is endabled or not
     * @param isEnabled
     */
    public void setEnabled(boolean isEnabled);
    
}

