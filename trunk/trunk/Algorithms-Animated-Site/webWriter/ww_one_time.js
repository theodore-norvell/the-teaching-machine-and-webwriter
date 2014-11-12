// JavaScript Document

//if( console && console.debug ) console.debug("Start of ww_one_time.js" );	

///////////////////////
// Debug functions.
///////////////////////
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

///////////////////////////
// Loading
//////////////////////////


window.onload = doneLoading;


function doneLoading(){
	var startPoint = root.doc;
	//alert("location: " + location);
	if (location.search) { // frames weren't loaded originally
		startPoint = location.search.substring(1);
		//alert("startPoint is " + startPoint + "\nself is " + self.location.href);
		startPoint = getRelativeTo(getBaseURL(), startPoint );
		//alert("contents url is " + parent.contents.document.location.href + "\nwhile self url is " + self.location.href);
	}
	//alert("loading " + startPoint + " into contents frame." );
// Using replace instead of setting location directly keeps waiting page out of history list
	parent.contents.location.href = startPoint;
	return true;
}


// Ultimate client-side JavaScript client sniff. Version 3.03
// (C) Netscape Communications 1999-2001.  Permission granted to reuse and distribute.
// Revised 17 May 99 to add is_nav5up and is_ie5up (see below).
// Revised 20 Dec 00 to add is_gecko and change is_nav5up to is_nav6up
//                      also added support for IE5.5 Opera4&5 HotJava3 AOLTV
// Revised 22 Feb 01 to correct Javascript Detection for IE 5.x, Opera 4, 
//                      correct Opera 5 detection
//                      add support for winME and win2k
//                      synch with browser-type-oo.js
// Revised 26 Mar 01 to correct Opera detection
// Revised 02 Oct 01 to add IE6 detection

// Everything you always wanted to know about your JavaScript client
// but were afraid to ask. Creates "is_" variables indicating:
// (1) browser vendor:
//     is_nav, is_ie, is_opera, is_hotjava, is_webtv, is_TVNavigator, is_AOLTV
// (2) browser version number:
//     is_major (integer indicating major version number: 2, 3, 4 ...)
//     is_minor (float   indicating full  version number: 2.02, 3.01, 4.04 ...)
// (3) browser vendor AND major version number
//     is_nav2, is_nav3, is_nav4, is_nav4up, is_nav6, is_nav6up, is_gecko, is_ie3,
//     is_ie4, is_ie4up, is_ie5, is_ie5up, is_ie5_5, is_ie5_5up, is_ie6, is_ie6up, is_hotjava3, is_hotjava3up,
//     is_opera2, is_opera3, is_opera4, is_opera5, is_opera5up
// (4) JavaScript version number:
//     is_js (float indicating full JavaScript version number: 1, 1.1, 1.2 ...)
// (5) OS platform and version:
//     is_win, is_win16, is_win32, is_win31, is_win95, is_winnt, is_win98, is_winme, is_win2k
//     is_os2
//     is_mac, is_mac68k, is_macppc
//     is_unix
//     is_sun, is_sun4, is_sun5, is_suni86
//     is_irix, is_irix5, is_irix6
//     is_hpux, is_hpux9, is_hpux10
//     is_aix, is_aix1, is_aix2, is_aix3, is_aix4
//     is_linux, is_sco, is_unixware, is_mpras, is_reliant
//     is_dec, is_sinix, is_freebsd, is_bsd
//     is_vms
//
// See http://www.it97.de/JavaScript/JS_tutorial/bstat/navobj.html and
// http://www.it97.de/JavaScript/JS_tutorial/bstat/Browseraol.html
// for detailed lists of userAgent strings.
//
// Note: you don't want your Nav4 or IE4 code to "turn off" or
// stop working when new versions of browsers are released, so
// in conditional code forks, use is_ie5up ("IE 5.0 or greater") 
// is_opera5up ("Opera 5.0 or greater") instead of is_ie5 or is_opera5
// to check version in code which you want to work on future
// versions.

    // convert all characters to lowercase to simplify testing
    var agt=navigator.userAgent.toLowerCase();

    // *** BROWSER VERSION ***
    // Note: On IE5, these return 4, so use is_ie5up to detect IE5.
    var is_major = parseInt(navigator.appVersion);
    var is_minor = parseFloat(navigator.appVersion);

    // Note: Opera and WebTV spoof Navigator.  We do strict client detection.
    // If you want to allow spoofing, take out the tests for opera and webtv.
    var is_nav  = ((agt.indexOf('mozilla')!=-1) && (agt.indexOf('spoofer')==-1)
                && (agt.indexOf('compatible') == -1) && (agt.indexOf('opera')==-1)
                && (agt.indexOf('webtv')==-1) && (agt.indexOf('hotjava')==-1));
    var is_nav2 = (is_nav && (is_major == 2));
    var is_nav3 = (is_nav && (is_major == 3));
    var is_nav4 = (is_nav && (is_major == 4));
    var is_nav4up = (is_nav && (is_major >= 4));
    var is_navonly      = (is_nav && ((agt.indexOf(";nav") != -1) ||
                          (agt.indexOf("; nav") != -1)) );
    var is_nav6 = (is_nav && (is_major == 5));
    var is_nav6up = (is_nav && (is_major >= 5));
    var is_gecko = (agt.indexOf('gecko') != -1);


    var is_ie     = ((agt.indexOf("msie") != -1) && (agt.indexOf("opera") == -1));
    var is_ie3    = (is_ie && (is_major < 4));
    var is_ie4    = (is_ie && (is_major == 4) && (agt.indexOf("msie 4")!=-1) );
    var is_ie4up  = (is_ie && (is_major >= 4));
    var is_ie5    = (is_ie && (is_major == 4) && (agt.indexOf("msie 5.0")!=-1) );
    var is_ie5_5  = (is_ie && (is_major == 4) && (agt.indexOf("msie 5.5") !=-1));
    var is_ie5up  = (is_ie && !is_ie3 && !is_ie4);
    var is_ie5_5up =(is_ie && !is_ie3 && !is_ie4 && !is_ie5);
    var is_ie6    = (is_ie && (is_major == 4) && (agt.indexOf("msie 6.")!=-1) );
    var is_ie6up  = (is_ie && !is_ie3 && !is_ie4 && !is_ie5 && !is_ie5_5);

