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
package editor.controller;

import editor.controller.colorizer.DocumentColorizerBase;
import editor.controller.colorizer.JavaDocumentColorizer;
import editor.model.DocumentBase;

/**
 * Document style handler
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class DocumentStyleHandler {
    private DocumentBase doc;
    private DocumentColorizerBase colorizer;
    
    /**
     * Constructor
     * @param doc
     */
    public DocumentStyleHandler(DocumentBase doc){
        this.doc=doc;
        //Create Default colorizer
        if(doc.getType()!=null){
            if(doc.getType().equals(DocumentBase.Type.JAVA))
                setColorizer(new JavaDocumentColorizer(doc));
        }
    }
    
    /**
     * Get document model
     * @return
     */
    public DocumentBase getDocumentModel(){
        return doc;
    }
    
    /**
     * Set colorizer
     * @param colorizer
     */
    public void setColorizer(DocumentColorizerBase colorizer){
        this.colorizer=colorizer;
    }
 
    /**
     * Perform the stylize with known position
     * @param startAndEndPosition
     */
    public void run(int[] startAndEndPosition){
        if(colorizer!=null)
            colorizer.digestDocumentStreamWithKnownPosition(startAndEndPosition);
    } 
    
    /**
     * Perform the stylize
     *
     */
    public void run() {
        if(colorizer!=null)
            colorizer.digestDocumentStream();
    }
}

