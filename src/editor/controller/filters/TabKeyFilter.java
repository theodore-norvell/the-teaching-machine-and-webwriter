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
 * Created on 2006-11-17
 * project: FinalProject
 */
package editor.controller.filters;

import java.util.ArrayList;
import java.util.List;

import javax.swing.KeyStroke;

import editor.model.DocumentBase;
import editor.utility.LoggerUtility;

/**
 * Tab key filter
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class TabKeyFilter implements IKeyFilter {
    
    private List<KeyStroke> keys;

    private DocumentBase doc;

    /**
     * Forbbit constructing without params
     * 
     */
    private TabKeyFilter() {
        keys = new ArrayList<KeyStroke>();
    }

    /**
     * Construct filter by keyStrokes
     * 
     * @param key
     * @param doc
     */
    public TabKeyFilter(DocumentBase doc) {
        this();
        this.keys.add(KeyStroke.getKeyStroke('\t')); // add 'enter' key
        this.doc = doc;
    }
    
    public List<KeyStroke> getKeyStrokes() {
        return keys;
    }

    public boolean performFilter(KeyStroke input) {
        boolean canBeFilted = false;

        // Check matched or not
        canBeFilted = keyChecker(input);
        // perform action
        if (canBeFilted)
            performFilter(input, doc);
        return canBeFilted;
    }
    
    /**
     * Perform filter action
     * 
     * @param input
     * @param doc
     */
    private void performFilter(KeyStroke input, DocumentBase doc) {
//        LoggerUtility.logOnConsole("Tab");
        doc.removeString(doc.getCurrentCaretEndPostion()-1, 1);
        doc.insertString(doc.getCurrentCaretEndPostion(), "  ");
        
    }
    /**
     * Check current key is match the filter condition or not
     * 
     * @param input
     * @return
     */
    private boolean keyChecker(KeyStroke input) {
        return (keys.contains(input));
    }

}

