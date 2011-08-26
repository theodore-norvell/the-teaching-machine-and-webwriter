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
 * Created on 2006-10-25
 * project: FinalProject
 */
package editor.plugins;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import editor.view.EditorViewBase;


/**
 * Plugin creater
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class PluginCreater {
    
    private EditorViewBase view=null;
    
    /**
     * Constructor
     * @param view
     */
    public PluginCreater(EditorViewBase view){
        this.view=view;
    }
    /**
     * Constructor
     *
     */
    public PluginCreater(){
    }
    /**
     * Get xml path
     * @return
     */
    private String getPath(){
        String currentPath=this.getClass().getResource("PluginCreater.class").getPath();
        return currentPath.substring(0,currentPath.lastIndexOf("/"));
    }
    
    public List<EditorPlugin> getPluginList(){
        List<EditorPlugin> pluginList=new ArrayList<EditorPlugin>();
        XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = "/plugins/plugin-class/text()";
        try {
            NodeList nameNodes = (NodeList) xpath.evaluate(expression, new
             InputSource(getPath()+"\\plugin.xml"), XPathConstants.NODESET);
            for(int i=0;i<nameNodes.getLength();i++){
                try {
                    Object tempObj=Class.forName(nameNodes.item(i).getNodeValue()).newInstance();
                    if (tempObj instanceof editor.plugins.EditorPlugin) {
                        ((EditorPlugin)tempObj).setView(view);
                        pluginList.add((EditorPlugin)tempObj);
                    }else{
                        throw new Error("Unexpected class loaded:"+nameNodes.item(i).getNodeValue());
                    }
                } catch (DOMException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (XPathExpressionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pluginList;
    }
    public static void main(String[] arg){
        new PluginCreater().getPluginList();
    }
}

