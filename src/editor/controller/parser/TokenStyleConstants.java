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
package editor.controller.parser;

import java.awt.Color;
import java.awt.Font;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * 
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/

public class TokenStyleConstants {
    public static SimpleAttributeSet PLAINTEXT_COLOR=generrateAttributeSet(Color.BLACK,new Font(null,Font.PLAIN,12),"SansSerif");
    public static SimpleAttributeSet KEYWORD_COLOR=generrateAttributeSet(new Color(127,0,85),new Font(null,Font.BOLD,12),"SansSerif");
    public static SimpleAttributeSet COMMENT_COLOR=generrateAttributeSet(new Color(63,127,95),new Font(null,Font.TRUETYPE_FONT,12),"Arial");
    public static SimpleAttributeSet CONSTANT_COLOR=generrateAttributeSet(new Color(0,0,192),new Font(null,Font.PLAIN,12),"SansSerif");
    public static SimpleAttributeSet PREPROCESSOR_COLOR=generrateAttributeSet(new Color(0,0,192),new Font(null,Font.PLAIN,12),"SansSerif");
    public static SimpleAttributeSet OPERATOR_COLOR=generrateAttributeSet(new Color(0,0,192),new Font(null,Font.BOLD,12),"SansSerif");
    public static SimpleAttributeSet IDENTIFIER_COLOR=PLAINTEXT_COLOR;//generrateAttributeSet(new Color(0,0,192),new Font(null,Font.ITALIC,12),"SansSerif");
    public static SimpleAttributeSet METHOD_COLOR=generrateAttributeSet(Color.BLACK,new Font(null,Font.ITALIC,12),"SansSerif");
    
    public static SimpleAttributeSet generrateAttributeSet(Color color,Font font,String family){
        SimpleAttributeSet sAS = new SimpleAttributeSet();
        StyleConstants.setForeground(sAS,color);
        StyleConstants.setFontSize(sAS, font.getSize());
        StyleConstants.setBold(sAS, font.isBold()?true:false);
        StyleConstants.setItalic(sAS, font.isItalic()?true:false);
        StyleConstants.setFontFamily(sAS, family);
        return sAS;
    }
}

