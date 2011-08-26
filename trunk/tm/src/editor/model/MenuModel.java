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

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

/**
 * Menu model for editor
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class MenuModel implements IMenuModel{
    private String menuName;
    
    //the menu level,root for 0
    private int level=0;
    
    private List<IMenuModel> childNode=null;
    
    private Type type;

    private AbstractAction action;
    
    private boolean isEnabled=true;
    
    /**
     * Constructor
     * @param menuName
     * @param level
     * @param type
     */
    public MenuModel(String menuName,int level,Type type){
        this.menuName=menuName;
        this.level=level;
        this.type=type;
    }
    
    public List<IMenuModel> getChildNode() {
        return childNode;
    }

    public String getMenuName() {
        return menuName;
    }

    public IMenuModel getParentNode() {
        // TODO Auto-generated method stub
        return null;
    }

    public Type getType() {
        return type;
    }

    public void setAction(AbstractAction action) {
        this.action=action;
    }

    public AbstractAction getAction(){
        return action;
    }
    
    public void setChildNode(List<IMenuModel> child) {
        childNode=child;
    }

    public void setMenuName(String menuName) {
        this.menuName=menuName;
    }

    public void setType(Type type) {
        this.type=type;
    }
    
    
    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    public void addChildNode(MenuModel child){
        if(childNode==null)
            childNode=new ArrayList<IMenuModel>();
        childNode.add(child);
        level=child.level+1;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    
}

