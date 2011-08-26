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
 * Created on 12-Nov-2005 by Theodore S. Norvell. 
 */
package tm.displayEngine.generators;

import tm.interfaces.Datum;
import tm.interfaces.RegionInterface;


/** Create a generator that consists of the combined contents of
 * two generators.
 * 
*/

public class CatenatedGenerator extends AbstractGenerator {
    private AbstractGenerator firstGenerator ;
    private AbstractGenerator secondGenerator ;
    
    public CatenatedGenerator(AbstractGenerator firstGenerator, AbstractGenerator secondGenerator){
        this.firstGenerator = firstGenerator ;
        this.secondGenerator = secondGenerator ;
    }
    
    public int getNumChildren(){
        return firstGenerator.getNumChildren() + secondGenerator.getNumChildren() ;
    }
    
    public Datum getChildAt(int i){
        int fgnc = firstGenerator.getNumChildren() ;
        if( i < fgnc )
            return firstGenerator.getChildAt(i);
        else
            return secondGenerator.getChildAt(i - fgnc ) ;
    }
    
    public void refresh(){
    	firstGenerator.refresh();
    	secondGenerator.refresh();
    }
    
    public String toString(){
        return "CatenatedGenerator with (" + firstGenerator.toString() +") and ("
            + secondGenerator.toString() + ")" ;
    }
}