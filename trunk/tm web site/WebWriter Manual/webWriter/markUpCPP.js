var markUpCPP = {} ;

markUpCPP.markUp = function( string ) {
    markUp.init() ;
    markUpCPP.defaultState.lex( string ) ;
    return markUp.codeLines ; }

markUpCPP.defaultState     = new McLexer.State() ;
markUpCPP.commentState     = new McLexer.State() ;
markUpCPP.ppDirectiveState = new McLexer.State() ;

// key words.
markUpCPP.keywords = {} ;

markUpCPP.keywords["auto"] = 0 ;
markUpCPP.keywords["bool"] = 0 ;
markUpCPP.keywords["break"] = 0 ;
markUpCPP.keywords["case"] = 0 ;
markUpCPP.keywords["catch"] = 0 ;
markUpCPP.keywords["char"] = 0 ;
markUpCPP.keywords["class"] = 0 ;
markUpCPP.keywords["const"] = 0 ;
markUpCPP.keywords["continue"] = 0 ;
markUpCPP.keywords["default"] = 0 ;
markUpCPP.keywords["delete"] = 0 ;
markUpCPP.keywords["do"] = 0 ;
markUpCPP.keywords["double"] = 0 ;
markUpCPP.keywords["else"] = 0 ;
markUpCPP.keywords["enum"] = 0 ;
markUpCPP.keywords["extern"] = 0 ;
markUpCPP.keywords["false"] = 0 ;
markUpCPP.keywords["float"] = 0 ;
markUpCPP.keywords["for"] = 0 ;
markUpCPP.keywords["friend"] = 0 ;
markUpCPP.keywords["goto"] = 0 ;
markUpCPP.keywords["if"] = 0 ;
markUpCPP.keywords["inline"] = 0 ;
markUpCPP.keywords["int"] = 0 ;
markUpCPP.keywords["long"] = 0 ;
markUpCPP.keywords["namespace"] = 0 ;
markUpCPP.keywords["operator"] = 0 ;
markUpCPP.keywords["new"] = 0 ;
markUpCPP.keywords["private"] = 0 ;
markUpCPP.keywords["public"] = 0 ;
markUpCPP.keywords["redeclared"] = 0 ;
markUpCPP.keywords["register"] = 0 ;
markUpCPP.keywords["return"] = 0 ;
markUpCPP.keywords["short"] = 0 ;
markUpCPP.keywords["signed"] = 0 ;
markUpCPP.keywords["sizeof"] = 0 ;
markUpCPP.keywords["static"] = 0 ;
markUpCPP.keywords["struct"] = 0 ;
markUpCPP.keywords["switch"] = 0 ;
markUpCPP.keywords["template"] = 0 ;
markUpCPP.keywords["this"] = 0 ;
markUpCPP.keywords["throw"] = 0 ;
markUpCPP.keywords["true"] = 0 ;
markUpCPP.keywords["try"] = 0 ;
markUpCPP.keywords["typedef"] = 0 ;
markUpCPP.keywords["union"] = 0 ;
markUpCPP.keywords["unsigned"] = 0 ;
markUpCPP.keywords["using"] = 0 ;
markUpCPP.keywords["virtual"] = 0 ;
markUpCPP.keywords["void"] = 0 ;
markUpCPP.keywords["volatile"] = 0 ;
markUpCPP.keywords["while"] = 0 ;
// White space
markUpCPP.defaultState( /(\s)+/ ) 
               ( function(m, rest, state) {
                     markUp.plain(m[0]);
                     return state.continuation(rest) ; } )
// Single line comment
markUpCPP.defaultState( /\/\/([^\n\r])*(\n|\r|\r\n)?/ ) 
               ( function(m, rest, state) {
                     markUp.comment(m[0]);
                    return state.continuation(rest) ; } )
// Multiline comment
markUpCPP.defaultState( /\/\*/ ) 
               ( function(m, rest, state) {
                     markUp.comment(m[0]);
                     return markUpCPP.commentState.continuation(rest) ; } )
