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
 * Created on 2006-11-23
 * project: FinalProject
 */
package editor.controller.colorizer;

import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import editor.controller.parser.Token;
import editor.model.DocumentBase;
import editor.utility.DocumentUtility;

/**
 * Document colorizer base
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public abstract class DocumentColorizerBase implements IDocumentColorizer{

    protected static int TOKEN_SEARCH_AHEAD_AND_BEHIND = 4;
    
    protected DocumentBase doc;
    
    /**
     * Constructor
     * @param doc
     */
    public DocumentColorizerBase(DocumentBase doc){
        this.doc=doc;
    }
    
    /**
     * Digest document stream
     */
    public abstract void digestDocumentStream();
    
    /**
     * Digest document stream with known position
     * @param startAndEndPosition
     */
    public abstract void digestDocumentStreamWithKnownPosition(int[] startAndEndPosition);

    /**
     * Colorize the code by tokenized document
     * 
     * @author hsun
     * 
     */
    protected static class DocumentColorizerRunnable implements Runnable {

        private String totalString = null;

        private DocumentBase doc;

        private int[] startAndEndPosition = { 0, 0 };

        public DocumentColorizerRunnable(DocumentBase doc,
                int[] startAndEndPosition) {
            this.doc = doc;
            this.startAndEndPosition = startAndEndPosition;
            
        }

        /**
         * Colorize the token list
         */
        public void run() {
            doc.setCanUndo(false);
            List<Token> tempList = doc.getTokenList();
            try {
                totalString = doc.getDocument().getText(0,
                        doc.getDocument().getLength());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            for (int i = startAndEndPosition[0]; i < startAndEndPosition[1]; i++) {
                if (i < tempList.size() && tempList.get(i) != null) {
                    Token tempToken = tempList.get(i);
                    int[] tempBeginPosition = DocumentUtility
                            .getRowAndColumnByOffset(totalString, i);
                    int[] tempEndPosition = DocumentUtility
                            .getRowAndColumnByOffset(totalString, i
                                    + tempToken.image.length() - 1);

                    tempToken.beginLine = tempBeginPosition[0];
                    tempToken.beginColumn = tempBeginPosition[1];
                    tempToken.endLine = tempEndPosition[0];
                    tempToken.endColumn = tempEndPosition[1];
                    colorizeToken(doc.getDocument(), tempToken, totalString);
                    // System.out.println(tempToken.image.length()+" "+"
                    // position:"+tempToken.beginLine+tempToken.beginColumn+"--"+tempToken.endLine+tempToken.endColumn);
                    doc.getTokenList().set(i, tempToken);
                }
            }
            doc.setCanUndo(true);
        }

    }

    /**
     * Colorize tokens
     * @param doc
     * @param token
     * @param origString
     */
    protected static void colorizeToken(StyledDocument doc, Token token,
            String origString) {
        // if(token != null && token.specialToken != null)
        // setStyle(doc, token.specialToken, origString);
        setStyle(doc, token, origString);
    }

    /**
     * Set style
     * @param doc
     * @param token
     * @param origString
     */
    protected static void setStyle(StyledDocument doc, Token token,
            String origString) {
//        System.out.println(token + " " + " position:" + token.beginLine
//                + token.beginColumn + "--" + token.endLine + token.endColumn);
        // doc.rem
        doc.setCharacterAttributes(DocumentUtility.getOffsetByRowAndColumn(doc
                .getDefaultRootElement(), token.beginLine, token.beginColumn),
                token.image.length(), token.style, true);

    }

    /**
     * Get parser start and end position
     * @param doc
     * @return
     */
    protected static int[] getParserStartAndEndPosition(DocumentBase doc) {
        int[] result = { 0, doc.getDocument().getLength() }; // result[0]:start
        // find start token

        int lastBlockLength=doc.getLastCaretEndPosition()-doc.getLastCaretStartPosition();
        int currentBlockLength=doc.getCurrentCaretEndPostion()-doc.getLastCaretStartPosition();
        int stringIncrement=currentBlockLength-lastBlockLength;
        int currentCaretEndPostion = doc.getCurrentCaretEndPostion();
        if (doc.getTokenList().isEmpty())
            return result;
        else
            result[1] = doc.getCurrentCaretEndPostion();
        if (doc.getLastCaretStartPosition() > doc.getTokenList().size() - 1)
            result[0] = doc.getTokenList().size() - 1;
        else
            result[0]= doc.getLastCaretStartPosition();
        if (currentCaretEndPostion > doc.getTokenList().size())
            result[1] = doc.getTokenList().size() - 1;
        else
            result[1] = currentCaretEndPostion;
         //System.out.println("start:"+result[0]+" end:"+result[1]);
        
        for (int i = 0; i < TOKEN_SEARCH_AHEAD_AND_BEHIND; i++) {
            while (result[0] > 0) {
                if (doc.getTokenList().get(result[0]) == null) {
                    result[0]--;
                } else {
                    break;
                }
            }
        }
        for (int i = 0; i < TOKEN_SEARCH_AHEAD_AND_BEHIND; i++) {
            while (result[1] <= doc.getTokenList().size() - 1) {
                if (doc.getTokenList().get(result[1]) == null) {
                    result[1]++;
                } else {
                    int tempIndex = 2*stringIncrement+DocumentUtility.getOffsetByRowAndColumn(doc
                            .getDocument().getDefaultRootElement(), doc
                            .getTokenList().get(result[1]).endLine, doc
                            .getTokenList().get(result[1]).endColumn);

                    result[1] = (tempIndex >= currentCaretEndPostion) ? tempIndex
                            : currentCaretEndPostion;
                    break;
                }
            }
        }
        if (result[1] >= doc.getTokenList().size()) {
            result[1] = doc.getDocument().getLength();
        }
        // System.out.println("start:" + result[0] + " end:" + result[1]);
        return result;
    }

}

