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
 * Created on 2006-11-9
 * project: FinalProject
 */
package editor.controller.filters;

import java.util.ArrayList;
import java.util.List;

import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

import editor.model.DocumentBase;
import editor.view.EditorViewConstants;

/**
 * Code filter for the editor
 * 
 * @author hao sun
 * @version $Revision: 1.1 $
 * @since version 1.1
 */

public class IndentationKeyFilter implements IKeyFilter, EditorViewConstants {

    private List<KeyStroke> keys;

    private DocumentBase doc;

    /**
     * Forbbit constructing without params
     * 
     */
    private IndentationKeyFilter() {
        keys = new ArrayList<KeyStroke>();
    }

    /**
     * Construct filter by keyStrokes
     * 
     * @param key
     * @param doc
     */
    public IndentationKeyFilter(DocumentBase doc) {
        this();
        this.keys.add(KeyStroke.getKeyStroke('\n')); // add 'enter' key
        this.doc = doc;
    }

    public List<KeyStroke> getKeyStrokes() {
        return keys;
    }

    /**
     * Perform filter action
     * 
     * @param input
     * @param doc
     */
    private void performFilter(KeyStroke input, DocumentBase doc) {
        // Make auto-indentation
        // Step 1:Find last line indentation increment
        int indentation = findIndentation();
        StringBuffer indentationString = new StringBuffer("");
        for (int i = 0; i < indentation; i++) {
            indentationString.append(" ");
        }
        // Step 2:Append indentation to current position
        // Insert indentation
        doc.insertString(doc.getCurrentCaretEndPostion(), indentationString
                .toString());
//        LoggerUtility.logOnConsole("make indetation! ->" + indentationString
//                + "<-");
    }

    public boolean performFilter(KeyStroke input) {
        boolean canBeFilted = false;

        // Check matched or not
        canBeFilted = keyChecker(input);
        // perform action
//        System.out.println("currentPos before filting"+doc.getCurrentCaretEndPostion());
        if (canBeFilted)
            performFilter(input, doc);
        return canBeFilted;
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

    /**
     * Find Indentation for current input
     * @return
     */
    private int findIndentation() {
        // Get current row and col
        // int[]
        // currentPostion=DocumentUtility.getRowAndColumnByOffset(doc.getDocument().getText(0,
        // doc.getDocument().getLength()), );
        Element lastLine = doc.getDocument().getDefaultRootElement()
                .getElement(
                        doc.getDocument().getDefaultRootElement()
                                .getElementIndex(
                                        doc.getCurrentCaretEndPostion()) - 1);
        String tempLineString = "";
        try {
            tempLineString = doc.getDocument().getText(
                    lastLine.getStartOffset(),
                    lastLine.getEndOffset() - lastLine.getStartOffset());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        int result = 0;
        for (int i = 0; i < tempLineString.length(); i++) {
            if (tempLineString.charAt(i) == ' ')
                result++;
            else if (tempLineString.charAt(i) == '\t')
                result += DEFAULT_TABKEYINCREMENT;
            else
                break;
        }
        return result;
    }
}
