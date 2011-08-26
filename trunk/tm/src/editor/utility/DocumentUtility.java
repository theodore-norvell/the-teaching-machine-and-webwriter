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
 * Created on 2006-10-10
 * project: FinalProject
 */
package editor.utility;

import java.io.IOException;
import java.io.Reader;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;

import tm.utilities.TMFile;
import editor.model.DocumentBase;
import editor.model.DocumentModel;

/**
 * Document utility,offers many tool kit for document
 * @author hao sun
 * @version $Revision: 1.1 $
 * @since version 1.1
 */

public class DocumentUtility {

//    public static int[] getRowAndColumnByOffset(Element root, int offs) {
//        int[] rowAndColumn = { 1, 1 };
//        rowAndColumn[0] = root.getElementIndex(offs) + 1;
//        Element childEl = root.getElement(rowAndColumn[0] - 1);
//        rowAndColumn[1] = childEl.getElementIndex(offs) + 1;
//        return rowAndColumn;
//    }
    
    /**
     * Get offset by known RowAndColumn
     * @param root Element
     * @param row
     * @param column
     */
    public static int getOffsetByRowAndColumn(Element root, int row, int column) {
       Element el=root.getElement(row-1);
//       System.out.println("row:"+row+" col:"+column+" offset:"+(el.getStartOffset()+column-1));
//       System.out.println(el.getStartOffset()+column-1);
       if(el==null)
           return 0;
       else
           return el.getStartOffset()+column-1;
    }
    
    /**
     * Get offset by RowAndColumn
     * @param stream
     * @param row
     * @param column
     * @return
     */
    public static int getOffsetByRowAndColumn(String stream, int row, int column) {
        int offset = 0;
        while (row > 1) {
            offset = stream.indexOf('\n', offset) + 1;
            if (offset == 0)
                break;
            else
                row--;
        }
        if (offset + column - 1 >= stream.length())
            offset = stream.length() - 1;
        return offset + column - 1;
    }

    /**
     * Get RowAndColumn by known offset
     * @param stream
     * @param offset
     * @return
     */
    public static int[] getRowAndColumnByOffset(String stream, int offset) {
        int startPoint = 0;
        int lastLinePoint = 0;
        int[] rowAndColumn = { 0, 0 };  //Start from 1,1 as offset=0
        //Check the offset
        if (offset > stream.length()) {
//            System.out.println("offset:"+offset+" length:"+stream.length()+" stream:"+stream);
            throw new Error("offset is out of index! offset="+offset+" stream length="+stream.length());
        }
        while (startPoint < offset && startPoint >= 0) {
            lastLinePoint = startPoint;
            startPoint = stream.indexOf('\n', startPoint + 1);
            if (startPoint < offset && startPoint >= 0){
                rowAndColumn[0]++;
            }else if(rowAndColumn[0]!=0)
                lastLinePoint++;
        }
        rowAndColumn[1] += offset - lastLinePoint+1;
        rowAndColumn[0]++;
        return rowAndColumn;
    }
    
    /**
     * Convert TMFile object to Document model
     * @param file
     * @return
     */
    public static DocumentBase convertTMFileToModel(TMFile file){
        Reader fileReader=file.toReader();
        DocumentBase doc=null;
        char[] buffer={};
        try {
            fileReader.read(buffer);
            doc=new DocumentModel();
            doc.setDocumentName(file.getFileName());
            StyledDocument styledDoc=new DefaultStyledDocument();
            try {
                styledDoc.insertString(0, String.valueOf(buffer), null);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            doc.setDocument(styledDoc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }
}
