/* (C) Theodore S. Norvell 2005 */

var markUp = {}

markUp.PLAIN = 0 ;
markUp.COMMENT = 1 ;
markUp.CONSTANT = 2 ;
markUp.KEYWORD = 3;
markUp.PREPROCESSOR = 4 ;

markUp.plain = function( image, forceEndAfter ) {
    markUp.digestToken( image, markUp.PLAIN, forceEndAfter ) ; }

markUp.comment = function( image, forceEndAfter ) {
    markUp.digestToken( image, markUp.COMMENT, forceEndAfter ) ; }

markUp.constant = function( image, forceEndAfter ) {
    markUp.digestToken( image, markUp.CONSTANT, forceEndAfter ) ; }

markUp.keyword = function( image, forceEndAfter ) {
    markUp.digestToken( image, markUp.KEYWORD, forceEndAfter ) ; }

markUp.preprocessor = function( image, forceEndAfter ) {
    markUp.digestToken( image, markUp.PREPROCESSOR, forceEndAfter ) ; }
    
markUp.init = function() {
    markUp.codeLines = "" ;
    markUp.currentLine = "" ;
    markUp.currentColour = markUp.PLAIN; }

markUp.finish = function(str, rest, state) {
                     markUp.endColour() ;
                     markUp.flush() ;
                     return null ; }

markUp.startColour = function( colourClass ) {
        if( markUp.currentColour != markUp.PLAIN ) markUp.endColour() ;
        switch( colourClass ) {
          case markUp.PLAIN : { }
          break ;

          case markUp.KEYWORD : {
            markUp.currentLine += "<SPAN class=\"codeKeyword\">" ; }
          break ;

          case markUp.COMMENT : {
            markUp.currentLine += "<SPAN class=\"codeComment\">" ; }
          break ;

          case markUp.CONSTANT :{
            markUp.currentLine += "<SPAN class=\"codeConstant\">" ; }
          break ;

          case markUp.PREPROCESSOR : {
            markUp.currentLine += "<SPAN class=\"codePreprocessor\">" ; }
          break ;

          default : alert("assert failed") ;
        }
        markUp.currentColour = colourClass ;
    }

markUp.endColour = function() {
        switch( markUp.currentColour ) {

          case markUp.PLAIN : { } break ;
          case markUp.KEYWORD :
          case markUp.COMMENT :
          case markUp.CONSTANT :
          case markUp.PREPROCESSOR : {
            markUp.currentLine += "</SPAN>" ; }
          break ;

          default : alert("assert failed") ; }
        markUp.currentColour = markUp.PLAIN ;
    }

markUp.flush = function() {
    markUp.codeLines += markUp.currentLine ;
    markUp.currentLine = "" ; }
    
markUp.digestToken = function(image, colour, forceEndAfter ) {
        forceEndAfter = forceEndAfter ? true : false ;
        if( markUp.currentColour != colour ) {
            markUp.endColour() ; }

        for( var i = 0 ; i < image.length ; ++i ) {

            if( markUp.currentColour != colour ) {
                markUp.startColour( colour ) ; }

            var ch = image.charAt( i ) ;
            switch( ch ) {

               case "\t" : {
                 // TABS are assumed to be every 4 spaces!!!
                 do {
                     markUp.currentLine += " " ;
                 } while( markUp.currentLine.length % 4 != 0 ) ; }
               break ;

               case "&" : {
                 markUp.currentLine += "&amp;" ; }
               break ;

               case "<" : {
                 markUp.currentLine += "&lt;" ; }
               break ;

               case ">" : {
                 markUp.currentLine += "&gt;" ; }
               break ;

              case "\n" : {
                markUp.endColour() ;
                markUp.currentLine += "\n" ;
                markUp.flush() ;}
              break ;

              case "\r" : {}
              break ;

              default : {
                 markUp.currentLine += ch ; } } }
        
        if(forceEndAfter) markUp.endColour() ;
        
    }
    