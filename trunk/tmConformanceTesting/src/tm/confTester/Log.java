/*
 * Created on Mar 29, 2005
 *
 */
package tm.confTester;

import java.awt.Color;
import java.awt.Font;
import java.io.* ;

import javax.swing.text.*;
import javax.swing.SwingUtilities;

/** Log class stolen from autotest2
 * 
 * @author theo
 *
 */
public class Log {
        private PrintWriter logStrm ;
        private boolean inBox = false ;
        private JTextComponent textComponent ;
        private SimpleAttributeSet currentAttributeSet = new SimpleAttributeSet() ;

        public Log( JTextComponent textComponent ) {
            this.textComponent = textComponent ;
            textComponent.setAutoscrolls(true);
        }

        synchronized public void setFile( File logf ) throws IOException {
            close() ;
            logStrm = new PrintWriter( new FileOutputStream( logf ) ) ;
        }

        synchronized public void turnOffFileLogging() {
            close() ;
            logStrm = null ;
        }

        synchronized public void clearScreen() {
            SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        textComponent.setText(""); } }
            ) ;
        }

        
        synchronized public void setColor( Color color ) {
            currentAttributeSet.removeAttribute(StyleConstants.ColorConstants.Foreground);
            currentAttributeSet.addAttribute(
                    StyleConstants.ColorConstants.Foreground,
                    color ) ; }

        synchronized public void startBox(String title) {
            println("") ;
            println( "/--"+title+"----------------------------------------------------------") ;
            inBox = true ; }

        synchronized public void endBox() {
            inBox = false ;
            println("") ; }
        
        synchronized public void println( String str ) {
            if( inBox ) { str = "| "+str ; }
            print( str + "\n" ) ; }
        
        synchronized public void println( String str, Color color ) {
            setColor( color ) ;
            if( inBox ) { str = "| "+str ; }
            print( str + "\n" ) ;
            setColor( Color.BLACK ) ; }

        synchronized public void print( String str ) {
            final String str1 = str ;
            final AttributeSet attrSet1 = (AttributeSet) currentAttributeSet.clone() ;
            SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        Document doc = textComponent.getDocument() ;
                        int len = doc.getLength() ;
                        try {
                            doc.insertString( len, str1, attrSet1 ) ;
                            len = doc.getLength() ;
                            textComponent.setCaretPosition( len ); }
                        catch( javax.swing.text.BadLocationException e ) {} } }
            ) ;
            if( logStrm != null ) logStrm.println( str ) ;
        }

        synchronized public void close() {
            if( logStrm != null ) logStrm.close() ; }
}