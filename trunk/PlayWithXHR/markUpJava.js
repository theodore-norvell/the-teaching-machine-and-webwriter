var markUpJava = {} ;

markUpJava.markUp = function( string ) {
    markUp.init() ;
    markUpJava.defaultState.lex( string ) ;
    return markUp.codeLines ; }

markUpJava.defaultState     = new McLexer.State() ;
markUpJava.commentState     = new McLexer.State() ;

// key words.
markUpJava.keywords = {} ;

markUpJava.keywords["abstract"] = 0 ;
markUpJava.keywords["boolean"] = 0 ;
markUpJava.keywords["break"] = 0 ;
markUpJava.keywords["byte"] = 0 ;
markUpJava.keywords["case"] = 0 ;
markUpJava.keywords["catch"] = 0 ;
markUpJava.keywords["char"] = 0 ;
markUpJava.keywords["class"] = 0 ;
markUpJava.keywords["const"] = 0 ;
markUpJava.keywords["continue"] = 0 ;
markUpJava.keywords["default"] = 0 ;
markUpJava.keywords["do"] = 0 ;
markUpJava.keywords["double"] = 0 ;
markUpJava.keywords["else"] = 0 ;
markUpJava.keywords["extends"] = 0 ;
markUpJava.keywords["false"] = 0 ;
markUpJava.keywords["final"] = 0 ;
markUpJava.keywords["finally"] = 0 ;
markUpJava.keywords["float"] = 0 ;
markUpJava.keywords["for"] = 0 ;
markUpJava.keywords["goto"] = 0 ;
markUpJava.keywords["if"] = 0 ;
markUpJava.keywords["implements"] = 0 ;
markUpJava.keywords["import"] = 0 ;
markUpJava.keywords["instanceof"] = 0 ;
markUpJava.keywords["int"] = 0 ;
markUpJava.keywords["interface"] = 0 ;
markUpJava.keywords["long"] = 0 ;
markUpJava.keywords["native"] = 0 ;
markUpJava.keywords["new"] = 0 ;
markUpJava.keywords["null"] = 0 ;
markUpJava.keywords["package"] = 0 ;
markUpJava.keywords["private"] = 0 ;
markUpJava.keywords["protected"] = 0 ;
markUpJava.keywords["public"] = 0 ;
markUpJava.keywords["return"] = 0 ;
markUpJava.keywords["short"] = 0 ;
markUpJava.keywords["static"] = 0 ;
markUpJava.keywords["super"] = 0 ;
markUpJava.keywords["switch"] = 0 ;
markUpJava.keywords["synchronized"] = 0 ;
markUpJava.keywords["this"] = 0 ;
markUpJava.keywords["throw"] = 0 ;
markUpJava.keywords["throws"] = 0 ;
markUpJava.keywords["transient"] = 0 ;
markUpJava.keywords["true"] = 0 ;
markUpJava.keywords["try"] = 0 ;
markUpJava.keywords["void"] = 0 ;
markUpJava.keywords["volatile"] = 0 ;
markUpJava.keywords["while"] = 0 ;
markUpJava.keywords["strictfp"] = 0 ;
// White space
markUpJava.defaultState( /(\s)+/ ) 
               ( function(m, rest, state) {
                     markUp.plain(m[0]);
                     return state.continuation(rest) ; } )
// Single line comment
markUpJava.defaultState( /\/\/([^\n\r])*(\n|\r|\r\n)?/ ) 
               ( function(m, rest, state) {
                     markUp.comment(m[0]);
                    return state.continuation(rest) ; } )
// Multiline comment
markUpJava.defaultState( /\/\*/ ) 
               ( function(m, rest, state) {
                     markUp.comment(m[0]);
                     return markUpJava.commentState.continuation(rest) ; } )
                     
// Multiline comment
markUpJava.commentState( /\*\// ) 
               ( function(m, rest, state) {
                     markUp.comment(m[0], true);
                     return markUpJava.defaultState.continuation(rest) ; } )
// Multiline comment
markUpJava.commentState( /.|\s/ ) 
               ( function(m, rest, state) {
                     markUp.comment(m[0]);
                     return state.continuation(rest) ; } )

// The end of the string
markUpJava.commentState( /$/ ) 
               ( markUp.finish )
               
// Number Constants are over simplified.  E.g. 123ABC is a number
// rather than a number followied by an identifier. But since identifiers
// never immediately follow numbers in a real C++ program, its a
// reasonable simplification.
markUpJava.defaultState( /[0-9.][xX]?(([eE][+-])|[0-9a-fA-FlL.])*/ ) 
               ( function(m, rest, state) {
                     //console.error("Found number");
                     markUp.constant(m[0]);
                     return state.continuation(rest) ; } )

// Character constants (slightly simplified)
markUpJava.defaultState( /'([^'\\\n\r]|\\([ntbrf\\'"]|[0-7][0-7]?[0-7]?))'/ )
               ( function(m, rest, state) {
                     //console.error("Found char");
                     markUp.constant(m[0]);
                     return state.continuation(rest) ; } )

// String constants  (slightly simplified)
markUpJava.defaultState( /"([^"\\\n\r]|\\([ntbrf\\'"]|[0-7][0-7]?[0-7]?))*"/ )
               ( function(m, rest, state) {
                     //console.error("Found string");
                     markUp.constant(m[0]);
                     return state.continuation(rest) ; } )

// Identifiers and keywords (ascii only) */
markUpJava.defaultState( /[a-zA-Z]([a-zA-Z]|[0-9])*/ ) 
               ( function(m, rest, state) {
                     if( markUpJava.keywords[m[0]] == 0 )
                        markUp.keyword( m[0] ) ;
                     else 
                        markUp.plain(m[0]);
                     return state.continuation(rest) ; } )

// Everything else. Note . does not include new lines so we use .|\w
markUpJava.defaultState( /.|\w/ ) 
               ( function(m, rest, state) {
                     markUp.plain(m[0]);
                     return state.continuation(rest) ; } )
// The end of the string
markUpJava.defaultState( /$/ ) ( markUp.finish )