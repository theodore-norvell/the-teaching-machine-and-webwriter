/// <reference path="JSTM.ts" />

module jstm {

    function tmAdder( tm : JSTM ) {
        return function( element : HTMLElement ) : void {
            var name = element.getAttribute("name") ;
            var newElem = null ;
            var varName = null ;
            switch( name ) {
            case "tm-expDisp" :
                newElem = tm.makeExpressionDisplay() ; break ;
            case "tm-goForward" :
                newElem = tm.makeGoForwardButton() ; break ;
            case "tm-goBack" : 
                newElem = tm.makeGoBackButton() ; break ;
            case "tm-var-watcher" :
                varName = element.getAttribute("data-var-name") ;
                newElem = tm.makeVariableWatcher( varName ) ;
                break ;
            }
            if( newElem != null ) replace( element, newElem ) ;
      }
    }

    function replace( oldElem : HTMLElement, newElem : HTMLElement ) {
        oldElem.parentNode.replaceChild( newElem, oldElem ) ; }


    function walk( node : HTMLElement, operation : ( node : HTMLElement ) => void ) : void {
        // Apply the operation
        operation( node ) ;

        // Copy the children that are Elements.
        var children = node.childNodes ;
        var childArray = new Array<HTMLElement>() ;
        var i : number ;
        var j : number = 0 ;
        for( i = 0 ; i < children.length ; ++i ) {
            if(  children.item(i).nodeType == document.ELEMENT_NODE ) {
                childArray[j++] = <HTMLElement> children.item(i) ; } }

        // Recurse on the children
        for( i = 0 ; i < childArray.length ; ++i ) {
            walk( childArray[i], operation ) ; }
    }

    export function addTM( tm : JSTM, root: HTMLElement ) {
        walk( root, tmAdder( tm ) ) ;
    }
 }
