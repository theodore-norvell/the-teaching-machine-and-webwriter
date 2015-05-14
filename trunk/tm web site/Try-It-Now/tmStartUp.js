////////////////////////////////////////////////
// Teaching Machine start up                  //
////////////////////////////////////////////////

/** The following function should not be called from JavaScript.
* It is intended to be called from the TM applet when the "start"
* message is received.
*/
///////////////////////////////////////////////
// Teaching Machine start up                 //
///////////////////////////////////////////////

var hasTheTMCalledBack = false ; // Only used in the next function

// This function is called from the TM once it has started.
function theTMIsInitialized() {
	consoleDebug("the TM has called back" ) ;
	if( hasTheTMCalledBack ) return ;
	consoleDebug("Calling tryRunningTM") ;
	setTimeout( tryRunningTheTM, 1 ) ;
	hasTheTMCalledBack = true ;
}
  
var TMApplet = null ; // Only used within the next function.

function checkForTMApplet() {
	consoleDebug("Getting the applet. Check whether it has already been got.") ;	
	if (TMApplet != null) {
		consoleDebug("Looks like we got it already") ;
		return {tm: TMApplet} ; }
	
	consoleDebug("Applet is not yet got. Check on whether java is supported.") ;
	
	if( ! navigator.javaEnabled() ) {
		consoleError("Looks like Java is not supported.") ;
		return "noSupport" ; }
		
	consoleDebug("Looks like Java is supported. Look for the element.") ;
	
	var tm = document.getElementById("teachingMachine") ;
	if (tm == null) {
		consoleError('document.getElementById("teachingMachine") has failed.') ;
		return "missing" ; }
		
	consoleDebug('document.getElementById("teachingMachine") has succeeded.') ;
	
	consoleDebug('checkForTMApplet has succeeded') ;	
	TMApplet = tm ;
	// Wrap the result so that clients that fail to check
	// for a case will bomb at that point.
	return {tm: TMApplet} ;
}

//The following variable is only for use of the next couple of functions
var tmAppletIsReady = false ;

// This function should be called whenever the application needs the TM.
// If the result is null, the TM could not be run and the user has already been informed
// by means of an alert.
// If the result is not null, the TM has already fielded at least one method call,
// so it should be ready to go.
function getTMApplet() {
	consoleDebug("getTMApplet ..." ) ;
	var result = checkForTMApplet() ;
	if( result == "noSupport" ) {
		var alertMessage = "Unable start the Teaching Machine. "
				 + "The problem may be that you do not have the "
				 + "Java Runtime Environment intalled on your computer. "
				 + "For most desktop and laptop computers, "
				 + "Java can be obtained from Oracle corporation: "
				 +" http://java.com/en/download/index.jsp. "
				 + "Some browsers (notably Chrome) may require NPAPI to be enabled.";
		alert(alertMessage) ;
		consoleDebug("... returns null" ) ;
		return null ; }
	else if( result == "missing" ) {
		consoleDebug("... returns null" ) ;
		alert("The Teaching Machine can not be found." ) ;
		return null ; }
	else if( result.tm && !tmAppletIsReady ) {
		consoleDebug("... returns null" ) ;
		alert("The Teaching Machine was not ready yet. Please try again." ) ;
		return null ; }
	else if( result.tm && tmAppletIsReady ) {
		consoleDebug("... returns"+result.tm ) ;
		return result.tm ; }
	else consoleError("Bad result from checkForTMApplet" ) ;
}

var tmReadyCallBack = null ;

function setTMReadyCallBack( thunk ) {tmReadyCallBack = thunk;}

// This routine is called only once the tm has called back.
// We then send it a message to check that communication goes both ways.
// Once the TM has called back and a successful call has been made to it,
// we can set global flag 'tmAppletIsReady'.
function tryRunningTheTM() {  
	consoleDebug("tryRunningTheTM") ;
	var result = checkForTMApplet() ;
	if( result == "noSupport" ) {
		consoleError("Shouldn't happen: tryRunningTheTM:0") ; }
	else if( result == "missing" ) {
		consoleError("Shouldn't happen: tryRunningTheTM:1") ;}
	else if( result.tm && !tmAppletIsReady ) {
		try {
			// Call any old accessor just to see if the TM is really alive and kicking.
			consoleDebug("sending a 'isTMShowing' message") ;
			result.tm.isTMShowing( ) ;
			consoleDebug("send succeeded.") ;
			//Success.
			tmAppletIsReady = true ; 
		} catch( ex ) {
			consoleError("Calling the tm in 'tryRunningTheTM' threw ...") ;
			consoleError(ex.toString()) ;
			consoleStackTrace( ex ) ; } }
	else if( result.tm && tmAppletIsReady ) {
		consoleError("Shouldn't happen: tryRunningTheTM:2") ; }
	else  consoleError("Bad result from checkForTMApplet" ) ;
	
	// If for any reason there is a failure, try again later.
	if( ! tmAppletIsReady ) { setTimeout(tryRunningTheTM, 1000) ; }
	else {
		if( tmReadyCallBack!=null ) tmReadyCallBack.call() ;
	}
}
