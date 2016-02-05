/// <reference path="JSTM.ts" />

module jstm {


    export class MockJSTM implements jstm.JSTM {

        private expressionDisplays = new Array<HTMLElement>() ;
        private varVals_tempC = new Array<HTMLElement>() ;
        private varVals_tempF = new Array<HTMLElement>() ;

        private numStates = 8 ;
        private k = 0 ;

        private expressionStringToHTML( str : string ) : string {
	        var i ;
	        var html = "" ;
	        for( i=0 ; i < str.length ; ++i ) {
		        var c = str.charAt(i) ;
		        switch( c ) {
		        case "<" : html += "&lt;" ; break ;
		        case ">" : html += "&gt;" ; break ;
		        case "&" : html += "&amp;" ; break ; 
		        case "\uffff" : html += '<span class="tm-red">' ; break ;
		        case "\ufffe" : html += '<span class="tm-underline">' ; break ;
		        case "\ufffc" : html += '<span class="tm-blue">' ; break ;
		        case "\ufffb" : html += '</span>' ; break ;
		        default : html += c ; } }
	        return html ; }

        updateDisplays() {
            console.log("updateDisplays, k is " + this.k ) ;
            var exp = null ;
            var tempC = null ;
            var tempF = null ;
            switch( this.k ) {
            case 0 : exp = "" ;
                     tempC = "" ;
                     tempF = "" ;
                     break ;
            case 1 : exp = "\ufffetempF\ufffb = (tempC * 5 / 9) + 32 " ;
                     tempC = "10.0" ;
                     tempF = "0.0" ;
                     break ;
            case 2 : exp = "\ufffctempF\ufffb = (\ufffetempC\ufffb * 5 / 9) + 32 " ;
                     tempC = "10.0" ;
                     tempF = "0.0" ;
                     break ;
            case 3 : exp = "\ufffctempF\ufffb = (\ufffe\ufffctempC\ufffb\ufffb * 5 / 9) + 32 " ;
                     tempC = "10.0" ;
                     tempF = "0.0" ;
                     break ;
            case 4 : exp = "\ufffctempF\ufffb = (\uffff10\ufffb * \ufffe5\ufffb / 9) + 32 " ;
                     tempC = "10.0" ;
                     tempF = "0.0" ;
                     break ;
            case 5 : exp = "\ufffctempF\ufffb = (\ufffe\uffff10\ufffb * \uffff5.0\ufffb\ufffb / 9) + 32 " ;
                     tempC = "10.0" ;
                     tempF = "0.0" ;
                     break ;
            case 6 : exp = "\ufffe\ufffctempF\ufffb = \uffff50.0\ufffb\ufffb" ;
                     tempC = "10.0" ;
                     tempF = "0.0" ;
                     break ;
            case 7 : exp = "\uffff50.0\ufffb" ;
                     tempC = "10.0" ;
                     tempF = "50.0" ;
                     break ;
            }
            var i : number
            if( exp != null ) {
                this.expressionDisplays.forEach( (d) =>
                    d.innerHTML = this.expressionStringToHTML( exp ) ) ; }
            if( tempC != null ) {
                this.varVals_tempC.forEach( (d) =>
                    d.innerHTML = tempC ) ; }
            if( tempF != null ) {
                this.varVals_tempF.forEach( (d) =>
                    d.innerHTML = tempF ) ; }
        }

        makeExpressionDisplay() : HTMLElement {
            var  expDisp = document.createElement( "div" ) ;
            expDisp.setAttribute( "class", "tm-expression-display" ) ;
            this.expressionDisplays.push( expDisp ) ;
            return expDisp ;
        }

        makeGoForwardButton() : HTMLElement {
            var button = document.createElement( "button" ) ;
            button.setAttribute( "class", "tm-button" ) ;
            button.onclick = () => this.goForward() ;
            button.innerHTML = "-&gt;" ;
            return button ;
        }

        makeGoBackButton() : HTMLElement {
            var button = document.createElement( "button" ) ;
            button.setAttribute( "class", "tm-button" ) ;
            button.onclick = () => this.goBack() ;
            button.innerHTML = "&lt;-" ;
            return button ;
        }

        makeVariableWatcher( varName : string ) : HTMLElement {
            var varWatcher = document.createElement( "span" ) ;
            varWatcher.setAttribute( "class", "tm-var-watcher" ) ;
            var text = document.createTextNode( varName + " : ") ;
            var value = document.createElement( "span" ) ;
            value.setAttribute( "class", "tm-var-value" ) ;
            value.innerHTML = "" ;
            varWatcher.appendChild( text ) ;
            varWatcher.appendChild( value ) ;
            if( varName === "tempC" ) {
                this.varVals_tempC.push( value ) ; }
            else if( varName === "tempF" ) {
                this.varVals_tempF.push( value ) ; }
            return varWatcher ;
        }

        loadString( program : string, language : string ) : void {
        }

        getStatus() : number {
            return 3 ;
        }

        go( commandString : string ) : void {
        }

        goForward() : void {
            this.k = (this.k + 1) % this.numStates ; 
            this.updateDisplays() ; }

        goBack() : void {
            this.k = (this.k + this.numStates - 1) % this.numStates ; 
            this.updateDisplays() ; }
    }
}
