//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
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
 * Created on 31-Jul-2006 by Theodore S. Norvell. 
 */
package tm;

import tm.interfaces.DisplayManagerPIFactoryIntf;
import tm.interfaces.EditorPIFactoryInterface;
import tm.interfaces.HigraphPIFactoryInterface;
import tm.languageInterface.LanguagePIFactoryIntf;
import tm.plugins.PlugInFactory;
import tm.plugins.Requirement;

/**
 * @author theo
 */
public class TMBigAppletPIFactory implements PlugInFactory {

    public static String jackNameForCPP = "tm.BigApplet:C++" ;
    public static String jackNameForJava = "tm.BigApplet:Java" ;
    public static String jackNameDisplayManager = "tm.BigApplet:DisplayManager" ;
    public static String jackNameEditor = "tm.BigApplet:Editor" ;
    public static String jackNameHigraph = "tm.BigApplet:Higraph";
    private Requirement[] requirements = new Requirement[] {
            new Requirement(jackNameForCPP, LanguagePIFactoryIntf.class, false, false),
            new Requirement(jackNameForJava, LanguagePIFactoryIntf.class, false, false),
            new Requirement(jackNameDisplayManager, DisplayManagerPIFactoryIntf.class, true, false),
            new Requirement(jackNameEditor, EditorPIFactoryInterface.class, false, false) ,
            new Requirement(jackNameHigraph, HigraphPIFactoryInterface.class, false, false)
    } ;
    
    public static TMBigAppletPIFactory createInstance( String parameter ) {
        return new TMBigAppletPIFactory() ;
    }
    
    private TMBigAppletPIFactory() {} 
    
    /**
     * @see tm.plugins.PlugInFactory#getRequirements()
     */
    public Requirement[] getRequirements() {
        return requirements ;
    }

}
