var cppEditor ;
var cppEditorContainer ;
var javaEditor ;
var javaEditorContainer ;
var currentLanguage = null ;
var appletIsLoaded = false ;

var debugging = true ;

function consoleError(message){
	if (typeof console == 'undefined')
		alert(message);
	else
		console.error(message);
}

function consoleDebug(message){
	if (debugging) {
	    if (typeof console == 'undefined')
	    	alert(message);
	    else
		    console.debug(message);
	}
}

function consoleDebugObject(object){
	if( debugging ) {
		var out = '';
		for (var p in object) {
			if (!object.hasOwnProperty(p)) out += '(inherited) ';
			out += p + ': ' + object[p] + '\n';
		}
		consoleDebug(out)
	}
}

function consoleStackTrace(error) {
	if( !error ) error = new Error() ;
	if( error.stack ) {
		consoleError( error.stack.toString() ) ; }
	else {
		consoleError(  "no stack trace available" ) ; }
}

function getLanguage() {
	var radios = document.getElementsByName('language');
	for (var i = 0; i < radios.length; i++) {
		if (radios[i].checked) {
			consoleDebug( radios[i].value ) ;
			return radios[i].value ; } }
}


function disableRunButtons() {
	var buttons = document.getElementsByName('run');
	for (var i = 0; i < buttons.length; i++) {
		buttons[i].disabled = true ; }
}


function enableRunButtons() {
	var buttons = document.getElementsByName('run');
	for (var i = 0; i < buttons.length; i++) {
		buttons[i].disabled = false ; }
}


function disableRadioButtons() {
	var controlTable = document.getElementById('controlTable' ) ;
	controlTable.className = 'controlTable disabled' ;
	var radios = document.getElementsByName('language');
	for (var i = 0; i < radios.length; i++) {
		radios[i].disabled = true ; }
}

function enableRadioButtons() {
	var controlTable = document.getElementById('controlTable' ) ;
	controlTable.className = 'controlTable' ;
	var radios = document.getElementsByName('language');
	for (var i = 0; i < radios.length; i++) {
		radios[i].disabled = false ; }
}

function languageChange() {
	switchLanguageTo( getLanguage() ) ; 
}

function swap( oldContainer, newContainer, editor ) {
	disableRadioButtons() ;
	oldContainer.style.transform = "rotateY(90deg)" ;
	oldContainer.style.WebkitTransform = "rotateY(90deg)" ;
	setTimeout( function() {
					oldContainer.style.visibility = 'hidden' ;
					show( newContainer, editor ) ;
					setTimeout( function() {
									oldContainer.style.transform = "rotateY(-90deg)" ;
									oldContainer.style.WebkitTransform = "rotateY(-90deg)" ;},
								10 ) ; },
	            300 ) ;
}

function show( editorContainer, editor ) {	
	disableRadioButtons() ;
    editorContainer.style.visibility = 'visible' ;
	editorContainer.style.transform = "rotateY(0deg)" ;
	editorContainer.style.WebkitTransform = "rotateY(0deg)" ;
	setTimeout( function() { enableRadioButtons() ; editor.focus() ; }, 300 ) ;
}

function switchLanguageTo( language ) {
	var topContainer = document.getElementById("topContainer") ;
	topContainer.style.visibility = "visible" ;
	
	if( language == currentLanguage ) return ;
	
	else if( currentLanguage == null ) {
		if( language == "java" ) {	
			show( javaEditorContainer, javaEditor ) ;
			currentLanguage = 'java'  ;
		} else if( language == "cpp" ) {
			show( cppEditorContainer, cppEditor ) ;
			currentLanguage = 'cpp'  ;
		} else { alert("Unknown language"); }
	} else if( language == "java" ) {
		swap( cppEditorContainer, javaEditorContainer, cppEditor ) ;
		currentLanguage = 'java'  ;
	} else if( language == "cpp" ) {
		swap( javaEditorContainer, cppEditorContainer, javaEditor ) ;
		currentLanguage = 'cpp'  ;
	} else { alert("Unknown language"); }
}

