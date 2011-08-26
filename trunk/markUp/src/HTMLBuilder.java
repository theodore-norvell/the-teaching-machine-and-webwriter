/* (C) Theodore S. Norvell 2005 */

public class HTMLBuilder implements TokenClassConstants {
    public HTMLBuilder() {
    }

    private StringBuffer codeLines = new StringBuffer() ;
    private StringBuffer currentLine = new StringBuffer() ;

    public String getCodeLines () { return codeLines.toString() ; }

    private int currentColour = PLAIN ;

    private void startColour( int colourClass ) {
        if( currentColour != PLAIN ) endColour() ;
        switch( colourClass ) {

          case PLAIN : { }
          break ;

          case KEYWORD :{
            currentLine.append( "<SPAN class=\"codeKeyword\">" ) ; }
          break ;

          case COMMENT :{
            currentLine.append( "<SPAN class=\"codeComment\">" ) ; }
          break ;

          case CONSTANT :{
            currentLine.append( "<SPAN class=\"codeConstant\">" ) ; }
          break ;

          case PREPROCESSOR : {
            currentLine.append( "<SPAN class=\"codePreprocessor\">" ) ; }
          break ;

          default : throw new Error("assert failed") ;
        }
        currentColour = colourClass ;
    }

    private void endColour() {
        switch( currentColour ) {

          case PLAIN : { }
          break ;

          case KEYWORD :
          case COMMENT :
          case CONSTANT :
          case PREPROCESSOR : {
            currentLine.append( "</SPAN>" ) ; }
          break ;

          default : throw new Error("assert failed") ; }
        currentColour = PLAIN ;
    }

    public void flush() {
        if( currentColour != PLAIN ) endColour() ;
        if( currentLine.length() != 0 ) {
            currentLine.append( '\n' ) ;
            codeLines.append( currentLine.toString() ) ; }
    }

    public void digestToken(Token token, int colourClass, boolean forceEndAfter ) {

        if( currentColour != colourClass ) {
            endColour() ; }

        for( int i = 0 ; i < token.image.length() ; ++i ) {

            if( currentColour != colourClass ) {
                startColour( colourClass ) ; }

            char ch = token.image.charAt( i ) ;
            switch( ch ) {

               case '\t' : {
                 // TABS are assumed to be every 4 spaces!!!
                 do {
                     currentLine.append( " " ) ;
                 } while( currentLine.length() % 4 != 0 ) ; }
               break ;

               case '&' : {
                 currentLine.append( "&amp;" ) ; }
               break ;

               case '<' : {
                 currentLine.append( "&lt;" ) ; }
               break ;

               case '>' : {
                 currentLine.append( "&gt;" ) ; }
               break ;

              case '\n' : {
                endColour() ;
                currentLine.append( '\n' ) ;
                codeLines.append( currentLine.toString() ) ;
                currentLine.setLength(0) ;}
              break ;

              case '\r' : {}
              break ;

              default : {
                 currentLine.append( ch ) ; } } }
        
        if(forceEndAfter) endColour() ;
        
    }
}