// End of netScape browser sniffer Javascript. Thanks to Netscape!


if (!is_ie6up && !is_nav6up && !is_gecko) {
		alert("Webwriter++ the technology which underpins this site, probably won't work on your browser!" +
			  "\nIt has only been tested fully on IE6 and Mozilla 1.4." +
			  "\nIt should work on Mozilla-based browser (e.g. Netscape)");
}

/*	else if(parent.contents.location.search) { // document started to load before this window loaded (reload?)
		startPoint = parent.contents.location.search.substring(1);
	}*/


function setTitle(title){
	var titleBox = document.getElementById("titleContainer");
	titleBox.innerHTML = title;
}


/**** standard button actions **********************************************
	I did try to do this from webwriter but window.open call appears to be
	occurring relative to where the call first originated from. That is here.
**************************************************************************/

function invokePrint(){
	parent.contents.invokePrint();
}

var isShade = startInLectureMode && startWithShade;
var shadeOffTip = "Turns on the window shade";
var shadeOnTip = "Turns off the window shade";

function isShadeSet(){return isShade;}

function showShadeButton(showIt){
	var button = Button.getById("shadeButton");
	if (button ) button.show(showIt);
}

function toggleShade(){
	isShade = !isShade;
	parent.contents.showShade(isShade);
	parent.sliderFrame.activate(isShade);
	document.getElementById("shadeButton").title = (isShade ? shadeOnTip : shadeOffTip);
	return true;
}

var lectureMode = startInLectureMode;

function isLectureMode(){ return lectureMode;}

function changeModes(){
//alert("changeModes!");
	lectureMode = !lectureMode;
	showShadeButton(lectureMode);
	parent.contents.setStyleSheet( lectureMode? "lecture" : "notes" );
	if (!lectureMode) parent.sliderFrame.activate(false);
//alert("lectureMode is " + (lectureMode ? "true" : "false"));
	return true;
}

var helpTip = "A little help on how to use this site";

function getHelp(){
	alert("Sorry. Help not hooked in yet.");
}

function goHome(){
	top.location = homeUrl;
}


var siteDefaultConfig = defaultConfigurationFile;

function changeConfig(configFile){
	siteDefaultConfig = configFile;
	showConfigPicker(false);
	
//	alert("siteDefaultConfig is " + siteDefaultConfig);
}

