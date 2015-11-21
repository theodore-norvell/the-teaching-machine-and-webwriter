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

package tm.javaLang.analysis;

import tm.clc.analysis.SpecifierSet;
import tm.clc.ast.TypeNode;

/*******************************************************************************
Interface: Java_SpecifierSet

Overview:
This class is the Java Specifier Set.

Review:          xxxx xx xx     xxxxxxxxxxxxx
*******************************************************************************/

public class Java_SpecifierSet extends SpecifierSet
                               implements Java_Specifiers {

    private int arrayDimCount = 0;

    public Java_SpecifierSet () { spec_list = new boolean [SP_NUM_SPECIFIERS]; }

    public void setArrayDimCount(int adc) { arrayDimCount = adc; }
    public int getArrayDimCount() { return arrayDimCount; }

    public TypeNode getPrimitiveType(){
        for (int i = 0; i <= SP_VOID; i++)
            if (contains(i)) return typeMapping[i];
        return null;
    }

    public String getPrimitiveName(){
        for (int i = 0; i <= SP_VOID; i++)
            if (contains(i)) return spec_strings[i];
        return null;
    }

    public String toString () {
        StringBuffer specStr = new StringBuffer ("specifier set : ");
        for (int i = 0; i < spec_list.length; i++) {
            if (spec_list [i]) { specStr.append (spec_strings [i] + " "); }
        }

        if (type_name != null) {
            specStr.append (" (type name : " + type_name.getName () + ")");
        }
        return specStr.toString ();
    }
}


