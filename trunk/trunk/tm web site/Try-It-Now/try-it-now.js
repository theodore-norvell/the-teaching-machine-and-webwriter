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

function consoleStackTrace(error) {
	if( !error ) error = new Error() ;
	if( error.stack ) {
		consoleError( error.stack.toString() ) ; }
	else {
		consoleError(  "no stack trace available" ) ; }
}
var TMApplet = null ; // Only used within the next function.
function checkForTMApplet() {
	consoleDebug("Getting applet") ;
	
	if (TMApplet != null) {
		consoleDebug("Looks like we got it already") ;
		return {tm: TMApplet} ; }
	
	consoleDebug("Checking on whether java is supported.") ;
	if( ! navigator.javaEnabled() ) {
		consoleError("Looks like Java is not supported.") ;
		return "noSupport" ; }
	consoleDebug("Looks like Java is supported.") ;
	
	var tm = document.getElementById("teachingMachine") ;
	if (tm == null) {
		consoleError('document.getElementById("teachingMachine") has failed.') ;
		return "missing" ; }
	consoleDebug('document.getElementById("teachingMachine") has succeeded.') ;
	
	//if( ! tm.loadRemoteFile ) {
	//	consoleError("TMApplet does not have a loadRemoteFile function") ; 
	//	return "notReady" ;  }
		
	TMApplet = tm ;
	// Wrap the result so that client that fail to check
	// for a case will bomb at that point.
	return {tm: TMApplet} ;
}

function getTMApplet() {
	var result = checkForTMApplet() ;
	if( result == "noSupport" ) {
		var alertMessage = "Unable start the Teaching Machine. "
	             + "The problem may be that you do not have the "
				 + "Java Runtime Environment intalled on your computer. "
			 	 + "For most desktop and laptop computers, "
				 + "Java can be obtained from Oracle corporation: "
			 	 +" http://java.com/en/download/index.jsp ." ;
		alert(alertMessage) ;
		return null ; }
	else if( result == "missing" ) {
		alert("The Teaching Machine can not be found." ) ;
		return null ; }
	else if( result == "notReady" ) {
		alert("The Teaching Machine is not ready yet. Please try again." ) ;
		return null ; }
	else if( result.tm ) {
		return result.tm ; }
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

var warningHasBeenShown = false ;

function showWarning() {
	if( warningHasBeenShown ) return ;
	var warning = document.getElementById("warning") ;
	warning.style.visibility = "visible" ;
	warningHasBeenShown = true ;
}

function dismissWarning() {
	var warning = document.getElementById("warning") ;
	warning.style.visibility = "hidden" ;
}

function sendToTM() {
	consoleDebug("onSend") ;
	var tm = getTMApplet() ;
	if(tm==null || currentLanguage == null ) return ;
	
	showWarning() ;
	
	setTimeout( function() { continueSendToTM(tm, currentLanguage) ; }, 100 ) ;
}

function continueSendToTM(tm, currentLanguage) {
	showTheTM() ;
	var text ;
	if( currentLanguage == 'cpp' ) sourceText = cppEditor.getValue("\n") ;
	else sourceText = javaEditor.getValue("\n") ;
	var fileName = "tryItNow." + currentLanguage ;
	consoleDebug("fileName: " + fileName + " sourceText: " + sourceText ) ;
	try {
		tm.loadString( fileName, sourceText ) ; }
	catch( ex ) {
		consoleDebug("Exception on call to 'loadString'") ;
		consoleStackTrace( ex ) ; }
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