function showConfigPicker(show){
	var picker = document.getElementById("configPicker");
	picker.style.visibility= show ? "visible" : "hidden";
}



function getDefaultConfig(){
	return getToConfigurations() + siteDefaultConfig;
}

	

/************************************************************************************************************
	Navigation
************************************************************************************************************/



//generate a table of contents
function toc(){
	parent.contents.location.replace("tocGenerator.htm");
}

function previousTopic(){
	var here = treeWalker.current;
	treeWalker.previous();
	if (here != treeWalker.current) {
		parent.contents.location = treeWalker.current.doc;
		adjustButtons(treeWalker.current);
	}
}

function nextTopic(){
	var here = treeWalker.current;
	treeWalker.next();
	if (here != treeWalker.current) {
		parent.contents.location = treeWalker.current.doc;
		adjustButtons(treeWalker.current);
	}
}

function upTopic(){
	var here = treeWalker.current;
	treeWalker.up();
	if (here != treeWalker.current) {
		parent.contents.location = treeWalker.current.doc;
		adjustButtons(treeWalker.current);
	}
}

function adjustButtons(node) {
	document.getElementById("previous").disabled = (node.previous == null);
	document.getElementById("next").disabled = (node.next == null);
	document.getElementById("up").disabled = (node.parent == null);
}

function atPage(docURL){
//	alert("at page " + docURL + "\ntreeWalker at " + treeWalker.current.doc);
	docURL = getRelativeTo(getBaseURL(),docURL);  // convert doc to relative to this
	if (treeWalker.current == null || docURL != treeWalker.current.doc) {
		var mark = treeWalker.current;
		treeWalker.toRoot();
		while(treeWalker.current != null && treeWalker.current.doc != docURL)
			treeWalker.walk();
		if (treeWalker.current == null) // Not in tree map
			treeWalker.set(mark);
	}
	adjustButtons(treeWalker.current);
}
// base URL for the whole site. ww_one_time is assumed to be invoked by
// navBar.htm which is located in the site root.
function getBaseURL(){
	return stripSearch(self.location.href);
}

/* url's can include a search substring, denoted by a ? followed by the substring. We use it to
	bookmark frame documents. Returns all characters up to the ? */
function stripSearch(url) {
	var end = url.indexOf("?");
	return (end > 0 ? url.substring(0,end) : url);
}


function getRelativeTo(fromURL, toURL){
	//alert("getRelativeTo(" + fromURL + ", " + toURL + ")");
	// find what is common
	fromURL = stripQuery(fromURL);
	toURL = stripQuery(toURL);
	for (i = 0; i < toURL.length; i++){
		if (toURL.charAt(i) != fromURL.charAt(i))
			break;
	}
	// just keep the differences
	var diffFrom = fromURL.substring(i);
	var diffTo = toURL.substring(i);
//	alert("diffFrom is " + diffFrom + " and diffTo is " + diffTo);
	// Create the "../../" backup string from the fromURL
	var backUp = diffFrom.replace(/[^\/]*\//g,"../");
	var end = backUp.lastIndexOf("/");
	if (end == -1) backUp="";
	else
		backUp = backUp.substr(0, end+1);
	return backUp + diffTo;
}

function stripQuery(URL){
	var query = URL.indexOf("?");
	if (query > -1)
		return URL.substr(0,query);
	return URL;
}


function getToImages(){
	return imagesFolder;
}

function getToConfigurations(){
	return configurationsFolder;
}

function getToContent(){
	return contentFolder;
}

function getToWebWriter(){
	return wwFolder;
}

function getToVideos(){
	return videosFolder;
}
	

///////////////////////////////////////////////
// Teaching Machine start up                 //
///////////////////////////////////////////////

var hasTheTMCalledBack = false ; // Only used in the next function

function theTMIsInitialized() {
	consoleDebug("the TM has called back" ) ;
	if( hasTheTMCalledBack ) return ;
	consoleDebug("Calling tryRunningTM") ;
	setTimeout( tryRunningTheTM, 1 ) ;
	hasTheTMCalledBack = true ;
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
	
	consoleDebug('checkForTMApplet has succeeded') ;	
	TMApplet = tm ;
	// Wrap the result so that client that fail to check
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
				 +" http://java.com/en/download/index.jsp ." ;
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
}



//if( console && console.debug ) console.debug("End of ww_one_time.js" );
