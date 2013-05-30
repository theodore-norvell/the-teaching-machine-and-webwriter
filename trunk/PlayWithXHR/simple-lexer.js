/* Lexing states. */
var INIT = new McLexer.State() ;

/* Tokens will contain all of the tokens. */
var Tokens = [] ;

/* Token classes. */
function NUM(string) {
  this.string = string ;
  this.toString = function () { return "NUM(" + string + ")" ; } ;
}

function ID(string) {
  this.string = string ;
  this.toString = function () { return "ID(" + string + ")" ; } ;
}


/* Rules. */
INIT (/[A-Za-z_]+/) (function (match,rest,state) {
    Tokens.push(new ID(match[0])); 
    return state.continuation(rest) ;
 }) ;

INIT (/[0-9]+/) (function (match,rest,state) {
    Tokens.push(new NUM(match[0])); 
    return state.continuation(rest) ;
 }) ;

INIT (/\s+/) (McCONTINUE(INIT)) ;

INIT (/$/) (function (match,rest,state) {
    // Stop lexing.
    return null ;
  }) ;


/* Testing. */

INIT.lex("foo bar 123 baz") ;

// Tokens == [ID("foo"),ID("bar"),NUM(123),ID("baz")]  