// Multiline comment
markUpCPP.commentState( /\*\// ) 
               ( function(m, rest, state) {
                     markUp.comment(m[0], true);
                     return markUpCPP.defaultState.continuation(rest) ; } )
// Multiline comment
markUpCPP.commentState( /.|\s/ ) 
               ( function(m, rest, state) {
                     markUp.comment(m[0]);
                     return state.continuation(rest) ; } )
// The end of the string
markUpCPP.commentState( /$/ ) 
               ( markUp.finish )
            
// Preprocessor directive start
markUpCPP.defaultState( /#/ ) 
               ( function(m, rest, state) {
                     markUp.preprocessor(m[0]);
                     return markUpCPP.ppDirectiveState.continuation(rest) ; } )
                     
// Preprocessor directive end
markUpCPP.ppDirectiveState( /\n/ ) 
               ( function(m, rest, state) {
                     markUp.preprocessor(m[0]);
                     return markUpCPP.defaultState.continuation(rest) ; } )
                     
// Preprocessor directive body
markUpCPP.ppDirectiveState( /[^\n]/ ) 
               ( function(m, rest, state) {
                     markUp.preprocessor(m[0]);
                     return markUpCPP.ppDirectiveState.continuation(rest) ; } )
// The end of the string
markUpCPP.ppDirectiveState( /$/ ) 
               ( markUp.finish )

// Number Constants are simplified.  E.g. 123ABC is a number
// rather than a number followied by an identifier. But since identifiers
// never immediately follow numbers in a real C++ program, its a
// reasonable simplification.
markUpCPP.defaultState( /[0-9.][xX]?(([eE][+-])|[0-9a-fA-FlL.])*/ ) 
               ( function(m, rest, state) {
                     markUp.constant(m[0]);
                     return state.continuation(rest) ; } )

// Character constants
/*("L")? "'"
   (   (~["'","\\","\n","\r"])
   | ("\\" (
             ["n","t","v","b","r","f","a","\\","?","'","\""]
            |
             "0" (["0"-"7"])? (["0"-"7"])? (["0"-"7"])?
            |
             ("x" | "X") (["0"-"9","a"-"f","A"-"F"])+
           )
     )
   )
   "'" >  
*/
markUpCPP.defaultState( /L?'([^'\\\n\r]|\\[ntvbrfa\\?'\"]|\\0[0-7]?[0-7]?[0-7]?|\\[xX][0-9a-fA-F]+)'/ ) 
               ( function(m, rest, state) {
                     markUp.constant(m[0]);
                     return state.continuation(rest) ; } )
                     
/* <  STRING : ("L")? "\""
   ( ( ~["\"","\\","\n","\r"])
   | ("\\" (
             ["n","t","v","b","r","f","a","\\","?","'","\""]
            |
             "0" (["0"-"7"])? (["0"-"7"])? (["0"-"7"])?
            |
             ("x" | "X") (["0"-"9","a"-"f","A"-"F"])+
           )
     )
   )*
   "\"" >
*/

markUpCPP.defaultState( /"([^"\\\n\r]||\\[ntvbrfa\\?'\"]|\\0[0-7]?[0-7]?[0-7]?|\\[xX][0-9a-fA-F]+)*"/ ) 
               ( function(m, rest, state) {
                     markUp.constant(m[0]);
                     return state.continuation(rest) ; } )

// Identifiers and keywords          
/* ["a"-"z","A"-"Z", "_"] (["a"-"z","A"-"Z","0"-"9","_"])* */
markUpCPP.defaultState( /[a-zA-Z_][a-zA-Z0-9_]*/ ) 
               ( function(m, rest, state) {
                     if( markUpCPP.keywords[m[0]] == 0 )
                        markUp.keyword( m[0] ) ;
                     else 
                        markUp.plain(m[0]);
                     return state.continuation(rest) ; } )

// Everything else. Note . does not include new lines so we use .|\w
markUpCPP.defaultState( /.|\w/ ) 
               ( function(m, rest, state) {
                     markUp.plain(m[0]);
                     return state.continuation(rest) ; } )
// The end of the string
markUpCPP.defaultState( /$/ ) ( markUp.finish )