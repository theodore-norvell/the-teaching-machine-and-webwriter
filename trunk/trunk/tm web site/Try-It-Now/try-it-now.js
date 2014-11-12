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

function languageChange() {
	switchLanguageTo( getLanguage() ) ; 
}

function hide( language ) {
	if( language == "java" ) {
		 javaEditorContainer.style.transform = "rotateY(180deg)" ;
		 javaEditorContainer.style.WebkitTransform = "rotateY(180deg)" ; }
	else {
		 cppEditorContainer.style.transform = "rotateY(180deg)" ;
		 cppEditorContainer.style.WebkitTransform = "rotateY(180deg)" ; 
	}
}

function show( language ) {
	//document.getElementById("runButton").disabled = false ;
	if( language == "java" ) {
		 javaEditorContainer.style.transform = "rotateY(0deg)" ;
		 javaEditorContainer.style.WebkitTransform = "rotateY(0deg)" ; }
	else {
		 cppEditorContainer.style.transform = "rotateY(0deg)" ;
		 cppEditorContainer.style.WebkitTransform = "rotateY(0deg)" ; 
	}
}

function switchLanguageTo( language ) {
	var topContainer = document.getElementById("topContainer") ;
	topContainer.style.visibility = "visible" ;
	
	if( language == currentLanguage ) return ;
	
	if( language == "java" ) {
		hide( "cpp" ) ;
		show('java') ;
		currentLanguage = 'java'  ;
	} else if( language == "cpp" ) {
		hide("java" ) ;
		show('cpp') ;
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
	var result = TM.getTMApplet() ;
	if( result.status != "ok" ) return ;
	
	showLoadWarning() ;
	setTimeout( function(){ continueSendToTM(result) ; }, 1 ) ; }
	
function continueSendToTM(result) {
	showTheTM( ) ;
	dismissLoadWarning() ;
	var text ;
	if( currentLanguage == 'cpp' ) sourceText = cppEditor.getValue("\n") ;
	else sourceText = javaEditor.getValue("\n") ;
	var fileName = "tryItNow." + currentLanguage ;
	consoleDebug("fileName: " + fileName + " sourceText: " + sourceText ) ;
	try {
		result.tm.loadString( fileName, sourceText ) ; }
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
	//var tm = document.getElementById("teachingMachine") ;
	//if( tm != null ) {
	//	tm.setAttribute("width", "800" ) ;
	//	tm.setAttribute("height", "600" ) ; }
	//document.getElementById("runButton").disabled = true ;
	//document.getElementById("editButton").disabled = false ;
}

function hideTheTM() {
	var theTMCont = document.getElementById("teachingMachineContainer") ;
	var theEditorContCont = document.getElementById("editorContainerContainerContainer") ;
	
	theTMCont.setAttribute("class", "onBottom") ;
	theEditorContCont.setAttribute("class", "onTop") ;
	//var tm = document.getElementById("teachingMachine") ;
	//if( tm != null ) {
	//	tm.setAttribute("width", "1" ) ;
	//	tm.setAttribute("height", "1" ) ; }
	//document.getElementById("runButton").disabled = false ;
	//document.getElementById("editButton").disabled = true ;
}

function onLoad() {
	consoleDebug("onLoad starts") ;
	TM.setTMReadyCallBack( dismissWarning ) ;
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
	//document.getElementById("runButton").disabled = true ;
	//document.getElementById("editButton").disabled = true ;
	
	//setInterval( function (){ consoleDebug( checkForTMApplet() )  ; }, 100 ) ;
	consoleDebug("done onLoad") ;
}
