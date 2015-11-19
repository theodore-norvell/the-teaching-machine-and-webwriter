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

import java.io.ByteArrayInputStream;

import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

import editor.controller.parser.Token;
import editor.controller.parser.java.JavaCharStream;
import editor.controller.parser.java.JavaParserConstants;
import editor.controller.parser.java.JavaParserTokenManager;
import editor.model.DocumentBase;
import editor.utility.DocumentUtility;
import editor.utility.GlobalValue;

/**
 * JavaDocument Colorizer
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class JavaDocumentColorizer extends DocumentColorizerBase implements JavaParserConstants{
    
    /**
     * Constructor with known document model
     * @param doc
     */
    public JavaDocumentColorizer(DocumentBase doc){
        super(doc);
    }
    
    public void digestDocumentStream(){
        int[] parserStartAndEndPosition = getParserStartAndEndPosition(doc);
        digestDocumentStreamWithKnownPosition(parserStartAndEndPosition);
    }
    
    public void digestDocumentStreamWithKnownPosition(int[] parserStartAndEndPosition) {
        ByteArrayInputStream is;
        try {
            

//            System.out
//                    .println("No1:"
//                            + ((parserStartAndEndPosition[0] < parserStartAndEndPosition[1]) ? parserStartAndEndPosition[0]
//                                    : parserStartAndEndPosition[1])
//                            + " No2:"
//                            + ((parserStartAndEndPosition[0] > parserStartAndEndPosition[1]) ? (parserStartAndEndPosition[0] - parserStartAndEndPosition[1])
//                                    : (parserStartAndEndPosition[1] - parserStartAndEndPosition[0])));
            String totalString = doc.getDocument().getText(0,
                    doc.getDocument().getLength());
            String origString = "";
            if (((parserStartAndEndPosition[0] < parserStartAndEndPosition[1]) ? parserStartAndEndPosition[0]
                    : parserStartAndEndPosition[1])
                    + ((parserStartAndEndPosition[0] > parserStartAndEndPosition[1]) ? (parserStartAndEndPosition[0] - parserStartAndEndPosition[1])
                            : (parserStartAndEndPosition[1] - parserStartAndEndPosition[0])) >= doc
                    .getDocument().getLength())
                origString = totalString.substring((parserStartAndEndPosition[0] < parserStartAndEndPosition[1]) ? parserStartAndEndPosition[0]: parserStartAndEndPosition[1],doc
                        .getDocument().getLength());
            else
                origString = totalString.substring((parserStartAndEndPosition[0] < parserStartAndEndPosition[1]) ? parserStartAndEndPosition[0]: parserStartAndEndPosition[1],(parserStartAndEndPosition[0] > parserStartAndEndPosition[1]) ? parserStartAndEndPosition[0]: parserStartAndEndPosition[1]);
            
            is = new ByteArrayInputStream(origString.getBytes());
            JavaCharStream jcs = new JavaCharStream(is);
            JavaParserTokenManager jtm = new JavaParserTokenManager(jcs);
            Token testToken = null;
            boolean isStart = false;

            // doc.getTokenList().clear();
            {
                // The situation that increasing the text
                // Step1 :Delete the tokens in this range
                int selectedBlockLength = doc.getLastCaretEndPosition()
                        - doc.getLastCaretStartPosition();;
                int startPosition = doc.getLastCaretStartPosition();
                for (int i = 0; i < selectedBlockLength; i++) {
                    if (startPosition + i < doc.getTokenList().size())
                        doc.getTokenList().set(startPosition + i, null);
                }
                // Step2 :Add new tokens in this range
                if (doc.getLastCaretEndPosition() < doc
                        .getCurrentCaretEndPostion()) {
                    // Token list will be increased
                    for (int i = 0; i < doc.getCurrentCaretEndPostion()
                            - doc.getLastCaretEndPosition(); i++) {
                        doc.getTokenList().add(doc.getLastCaretEndPosition(),
                                null);
                    }
                } else if (doc.getLastCaretEndPosition() > doc
                        .getCurrentCaretEndPostion()) {
                    // Token list will be decreased
                    for (int i = 0; i < doc.getLastCaretEndPosition()
                            - doc.getCurrentCaretEndPostion(); i++) {
                        doc.getTokenList().remove(
                                doc.getCurrentCaretEndPostion());
                    }
                }
            }
            while (testToken != null || !isStart) {
                isStart = true;
                try {
                    testToken = jtm.getNextToken();
                    // testToken.
                    // doc.getTokenList().add(testToken);
                    int insertIndex = DocumentUtility.getOffsetByRowAndColumn(
                            origString,
                            testToken.beginLine, testToken.beginColumn)
                            + parserStartAndEndPosition[0];
                    if (testToken != null && testToken.kind != EOF) {
//                         System.out.println("add:" + testToken + " at"
//                         + insertIndex);
                        int[] newBeginPosition = DocumentUtility
                                .getRowAndColumnByOffset(totalString,
                                        insertIndex);
                        int[] newEndPosition = DocumentUtility
                                .getRowAndColumnByOffset(totalString,
                                        insertIndex + testToken.image.length()
                                                - 1);
                        // Add origenal position into the token
                        testToken.beginLine = newBeginPosition[0];
                        testToken.beginColumn = newBeginPosition[1];
                        testToken.endLine = newEndPosition[0];
                        testToken.endColumn = newEndPosition[1];
//                        while(true)
//                            if(insertIndex>=doc.getTokenList().size())
//                                doc.getTokenList().add(null);
//                            else
//                                break;
                        if(insertIndex<doc.getTokenList().size())
                            doc.getTokenList().set(insertIndex, testToken);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (testToken == null || testToken.kind == EOF) {
                    // Exit when reach to the EOF
                    break;
                }
            }
            // Check token list
            // If less than the length of document,add to reach the length of
            // document
            while (doc.getTokenList().size() < doc.getDocument().getLength())
                doc.getTokenList().add(null);
            // Run the thread to colorize the code
            if (GlobalValue.UPDATE_DOCUMENT_THREAD != null) {
                GlobalValue.UPDATE_DOCUMENT_THREAD.stop();
            }
            GlobalValue.UPDATE_DOCUMENT_THREAD = new Thread(
                    new DocumentColorizerRunnable(doc, parserStartAndEndPosition),
                    "updateDocument");
            GlobalValue.UPDATE_DOCUMENT_THREAD.setPriority(Thread.MIN_PRIORITY);

            SwingUtilities.invokeLater(GlobalValue.UPDATE_DOCUMENT_THREAD);
            GlobalValue.UPDATE_DOCUMENT_THREAD = null;
        } catch (BadLocationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

}