function showPopUp(id) {
	var popup = document.getElementById(id) ;
	popup.className = "popup showing" ;
}

function hidePopUp(id) {
	var popup = document.getElementById(id) ;
	popup.className = "popup hidden" ;
}

function showWarning() {
	consoleDebug("showWarning") ;
	showPopUp("warning") ;
}


function dismissWarning() {
	consoleDebug("dismissWarning") ;
	hidePopUp("warning") ;
}

function showLoadWarning() {
	consoleDebug("showLoadWarning") ;
	showPopUp("loadWarning") ;
}

function dismissLoadWarning() {
	consoleDebug("dismissLoadWarning") ;
	hidePopUp("loadWarning") ;
}

function sendToTM() {
	consoleDebug("onSend") ;
	var tm = getTMApplet() ;
	if( tm == null ) return ;

	showLoadWarning() ;
	setTimeout( function(){ continueSendToTM(tm) ; }, 1 ) ; }
	
function continueSendToTM(tm) {
	disableRadioButtons() ;
	showTheTM( ) ;
	dismissLoadWarning() ;
	var text ;
	if( currentLanguage == 'cpp' ) sourceText = cppEditor.getValue("\n") ;
	else sourceText = javaEditor.getValue("\n") ;
	var fileName = "tryItNow." + currentLanguage ;
	consoleDebug("fileName: " + fileName + " sourceText: " + sourceText ) ;
	try {
		tm.loadString( fileName, sourceText ) ; }
	catch( ex ) {
		consoleError("Exception on call to 'loadString'") ;
		consoleStackTrace( ex ) ;
		
		alert("There was a problem sending the code to the Teaching Machine."
		    + " Try again. If the problem persists, contact Theodore"
			+ " Norvell (theo@mun.ca)" ) ; }
}

function showTheTM() {
	var theTMCont = document.getElementById("teachingMachineContainer") ;
	var theEditorContCont = document.getElementById("editorContainerContainerContainer") ;
	
	theTMCont.setAttribute("class", "onTop") ;
	theEditorContCont.setAttribute("class", "onBottom") ;
}

function hideTheTM() {
	var theTMCont = document.getElementById("teachingMachineContainer") ;
	var theEditorContCont = document.getElementById("editorContainerContainerContainer") ;
	
	theTMCont.setAttribute("class", "onBottom") ;
	theEditorContCont.setAttribute("class", "onTop") ;
	var editor = null ;
	if( currentLanguage == "java" ) {	
		editor = javaEditor ;
	} else if( currentLanguage == "cpp" ) {
		editor = cppEditor ;
	} 
	setTimeout( function() { enableRadioButtons() ; if(editor) editor.focus() ; }, 2000 ) ;
}

function onLoad() {
	consoleDebug("onLoad starts") ;
	disableRunButtons() ;
	setTMReadyCallBack( function() { dismissWarning() ; enableRunButtons() ; }) ;
	cppEditorContainer = document.getElementById('cppEditorContainer') ;
	var cppTextArea = document.getElementById('cppTextArea') ;
	cppEditor = CodeMirror.fromTextArea(cppTextArea, {
					 mode: "text/x-c++src" } );
	
	javaEditorContainer = document.getElementById('javaEditorContainer') ;
	var javaTextArea = document.getElementById('javaTextArea') ;
	javaEditor = CodeMirror.fromTextArea(javaTextArea, {
					 mode: "text/x-java" } );
					 
	document.getElementById("cppRadio").checked = false ;
	document.getElementById("javaRadio").checked = false ;
	enableRadioButtons() ;
	
	//setInterval( function (){ consoleDebug( checkForTMApplet() )  ; }, 100 ) ;
	consoleDebug("done onLoad") ;
}

