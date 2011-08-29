/**
 * SourceCodeEditorKit.java
 * 
 * @date: Jun 27, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing;

import javax.swing.text.*;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * SourceCodeEditorKit is used to edit and display source codes.
 * @author Xiaoyu Guo
 */
public class SourceCodeEditorKit extends StyledEditorKit {

    private static final long serialVersionUID = 4639328943176993629L;
    private static final String MAIN_STYLE_NAME = "main_style_name";
    
    public static void createStyles(StyleContext sc){
        Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
        
        // Create and add the main document style
        Style mainStyle = sc.addStyle(MAIN_STYLE_NAME, defaultStyle);
        StyleConstants.setFontFamily(mainStyle, "monospaced");
        StyleConstants.setFontSize(mainStyle, 14);
    }

    /* The extended view factory */
    static class SourceCodeViewFactory implements ViewFactory {
      public View create(Element elem) {
        String elementName = elem.getName();
        if (elementName != null) {
          if (elementName.equals(AbstractDocument.ParagraphElementName)) {
            return new SourceCodeParagraphView(elem);
          }
        }

        // Delegate others to StyledEditorKit
        return styledEditorKitFactory.create(elem);
      }
    }
    private static final ViewFactory styledEditorKitFactory = (new StyledEditorKit())
            .getViewFactory();
    
    private static final ViewFactory defaultFactory = new SourceCodeViewFactory();

    @Override
    public ViewFactory getViewFactory(){
        return defaultFactory;
    }
}
