// <script LANGUAGE="JavaScript">
/******************************************************************************
 JavaScript code by
   	Michael Bruce-Lockhart
	Copyright 2000, 2001, 2004
	All rights reserved. May not be used, copied, distributed or reverse engineered without the permission of the author.
***********************************************************************************/


/******* REVISION HISTORY ************************************************************
	1.12.2001: Repaired writeNavTable to get bullet gifs in automenus located no matter
				where page is loaded.
	 
	1.11.2001: Added code to render properly html pop-ups inserted into c code as pedagogical
			markups.
			
    12.21.2001: Added new browser detection and merged in some Mozilla specifics.
	
	12.31.2001: Allowed configuration files to be specified in insertCode
	01.15.2002: Changed so pushing video button seeks video file in site_resources/videos
	08.23.2002: Changed insertCode to set class name instead of width and alignment, thus allowing appearance of code to be controlled by CSS
				Added H /H commands to pedagogical markup to allow code to be hidden in display. Used to display a function then send it and a main to drive it to the TM
	10.15.2002: Added an optional switch letter to H command to allow same code to be displayed multiple
				times with different hides
				Added example URL to TM button prompt
	2.17.2004:  MAJOR UPGRADE: 	1. Mozilla support added.
								2. Moved to 4th Generation Browsers only (JScript 5.5/JavaScript 1.5/ CSS2)
								3. Pop-ups and definitions work (slow Mozilla down)
								4. Added a top level navigation bar frame which normally only loads once
								5. Added modes to allow for differences between lecture/projection and study/personal viewing
								6. Added a window shade for lecture mode which can be turned on/off
								7. Split scripting between one time (navBar & slider) and per page (this script)
								8. Added notes which can be turned off (e.g. for lectures) / turned on.
								9. Reskinned TM display with title barincluding buttons.

************************************************************************************/ 

/*************************** TO DO LIST ****************************************

	1. Fix slides or remove them (no Mozilla support yet)
	2. Revise (simplify) rollover buttons
	3. Review (do away with) my image caching
	4. New manual
	5. TOC and indexing
	6. Review synchrony between co-operating frames. Make it bulletproof and simple. Needs to
		recognize only contents frame (containing these scripts) changes continually. Perhaps the
		navBar scripts can run an all-installed check. Work out state transition diagram!
		Explorer: can bookmark frames, Mozilla cannot. Back button works in both
		Reload works in Explorer unless I've bookmarked a frame in which case site has default?frameurl
		Reload in Foxfire takes me back to default. 
	7. Fix known bugs
	8. Start and end pop-ups is in but not implemented.
	9. Consider Frames for IE and Fixed for Mozilla.
	10. TM Logo
	
	
**************************** KNOWN BUGS ****************************************

	1. In IE Compliant mode document renders under scrollbar, putting up horizontal scrollbar.
		I couldn't get published fix to work.
	2. slider camouflage doesn't pick up style changing (no way I know of to check when style sheet
		is loaded. Should be able to fix if not a separate thread.
	3. Popups or more likely popup links cause Mozilla to get jumpy when they roll onto the screen

********************************************************************************************/


// Very basic switch. More sophisticated browser check/watning is left to one-time code
	var isIE = (document.all != null);
//	alert("isIE is " + (isIE ? "true" : "false"));


var loadApplet=null;
var parseApplet=null;
var TMApplet = null;

// Technically this is no applets as well. An effort to force reload to behave because I figured && top.navBarFrame.appletsLoaded
// reload is touching of a race between frames so we just treat it as if we were starting on a page not at the top.
var noAppletFrame = ((parent && parent.navBarFrame ) ? false : true);
//if(!noAppletFrame) alert("applets are " + (parent.navBarFrame.appletsLoaded ? "" : "not ") + "loaded");
var isLoaded = false;
var thisDoc = self.location.href;

/* This is temporary and should move to document. I would love to automate it but it we have to contend
 	with the possibility that the page may have been loaded directly in which case the frames are not
	loaded and there is no way to measure how deeply the page is embedded in the site tree.
*/

if (!nestingDepth)
	var nestingDepth = "../../";
/* If there is no frame for applets it is because the page was entered directly,
  bypassing the frames of the default.htm page in the root of the site tree.
*/
  
if (noAppletFrame) {
	/* Then, the script attempts to load the frames then reload this page.
  This will work if the variable nestingDepth is set properly on the actual page
  before this script is invoked. If it does work, the whole script will be terminated
  then invoked again when its page is reloaded inside the contents frame
  */

	var redirect = nestingDepth + "default.htm?" + thisDoc;
	//alert("direct load: frames are missing. Redirecting to\n" + redirect);
	window.location.replace(redirect);
}
 else {  //2006.04.13 - Don't think this is needed and its causing grief with webCT
	/* Either the page was entered in the normal way or the above procedure succeeded
	and the page is now being reloaded with the frames in place. Now we measure nestingDepth
	directly. */
	var oneTimeScript = parent.navBarFrame;
	//alert("thisDoc is " + thisDoc + "\nwhile parent is " + parent.location.href);
	nestingDepth = peelName(getRelativeTo(thisDoc, parent.location.href));
	//alert("nestingDepth is " + nestingDepth);
}

/*else if (!parent.navBarFrame.appletsLoaded) {  //trying to deal with reload race
//	alert("Applets not loaded");
	self.location.replace(nestingDepth + "wait_for_applets.htm?" + thisDoc);
}*/

//alert("My url is " + thisDoc);

window.onload = initialize;



// If any of these are null button disabled. Variables must be set on page after this script

function initialize(){
	isLoaded = true;
//	var ww = parent.navBarFrame;
	oneTimeScript.setTitle(document.title);
	oneTimeScript.atPage(thisDoc);
	parent.document.title = document.title;
	//writeHeader();
	setStyleSheet( (oneTimeScript.isLectureMode() ? "lecture" : "notes") );
	showNotes( oneTimeScript.doShowNotes());
	var myBackgroundColor;
	var myBackgroundImage;
	if (isIE) {
		myBackgroundColor = document.body.currentStyle.backgroundColor;
		myBackgroundImage = document.body.currentStyle.backgroundImage;
	} else {
		// document.defaultView.getComputedStyle instead of window.getComputedStyle is needed for Safari
		myBackgroundColor = document.defaultView.getComputedStyle(document.body, null).backgroundColor;
		myBackgroundImage = document.defaultView.getComputedStyle(document.body, null).backgroundImage;
	}
	parent.sliderFrame.setCamouflage(myBackgroundColor, myBackgroundImage);
//	alert("Finished loading " + thisDoc);
}
		

/*************************************************************************************/
/* Functions to load a document using the url2String applet which must be loaded into
the applets frame by the time these functions are called
*/

function fileToString(url){
//alert(url + " requested.");
	var url_contents = null;
	if (loadApplet == null) {
		loadApplet = getApplet("url2String");
	}
	if (loadApplet != null) {
		//alert("Found applet!");
		
		url_contents = loadApplet.read_relative_URL(url);
		//alert(url + " contains " + url_contents);
		//alert("here!");
   		if (!loadApplet.success())
       		alert( "Sorry couldn't read URL " + url );
	}
	else alert("Couldn't load applet at " + url);
	// url_contents is a JavaObject. Force it to a script String
	// This cures bug between Sun Java 1.5 and JScript
	return new String(url_contents.toString());	
}

/*	Puts up a window 
*/
function getApplet(appletName){
	if (noAppletFrame) return;
	
	if (!parent.navBarFrame.appletsLoaded) {
		var waitPage = peelName(getBaseURL()) + "wait_for_applets.htm";
		self.location.replace(nestingDepth + "wait_for_applets.htm");
//		var waitWindow = window.open(waitPage,null,"height=400,width=300");
/*		while(!waitWindow.closed) {
		}*/
	}
	if (parent.navBarFrame.appletsLoaded) {
	//	alert("Looking for applet " + appletName);
		var applet = parent.navBarFrame.document[appletName];
		//alert("Got applet " + applet);
		return applet;	// Changed 2004.02.17 to support both Mozilla & ie
	}
//	alert("Sorry. " + appletName + " is unavailable.");
	return null;
}

/*************************************************************************************/
/* Functions to write code dynamically into documents. A piece of sample code is extracted as a single string
directly from a cpp file. The code is assumed to be standard code EXCEPT that it may have been annotated for
teaching purposes. Annotations are embedded in c style comments so that they may be made by an instructor
without affecting the compilability of the code.

	The JAVA parser from the Teaching Machine has been ammended to provide HTML syntax colouring.

Annotations are handled by JavaScript functions.

 Configuration file support added 2001.12.31

*/
 
var TMLinkImage1 = -1;
var videoLinkImage1 = -1;
var guidedLinkImage1 = -1;
var currentCode = -1;
var baseCode = new Array();		// Holds code for TM
var config = new Array();		// holds config file names for examples
var exampleURL = new Array();     // Holds name of url to example file
var exampleSourceRoot = new Array() ; // Hold the URL for the root of the source tree for the example.
                                     // If this exampleSourceRoot[i] is not null, then exampleURL[i] is
									 // relative to the root.
var selection = new Array();		// holds selection strings for examples.


var TMLink = false;			// boolean-true if a TM link button wanted
var videoLink = false;		// boolean-true if a video link button wanted
var guidedLink = false;		// boolean-true if a guided link button wanted
var videoRef = null;
var guidedRef = null;

/* popups inside of code must be restored to html: added 01/11/01 */
var insidePopup = false;

/* set up linking buttons for
	(a) invoking TM on example
	(b) invoking a video
	(c) invoking a guided example
	TM, video and guided are boolean, specifying appearance or not of three buttons
	videoR and guidedR are url's for video and guided files
*/
/*var buttonsX = 0;
var buttonsY = 0;

function moveButtons(x,y){
	buttonsX = x;
	buttonsY = y;
}*/

// This could be redone as an object - in fact, it effectively is
function setButtons(TM, video, guided, videoR, guidedR){
//	if (noAppletFrame) return; // Most images loaded rel to parent which ain't there
	TMLink = TM;
	videoLink = video;
	guidedLink = guided;
   if (video) videoRef = videoR;
	if (guided) guidedRef = guidedR;
}

/* Theo's sourceRoot code added June 21, 2006 by mpbl
*/
var rootOfSourceFiles = null ;

// Set the root of the source files
function setSourceRoot( srcRoot ) {
	rootOfSourceFiles = getAbsoluteURL( srcRoot ) ;
}

function clearSourceRoot( ) {
	rootOfSourceFiles = null ;
}


/***************************************************************************************
	USER DEFINED BUTTONS
	Support for user defined buttons that will change when rolled over or pressed and can be greyed out. User must provide four gifs for each button, one for each of the states, as well as a unique reference no. and an action string.



Add a button. Button does not appear on the page. This routine simply loads the references into a button array.
Each button must (a) have had all its gifs loaded into the cache already (b) be given a unique reference no. by the user.
Permits separate gifs for normal appearance, appearance when rolled over by the mouse, appearance when pressed, and
appearance when greyed out.
use putButton to actual insert button onto page
*/

/***************************************************************************************
  new button routines
  ********************************************************************/
  
var stateString = new Array();
	stateString[0] = "Normal.gif";
	stateString[1] = "Over.gif";
	stateString[2] = "Down.gif";
	stateString[3] = "DownOver.gif";
	stateString[4] = "Gray.gif";
	stateString[5] = "DownGray.gif";


/**** Button Class *********************************************************************
*****/

// Button constructor
function Button(gifBase){
	this.ref = Button.nextRef++;
	this.gifBase = gifBase;
	this.isRollover = true;
	this.isToggle = false;
	this.grayable = false;
}

/* Class attributes *************************/
// The reference number of the next Button object to be put on this page
// alternatively, the number of Buttons currently on the page
Button.nextRef = 0;

// The set of all buttons on the current page
Button.buttons = new Array();	

/**** Button class methods ******************/
// Button factory method
Button.createButton =
function(gifBase, isRollover, isToggle, isGrayable, actionString, tooltip, downActionString, downtip){
	var button = new Button(gifBase);

	Button.buttons[button.ref] = button;		// Save it it buttons array
	
	button.isRollover = isRollover;
	button.isToggle = isToggle;
	button.isGrayable = isGrayable;
	if (isToggle) {
		button.toggleDown = false;
		if(downtip) button.downtip = downtip;
	}
	if (isGrayable) button.grayedOut = false;
	if (tooltip) button.tooltip = tooltip;

	document.write('<img src="',getToImages() + gifBase + 'Normal.gif', '" name="button',button.ref,'"'); 
	document.write(' onMouseOver="Button.get(', button.ref, ').changeState(1);return true;"');
	document.write(' onMouseDown="Button.get(', button.ref, ').changeState(2);return true;"');
	if(button.isToggle) {
		if (!downActionString) downActionString = "";
		document.write(' onMouseUp="if(Button.get(', button.ref, ').changeState(3)) if(Button.get(',
			button.ref, ').toggleDown) ', downActionString, '; else ',
				actionString, '; return true;"');
	} else {
		document.write(' onMouseUp="if(Button.get(', button.ref, ').changeState(3)) ', actionString, '; return true;"');
}
	document.write(' onMouseOut="Button.get(', button.ref, ').changeState(0);return true;"');
	document.write(' title="', tooltip, '">');
	
//	retrieveSource();
//alert ("Button.ref = " + Button.ref);
	return button;
}

// retrieve button by reference no.
Button.get = function(refNo){
	return Button.buttons[refNo];
}

/*** Button instance methods *************************/
Button.prototype.toString = function(){
	return "Button #" + this.ref + " (gifBase is " + this.gifBase + ")";
}

// GrayOut or un-grayOut a grayable button
Button.prototype.grayOut = function(gray){
//	alert("setting grayOut to " + gray);
	if (this.isGrayable) {
		this.grayedOut = gray;
		var stateStrNum;
		if (this.isToggle && this.toggleDown)
			stateStrNum = (gray ? 5 : 2);
		else
			stateStrNum = (gray ? 4 : 0);
		document.images["button" + this.ref].src = getToImages() + this.gifBase + stateString[stateStrNum];
	}
		
	else alert("Can't gray out non-grayable " + this.toString());
}

/* change button to reflect the indicated mouse state.
	0 = normal,  1= rollover,  2 = pressed,  3=greyed out
	button must have been put already and MUST be enabled.
*/
Button.prototype.changeState = function (mouseState){
	if (noAppletFrame) return false; // Most images loaded rel to parent which ain't there
//	alert(myButtons[refNo].toString());
	if(this.isGrayable && this.grayedOut) return false;
//	alert("changeButtonState(" + refNo + ", " + mouseState + ", " + tooltip + ")");

	var buttonImage = document.images["button" + this.ref];
	var gifBase = getToImages() + this.gifBase;
//alert("gifBase is " + gifBase);	
	switch (mouseState) {
		case 0: // mouse is out, normal unless button toggled down
			buttonImage.src = gifBase +
			( this.isToggle && this.toggleDown ? stateString[2] : stateString[0]) ;
			break;
			
		case 1: // over, if rollover button, over gif unless toggled down
			if (this.isRollover)  // rollover - is it up or down
				buttonImage.src = gifBase + 
						( this.isToggle && this.toggleDown ? stateString[3] : stateString[1]) ;
				break;
				
		case 2: // mouse down, button down
			buttonImage.src = gifBase + stateString[2];
			break;
			
		case 3:  // mouse up, button up unless it's a toggle
			//alert("up, up and away...");
			if (this.isToggle) {
				this.toggleDown = !this.toggleDown;
				if (this.toggleDown) {
					buttonImage.src = gifBase + stateString[2 + this.isRollover];
					buttonImage.title = this.downtip;
				} else {
					buttonImage.src = gifBase + stateString[0 + this.isRollover];
					buttonImage.title = this.tooltip;
				}
			}
			else buttonImage.src = gifBase + stateString[0 + this.isRollover];
			break;
	}
//	alert("The new src for " + buttonImage.name + " is " + buttonImage.src);
	return true;
}

/**** End of Button Class **************************************************************
*****/

//******************** END of Buttons **********************************************

/* Insert a code example into the document. It will be automatically marked for syntax colouring
 and annotations will be inserted as needed.
	relativeURL: the file holding the code
	buttonSet: boolean. if true, insert the buttonSet (which must be already defined)
	className: user defined class to allow appearance to be controlled by a cascading style sheet
	configurationFile: relative path to configuration file to be used with TM
	hideLetter: added 10.15.2002. let's file be invoked in multiple places with different
			parts of it being hidden
	tmSelection: 
*/

function insertCode(relativeURL, buttonSet, className, configurationFile, hideLetter, tmSelection){
	if(noAppletFrame) return;

	if (hideLetter != null) setSwitchLetter(hideLetter);
	else setSwitchLetter("");
//	alert("insertCode.");
	
// Theo's root switching, added in by mpbl June 21, 2006
	var theRoot ;
	if( rootOfSourceFiles == null ) {
		theRoot = peelName(location.href); }
	else {
		theRoot = rootOfSourceFiles ; }
		
	// Normalize to the absolute url
//	var theURL = getAbsoluteURL(relativeURL);
	var theURL = catenateURLs(theRoot, relativeURL);
//	 alert("InsertCode: absoluteURL is " + theURL + "\nwhile navBarFrame doc url is " + getBaseURL());
	// construct the differential url to the applet document

	theURL = oneTimeScript.getRelativeTo(oneTimeScript.getBaseURL(),theURL);
//	 alert("InsertCode: differentialURL is " + theURL);
	// Now use it to get the code, making sure url is in Java format
//    alert("InsertCode: JavaURL is " + getJavaURL(theURL));
	var javaURL = getJavaURL(theURL);
	rawCode = fileToString(javaURL);
	if (rawCode != null) {
		if (parseApplet == null) {
			parseApplet = getApplet("markUp");
		}
		if (parseApplet != null) {
//			alert("rawCode is " + rawCode);
		// In business! Is a TM link wanted?
			currentCode++;		// track no of examples on this page
// More of Theo's sourceRoot
			if( rootOfSourceFiles == null ) {
    			exampleURL[currentCode] = javaURL;
				exampleSourceRoot[currentCode] = null ; }
			else {
				exampleURL[currentCode] = relativeURL ;
				exampleSourceRoot[currentCode] = getJavaURL( rootOfSourceFiles ) ; }
//			exampleURL[currentCode] = javaURL;
			document.write('<div class="tmContainer">');
			document.write('<table class="tmBar" width = "100%"> <tr><td width="120px" align="left">');
			document.write('<img src="', getToImages() + "greenBoard.gif" + '"></td><td align="left">');
			if(buttonSet) {
				if ((TMLink||guidedLink) && (TMApplet == null))
					TMApplet = parent.navBarFrame.document.teachingMachine;
				if (TMLink){
					Button.createButton("runButton", true, false, false, "invokeTM(" + currentCode + ")", "Run " + theURL + " in the Teaching Machine");
					baseCode[currentCode] = getTMCode(rawCode);
					config[currentCode] = getToConfigurations() + configurationFile;
					selection[currentCode] = tmSelection;					
				}
				if (videoLink) {
					Button.createButton("videoButton", true, false, false, "invokeVideo('" + videoRef +"')" , "See a video of " + theURL + " being run in the Teaching Machine");
//					retrieveSource();
					
				}
				if (guidedLink) {
					//Button.createButton("guidedButton", true, false, false, "invokeguided(" + guidedRef +")"  , "Self-guided example in Teaching Machine with audio commentary");
					Button.createButton("editButton", true, false, false, "invokeEdit(" + currentCode +")"  , "Change the example temporarily");
				}
				
			}
			document.write('</td> <td align="right">', relativeURL,'</td></tr></table>');
			document.write('<div class="',className,'" style="position:relative;">');
		//Mark it as code and as preformatted
			document.write('<pre>');
		//	alert("RAW CODE\n" + rawCode);
		// have parser applet return HTML for syntax colouring
//		static public String markUpString(String str, String lang) Where lang should equal "C++" or "Java". It's been tested a little.
			try {
				var language = ((theURL.search(/.jav/i) == -1 ) ? "C++" : "Java");
				//alert("languge is " + language + "\nparseApplet is " + parseApplet.tostring());
				/* new String is required to convert Java String Object to JavaScript one
				   Or Safari won't apply native methods like match to it. */
				var stainedCode = new String(parseApplet.markUpString(rawCode, relativeURL));
		//document.write("STAINED CODE<pre>" + stainedCode + "</pre>------ END OF STAINED CODE -----------------");
			// Now write it dynamically, handling notations, then close off block
				writeAnnotated(stainedCode);
			} catch(e) {
				document.write('<p class="error">Error retrieving code\n' + e + "</p>");
			}
			finally {
				document.write('</pre></div></div>');
			}
		}
	else alert("Can't find markUp Applet");
	}
}


function getTMCode(rawCode){
//	alert("getTMCode(" + rawCode + ")");
	var splitUp = rawCode.split(/\/\*#/);
//	alert("0'th splitUp is " + splitUp[0]);
	for(i = 1; i < splitUp.length; i++){
		var end = splitUp[i].search(/\*\//);
		splitUp[i] = splitUp[i].substring(end+2);
//	alert(i + "'th splitUp is " + splitUp[i]);
	}
//	alert("length is " + splitUp.length);
	var processedCode = splitUp[0];
	for(i = 1; i < splitUp.length; i++)
		processedCode += splitUp[i];
//	alert("processedCode is " + processedCode);	
	return processedCode;  //splitUp.join("");
}

// Configuration added 2001.12.31
function invokeTM(example){
	if (TMApplet == null)
		TMApplet = getApplet("teachingMachine");
	if (TMApplet == null)
		alert("Sorry. Unable to locate the Teaching Machine Applet");
	else {
//	 alert(exampleURL[example]);
	// String language, String fileName, String programSource
		TMApplet.loadRemoteFile(exampleURL[example]);
		if (config[example] != null) {
	//		alert(config[example]);
			TMApplet.readRemoteConfiguration(config[example]);
		}
		if (selection[example] != null) {
	//		alert(selection[example]);
			TMApplet.setSelectionString(selection[example]);
		}
	}
}
/* Added Jan 15, 2002: Currently assumes video is located in site_resources. Would like to be able to detect whether it is local or not so it could be put locally or in site_resources. */
function invokeVideo(urlString){
	if (urlString != null) {
		//alert("Invoking video: " + getToVideos());
		var videoW = window.open(getAbsoluteURL(getToVideos() + urlString));
		videoW.focus();
	}
}

/***********************************************************************************
                             Edit mode
***********************************************************************************/

/* This is all new and experimental. Only works with one example at a time, C++ hard wired in */
var currentExample;
var formW;
function invokeEdit(example){
	currentExample = example;
	//open a new window with the form editor in it
	formW = window.open(getToWebWriter()+"inputForm.htm", "inputForm","");
	formW.focus();
}
// Called from input form window by its onload handler
function formLoaded(){
	formW.loadForm(baseCode[currentExample]);
}

// Called by input form window once editing is done
function changeCode(newCode){
	if (TMApplet == null)
		TMApplet = getApplet("teachingMachine");
	if (TMApplet == null)
		alert("Sorry. Unable to locate the Teaching Machine Applet");
	else {
//	 alert(exampleURL[example]);
	// String language, String fileName, String programSource
		TMApplet.loadString( '.cpp',newCode);
		if (config[currentExample] != null) {
	//		alert(config[example]);
			TMApplet.readRemoteConfiguration(config[currentExample]);
		}
		else TMApplet.readRemoteConfiguration(getToConfigurations()+ "default.cfg");
	}
}




/* Code is in annotated string. It already contains HTML for syntax colouring.
	Output it dynamically, cutting out the annotation marks and inserting the
	proper HTML/javaScript calls in their place.
*/

/* 10.15.2002: added hideCodeSwitch and setSwitchLetter function to allow
a single pice of code to contain multiple hide markings so that it can
be displayed differently according to which letter is passed in
*/
var hideCode = false;	// Write out code into document
var hideCodeSwitch = "";	// letter for hiding different pieces 

function setSwitchLetter(switchLetter){
	hideCodeSwitch = switchLetter;
}
			
function writeAnnotated(annotated){
	//document.writeln('<p class="error">Annotated is ',Object.prototype.toString.apply(annotated),'</p>');
	hideCode = false;
	//Look for an annotation mark (it will have been syntax stained)
	var regular = /<SPAN class="codeComment">\/\*#/;
	// Safari reports TypeError - Value undefined (result of expression annotation.match is not an object)
	var found=annotated.match(regular);
	while (found != null){
		var nextPiece = annotated.substring(0,found.index);
		if (insidePopup)	// restore to html: added 01/11/01 
			nextPiece = commentToHTML(nextPiece);
		if (!hideCode) writeCode(nextPiece);	// write everything before found
		// strip off what was written plus the annotation thus far
		annotated=annotated.substring(found.index+found[0].length); 
//		alert("Annotated:\n" + annotated);
		/* Write out the correct HTML/JavaScript for the particular annotation
		and return the code with the rest of this annotation stripped out*/
		annotated = writeAnnotation(annotated,false); 
		found=annotated.match(regular);
	}
	if (!hideCode)writeCode(annotated);	// last piece
}	

/* Slightly recursive function (one level only). The only difference between opening and closing marks is the "/" before trailing ones. So always start with closing false then
if the first character is a "/" strip it and recurse with closing set true.
Precondition: The first character in annotation is the command character.
Returns: annotation with the rest of this annotation stripped out
*/
var annotArg;

function writeAnnotation(annotation,closing){
	//alert("write " + (closing ? "closing" : "opening") + " annotation: " + annotation );
	switch (annotation.substr(0,1)){
		case "/":		// closing, strip leading '/'
			annotation = annotation.substring(1);
			return writeAnnotation(annotation,true);
		case "d":										// DefLink
			if (closing)
				endLink();
			else
				startDefLink(getAnnotationArg(annotation));
			break;
			
		case "p":										// PopLink
			if (closing)
				endLink();
			else
				startPopLink(getAnnotationArg(annotation));
			break;
			
		case "P":										//PopUp
					var popUpName = getAnnotationArg(annotation);
					annotation = eatArgument(annotation);
					var popUpHTML = commentToHTML(getAnnotationArg(annotation));
					//alert("insertPopUp(" + popUpName + ", " + getAnnotationArg(annotation) + ", false);");
					insertPopUp(popUpName, popUpHTML, false); 
				break;
			
		case "B":										//Block
			if (closing)
				endBlock();
			else
				startBlock(getAnnotationArg(annotation));
			break;
			
		case "b":										//BlockLink
			if (closing)
				endLink();
			else
				startBlockLink(getAnnotationArg(annotation));
			break;
		case "H":
/* 10.15.2002: Added switchLetter to allow single piece of code to be displayed
	multiple times with different pieces showing 
	11.08.2002: Added special hideSwitchCode "all" to allow full display
		of example regardless of display instructions.*/
			if (hideCodeSwitch !="all"){
				switchLetter = annotation.substr(1,1);
				if (switchLetter == '*' || switchLetter ==hideCodeSwitch)
					hideCode = !closing;
			} 
			break;
		case "h": // Highlighting, added 6.03.2004
			if (closing)
				document.write('</span>');
			else
				document.write('<span class="codeHighlight">');
			break;

		case "D":	// display (opposite of hide)
/* 10.15.2002: Added switchLetter to allow single piece of code to be displayed
	multiple times with different pieces showing */
			if (hideCodeSwitch !="all"){
				switchLetter = annotation.substr(1,1);
				if (switchLetter == '*' || switchLetter ==hideCodeSwitch)
					hideCode = closing;
			} 
			break;
		
		default:
			document.write("Annotation " + annotation.substr(0,1) + "unrecognized");
		}
	return eatRestOfComment(annotation);	
}
/* Strip the argument in quotes from the front of the annotation string */
function eatArgument(annotation){
	var found = annotation.search(/"/); // first quote
	var stripped = annotation.substring(found+1);
	found = stripped.search(/"/);
	stripped = stripped.substring(found+1);
//	alert("stripped: " + stripped);
	return stripped;
}

/* Strip away the front of the annotation string up to the next closing comment */
function eatRestOfComment(annotation){
	var found = annotation.search(/<\/SPAN>/);
	return annotation.substring(found+7);
}

// annotation argument will be the next piece of text found between quotes
function getAnnotationArg(annotation){
	var found = annotation.match(/"[^"]*"/)
	if (found == null) return "";
	return found[0].replace(/"/g,"");
}

// Added 2006.3.25 to write code between annotations with collapsing comments.
function writeCode(codePiece){
	// NOTE: because these are spans and not divs they surround evey line of a multiline comment
	var startComment = /<SPAN class="codeComment">\/\*/;
	var endComment = /\*\/<\/SPAN>/;
	var inside = false;
	// Safari reports TypeError - Value undefined (result of expression annotation.match is not an object)
	var found=codePiece.match(startComment);
	while (found != null){
		var nextPiece = codePiece.substring(0,found.index);
		writeUsingBreaks(nextPiece);	// write everything before found
		// strip off what was written plus the annotation thus far
		codePiece=codePiece.substring(found.index+found[0].length);
		if (!inside) {
			document.write('<div class="mlCommentContainer"><image class = "codeWidget" src="', getToImages() + 'contracter.gif', '" onclick="toggleExpand(this);">');
			document.write('<div class="multilineComment" style="display:block"><SPAN class="codeComment">/*');
			found=codePiece.match(endComment);
		} else {
			document.write('*/</SPAN></div></div>');
			found=codePiece.match(startComment);
		}
		inside = !inside;
	}
	writeUsingBreaks(codePiece);
}

function writeUsingBreaks(code){
	/*for(var i = 0; i < code.length; i++) {
		var char = code.charAt(i);
		if (char == "") document.write("&nbsp;");
		else if (char == "\n") document.write('<br>');
		else document.write(char);
	}*/
	var lines = code.split(/\n/);
	for (var l = 0; l < lines.length; l++){
		lines[l].replace(/ /,"\u00a0");
		document.write(lines[l]);
		if (l < lines.length-1)
			document.write('<br>');
	}
}

function toggleExpand(thisElement){
	//IE does not support negative substr positions - e.g. substr(-12)
	if (thisElement.src.substr(thisElement.src.length-12) == "expander.gif")
		thisElement.src = getToImages() + "contracter.gif";
	else
		thisElement.src = getToImages() + "expander.gif";
	
	var expandElement = moveToSubsidiaryList(thisElement);
	if (expandElement != null){
		if (expandElement.style.display == "none")
			expandElement.style.display = "block";
		else
			expandElement.style.display = "none";
	}
}

function moveToSubsidiaryList(thisElement){
	var expandElement = thisElement;
	while (expandElement != null && expandElement.nodeName != "DIV")
		expandElement = expandElement.nextSibling;
	//alert("moved through siblings to" + expandElement);
	return expandElement;
}


/* Added 01/11/01 *************************************************************
	nextPiece is supposed to be a piece of html embedded in comment markers.
	However it has been converted by syntax staining. Reconvert to pure html
	1. Strip the comment markers.
	2. Strip ALL syntax coloring span class stuff (repeated every line)
	3. restore "&lt;" & "&gt;" to '<' and '>'
*******************************************************************************/
function commentToHTML(nextPiece) {
	//alert("Strip comments out of:/n" + nextPiece);
	nextPiece = nextPiece.replace(/<SPAN class="codeComment">/g,"");
	nextPiece = nextPiece.replace(/<\/SPAN>/g,"");
	nextPiece = nextPiece.replace(/\/\*/g,"");
	nextPiece = nextPiece.replace(/\*\//g,"");
	nextPiece = nextPiece.replace(/&lt;/g,"<");
	nextPiece = nextPiece.replace(/&gt;/g,">");
	//alert("Comments strippped out:/n" + nextPiece);
	return nextPiece
}	

/**************************************************************************************/
/* POP-UPS: There are two categories of routines here.
	(a) Those which handle the showing and hiding of popUps in response to mouse rollovers of their associated links

	(b) Those which handle the dynamic writing of links & popups into the page.

*/


// Set up variables and event capturing to track the current cursor position.

document.onmousemove = markCursor;  // Tell browser what function handles mouse moves. Why do we handle every move?

var lastEvent;

function markCursor(e) {
  // If not IE save event and git. Don't want to slow Mozilla
	if (!isIE) lastEvent = e; 
  return true;
}

/* Postion the popUp & make it visible
*/
/* Rough size of link: can't measure in Mozilla although probably in ie */
var linkWidth = 75;
var linkHeight = 20;

function overPopUp(name,dx,dy) {
	//alert("name is " + name);
	var popUp = document.getElementById(name).style;	// Fetch the popUp
	if (popUp != null && !isPersistent(popUp)) {
	  	//alert(examine(popUp));
	  	//alert("popUp is @ (" + popUp.offsetLeft + ", " + popUp.offsetTop + ") and is " + popUp.offsetWidth + " x " + popUp.offsetHeight + " relative to " );
  
         // Establish current geometry
		var winWidth = 600;		// Init for non-compliant browsers?
		var winHeight = 400;
		var mouseX = 0;  // relative to WINDOW
		var mouseY = 0;
		var offsetX = 0;
		var offsetY = 0;
		if (isIE) {
			if (document.documentElement &&
					document.documentElement.clientWidth||document.documentElement.clientHeight) { // compliant mode
				winWidth = document.documentElement.clientWidth - 20;
				winHeight = document.documentElement.clientHeight - 20;
				offsetX = document.documentElement.scrollLeft;
				offsetY = document.documentElement.scrollTop;
			} else { // Quirky mode
				winWidth = document.body.offsetWidth - 20;
				winHeight = document.body.offsetHeight - 20;
				offsetX = document.body.scrollLeft;
				offsetY = document.body.scrollTop;
			}
       		mouseX = event.clientX;
        	mouseY = event.clientY;
		 } else {
			offsetX = window.pageXOffset;
			mouseX = lastEvent.pageX - offsetX;		
			offsetY = window.pageYOffset;
			mouseY = lastEvent.pageY - offsetY;
			winWidth = window.innerWidth - 16;
			winHeight = window.innerHeight - 16;
		 }
//		 alert("mouseX: " + mouseX + "\nmouseY: " + mouseY + "\noffsetX: " + offsetX + "\noffsetY: " + offsetY);
//		 alert("This window is " + winWidth + " x " + winHeight);
//		 alert("popUp.width is " + popUp.width + " and popUp.visibility is " + popUp.visibility);
// Compute popUp position from geometry		 
		 popUp.left = popPosition(mouseX, parseInt(popUp.width), linkWidth, winWidth, offsetX);
		 popUp.top = popPosition(mouseY, parseInt(popUp.height), linkHeight, winHeight, offsetY);
//		 alert("Placing popUp at (" + popUp.left + ", " + popUp.top + ")");
//		alert("popUp class is "	+ document.getElementById(name).getAttribute("className"));			 
		 popUp.zIndex = 1;
   	     popUp.visibility = "visible";
      }
}

/* One Dimesional positioning 
	The position of the popUp is set by setting its top left corner in document co-ordinates.
	Since computing the left component and the top component is an identical process this
	function just computes one dimension at a call.
	The dimension is deemed to run from 0 to winD-1
	up dimension means to the higher side of the dimension space (right if x, bottom if y)
	down dimension means to the lower side (left for x, top for y)
	mouseP: The mouse position in window
	popD: The extent of the popUp
	linkD: the extent of the link (may be hypothetical since it can't be measured)
	winD: the size of the window
	offset: the amount added to position in window to get position in document
*/
function popPosition(mouseP, popD, linkD, winS, offset) {
//	alert("popPosition(mouseP: " + mouseP + ", popD: " + popD + ", linkD: " + linkD + ", winS: " + winS + ", offset: " + offset + ")");
	var dim=20;
	var delta;
	 var channelD = mouseP - linkD;
	 var channelU = winS - mouseP - linkD;
	 if (channelD > channelU) {	// More room on lower side
		 if (popD > channelD)
			dim = 0;		// Too narrow, jam it left
		else {
			delta = (channelD-popD)/2;	// there's room, 
			dim = (delta > 20 ? channelD-20 : channelD - delta) - popD;
		}
	 } else {
		 if (popD > channelU)
			 dim = winS - popD;	// Jam it as far up dimension as possible
		 else {
			delta = (channelU-popD)/2;
			dim = mouseP + linkD + (delta > 20 ? 20 : delta);
		 }
	 }
	 dim += offset;
	 return dim + "px";
}

/* Make the popUp invisible unless it is modal */
function outPopUp(name) {

      var popUp = document.getElementById(name).style;

   // Hide the popUp if it is not persistent
      if (popUp != null && !isPersistent(popUp)) { 
     	 popUp.visibility = "hidden";
      } 
}

/* Modal popUps. click link to display, click to close */
function togglePopUp(name) {
	var popUp = document.getElementById(name).style;
	 if (popUp != null) {
		if (isPersistent(popUp)) popUp.persists = false;
		else popUp.persists = true;
		popUp.visibility = popUp.persists ? "visible" : "hidden";
	}
}

function isPersistent(thePopUp) {
   // If persistence has not yet been defined for this popup, make it false
   if (thePopUp.persists == null) thePopUp.persists = false;
   return thePopUp.persists;
}



/*********************************************************************************/
/* These are the category B pop-up routines. They handle the dynamic writing of popUps and popUp
links into the web page */




/*	Retrieves the HTML for a definition from the dictionary file. Definitions are
presumed to be stored as definition lists, as follows:
	<a name="internalLevel"></a>
	<div class="whatever">
		html to be displayed
	</div>
	
	the argument name refers to (and must be the same as) the name string in the anchor tag .
	The function retrieves the full definition as it appears above.

*/

function getDefinition(name) {
	// Get a handle to the named popUp using browser-specific code.
	// the dictionary is located at the top of the tree, as is applet file
	var definition = fileToString(getToContent() + "dictionary.htm");
	if (definition != null) {
		var start;
		var found = false;
	// For each <a> in the dictionary
		while((start=definition.search(/<a/))!=-1){ 
		// toss everything before the <a>
			definition=definition.substr(start);
		// find end of that <a>
			var end = definition.search(/>/);
		// see if the name matches
			var pattern = new RegExp('name="'+ name + '"');
			var def = definition.search(pattern);
			if (def < end ) found = true;
		// Find the end of the definition anchor and toss
			end=definition.search(/<\/a>/);
			definition = definition.substr(end+4);
			// The actual definition is in a division
			start = definition.search(/<div/);
			// move past it
			definition = definition.substr(start+4);
			start = definition.search(/>/) + 1;
			end = definition.search(/<\/div>/);
			if (found) return definition.substring(start,end);
			definition=definition.substr(end+6); // Move past last div for rest of dictionary
		} // while 
	}// if definition
					
	return "Sorry! Definition unavailable";
}


/* Uses dynamic HTML to insert a link to the popUp called name. link is the text displayed
in the html file. Alternate forms merely mark the beginning and end of a link without writing
the link iteself.
*/
function insertPopLink(name, link) {
	if (arguments.length >=4)
	   startPopLink(name,arguments[2], arguments[3]);
	else
		startPopLink(name);
 	document.write(link);
 	endLink();
}

function startPopLink(name){
	var dx = (arguments.length >=2) ? arguments[1] : -10;
	var dy = (arguments.length >=3) ? arguments[2] : -10;
	document.write('<a class="poplink" onMouseOver="overPopUp(\'', name, '\', ', dx, ', ', dy, ')" ');
	document.write('onMouseOut="outPopUp(\'' + name + '\')" ');
	document.write('href="javascript:togglePopUp(\'',name, '\')";>');
}

function endLink(){
  document.write('</a>');
}

/* Uses dynamic HTML to insert a popup called name into the html at the point of calling. Contents of the popup is html in htmlString. If a popup called name exists already the popup is not written. Again there are two forms. The whole thing can be written at once or markers dropped at the beginning and the end of a popup while the popup text is written independently
*/


var popUpList = new Array();
/* 2004.02.19: Total rewrite to get pop-ups working properly on both browser classes.

	inputs: name: A string specifiying the specific name of the pop-up, used to set its id
			htmlString: the html for the contents of the pop-up
			isDef: boolean, true for definitions, false for other pop-ups
			width, height: (optional) the width and height in pixels
	
	If the pop-up doesn't exist it is created as a new <def> node and hooked in to the beginning
	of the document.body tree.
	
*/

function insertPopUp(name, htmlString, isDef, width, height){
	var inList = false;
	if (arguments.length < 4) width = 300;
	if (arguments.length < 5) height = 180;
//	alert("Trying to popUp " + name + " with msg: " + htmlString);
	for(i = 0; i < popUpList.length; i++) {
		if (popUpList[i]/*.getAttribute("id")*/ == name) {
			inList = true;
			break;
		}
	}
	if (!inList) {
		var popNode = document.createElement("div");
//		popNode.setAttribute("className", (isDef ? "definition" : "popup"));
		popNode.className = (isDef ? "definition" : "popup");
//		popNode.setAttribute("id", name);
		popNode.id = name;
		popNode.style.width = width + "px";
		popNode.style.height = height + "px";
		popNode.style.visibility = "hidden";
		popNode.style.position = "absolute";
		popNode.style.zIndex = -1;
		popNode.innerHTML = htmlString;
//		alert("popUp created is " + popNode.innerHTML);
		document.body.insertBefore(popNode,document.body.firstChild);
//		document.write('<div class=', isDef ? '"definition"' : '"popup"', ' id="', name, '" style="width:', width, 'px; height:', height, 'px; visibility:hidden;">');
		popUpList[popUpList.length] = name; //popNode;
		return true;
	}
	return false;
}

var startAnnotation;
function startPopUp(name, annotation){
	alert("Starting popUp " + name + " at " + annotation);
	startAnnotation = annotation;
}

function endPopUp(name, annotation) {
	alert("Ending popUp " + name + " at " + annotation);
}
	


/*	Inserts a definition link pointing into the dictionary file. name is the name of the definition
as specified by the id in the dictionary file. link is what actually appears on the web page.

	Also inserts the definition popup itself. That is, the definition is dynamically written into
	the web page as a popUp. NOTE: I need code here to insure the def popup is not written twice).
*/
function insertDefLink(name,link) {
	insertPopUp("def" + name,  getDefinition(name), true);
	if (arguments.length >= 4)
		insertPopLink("def" + name, link, arguments[2], arguments[3]);
	else
		insertPopLink("def" + name, link);
}

/* This version just marks the beginning of a definition link. The link must be output and its end
  marked as separate operations. These methods are easier for automated methods such as the pedagogical
  filter to utilize */
function startDefLink(name){
	insertPopUp("def" + name, getDefinition(name), true);
	if (arguments.length >= 3)
		startPopLink("def" + name, arguments[1], arguments[2]);
	else
		startPopLink("def" + name);
}



/* Illuminated block support. Rollover the blockLink & the associated block is illuminated (by having it's background color
	changed. It ACTUALLY WORKED THE FIRST TIME!!!!! Works just like popups except the background color of a <span> is changed
	instead of the visibility of a <div>.
	
	2004.2.20 Extensive changes to facilitate Mozilla (definitely didn't work the first time!). */

function insertBlockLink(name, link) {
	startBlockLink(name);
	document.write(link);
	endLink();
}

function startBlockLink(name){
  document.write('<a class="blocklink" onMouseOver="turnBlocksOn(\'', name, '\')" ');
  document.write('onMouseOut="turnBlocksOff(\'' + name + '\')" ');
  document.write('href="javascript:toggleBlocks(\'',name, '\')";>');
}
/* June 15, 2006: Changed class name from codeblock */
function startBlock(name){
	document.write('<span class="codeblock" id="',getElementName(name),'">');
}

function endBlock(){
	document.write('</span>');
}
/** Hidden Answer Support **************************************************************
	Routines to hide answers until a button is clicked.
	answer is stored in a <div> of className hideAnswer
	which is contained in a <div> called hideAnswerContainer which also contains
	a button to show answer. Only the visibilty is changed, so that the
	answer is laid out in the normal control. I intend to make the container
	visible and visibly different (even though the answer is hidden)
***********************************************************************************/
function startHiddenAnswer(label){
	var myLabel = (label == null || label == "" ? "answer" : label);
	document.write('<div class="hideAnswerContainer">');
	document.write('<button style="float: right" type="button" onClick="toggleAnswer(this);" title="Show ', myLabel, '">', myLabel, '</button>');
	document.write('<div class="hideAnswer" style="visibility:hidden">');
	document.write('<div class="hideAnswerContainer">');
}

function endHiddenAnswer(){
	document.write('</div></div></div>');
}

// Assumes button immediately precedes hideAnswer div in hideAnswerContainer
function toggleAnswer(thisNode){
	// walk up to contining div
	var current = thisNode;
//	alert("at entrance current is now " + current);
	while (current.nodeName != "DIV" && current !=null)
		current = current.parentNode;
//	alert("after walking up to container current is now " + current);
	if (current!= null && current.className == "hideAnswerContainer" && current.firstChild != null){
		current = current.firstChild;
//	alert("first child of container is " + current);
		while (!(current.nodeName == "DIV" && current.className == "hideAnswer")){
			current = current.nextSibling;
			if (current == null) break;
		}
//	alert("after looking for answer current is " + current);
		if (current != null)
			if(current.style.visibility == null || current.style.visibility == "hidden")
				current.style.visibility = "visible";
			else
				current.style.visibility = "hidden";
	}
//	alert("current is now " + current);
}

//ffffcc
var saveBlockColor;
var showBlockColor = "#ff0000"; 
/* IE is not kosher since it lets multiple blocks have the same id and then  finds them all with document.all[id]
or even document.getElementsByName[id] which is peculiar but useful since very few elements are actually allowed the
name attribute. These two functions fake a proper getElementByName by using getElementById and then specializing
each use of name after the first one to create ids of the form names.
2006. Why not classes? This works, worry about it later.
*/

function myGetElementsByName(name) {
	var firstElement = document.getElementById(name);
	var elements = new Array(0);
	if (firstElement) {
		elements[0] = firstElement;
		if (firstElement.totalElements)
			for (var i = 1; i < firstElement.totalElements; i++)
				elements[i] = document.getElementById(name+i);
	}
	return elements;
}

function getElementName(name) {
	var theName = name;
	var firstElement = document.getElementById(name);
	if (firstElement) {
		if(!firstElement.totalElements)
			firstElement.totalElements = 2;
		else firstElement.totalElements++;
		theName = name + (firstElement.totalElements -1);
	}
	return theName;
}
	
	
function turnBlocksOn(name){
	var blocks = myGetElementsByName(name); //all(name);
//	alert("there are " + blocks.length + " blocks by the name of " + name);
	for(var i = 0; i < blocks.length; i++) {
		if(!isPersistent(blocks[i])) {	
//		alert("not persistent");
			if (blocks[i].saveBlockColor == null)
				blocks[i].saveBlockColor = isIE ? blocks[i].style.backgroundColor :
								document.defaultView.getComputedStyle(blocks[i],null).backgroundColor;
			blocks[i].style.backgroundColor = showBlockColor;
			//alert("blocks[" + i + "] is " + blocks[i] + " and its background color is " + blocks[i].style.backgroundColor);
		}
	}
}

function turnBlocksOff(name){
	var blocks = myGetElementsByName(name); //all(name);
	for(var i = 0; i < blocks.length; i++) 
		if (!isPersistent(blocks[i]))
			blocks[i].style.backgroundColor = blocks[i].saveBlockColor;
}

function toggleBlocks(name){
	var blocks = myGetElementsByName(name); //all(name);
	for(var i = 0; i < blocks.length; i++) {
     	if (isPersistent(blocks[i])) {
			blocks[i].persists = false;
      		blocks[i].style.backgroundColor = blocks[i].saveBlockColor;
		} else {
			blocks[i].persists = true;
      		blocks[i].style.backgroundColor = showBlockColor;
		}
	 }
}


/*****************************************************************************************************
		StyleSheet Handling 
*****************************************************************************************************/

function setStyleSheet(title){
	//alert("Trying to change stylesheets to " + title);
	var links = document.getElementsByTagName("link");
	var i;
	var l;
	for(i = 0; i < links.length; i++) {
		l = links[i];
		if ( l.getAttribute("rel") && l.getAttribute("rel").indexOf("style") != -1 && l.getAttribute("title") )  {//is a titled stylesheet, either preferred or alternate
				l.disabled =  true;
				//alert("link " + l + " is " + (l.disabled ? "true" : "false"));
		}
	}
	for(i = 0; i < links.length; i++) {
		l = links[i];
		if ( l.getAttribute("rel") && l.getAttribute("rel").indexOf("style") != -1 && l.getAttribute("title") )  {//is a titled stylesheet, either preferred or alternate
				if(l.getAttribute("title") == title) l.disabled = false;
				//alert("link " + l + " is " + (l.disabled ? "true" : "false"));
		}
	}
}

/* hiddenNotes are a special division that can be displayed or not. For example, one might not want to display
them in a lecture but might want them in a study version */

function showNotes(show){
//alert("showNotes(" + (display ? "true" : "false") + ")");

	var blocks = myGetElementsByName("hiddenNote"); 
	for(var i = 0; i < blocks.length; i++) {
//		alert("blocks[" + i + "] is " + blocks[i]);
		blocks[i].style.display = (show ? "block" : "none");
	}
}
function startNote(){
	document.write('<div class="hiddenNotes" id="',getElementName("hiddenNote"),'">');
}

function endNote(){
	document.write('</div>');
}

/***********************************************************************************
                             Print support
***********************************************************************************/
var printW;
/* window opens from default.htm url (i.e. base). This document has a url down the
tree, so all it internal refs in document body are wrt this document. When DOM is
cloned into printform printForm must be at same location as this doc, which means
every folder that has documents to be printed has to have a printForm in that folder.
*/
function invokePrint(){
//	alert(getBaseToHere());
	
	printW = window.open(peelName(getBaseToHere()) + "printForm.htm", "printForm","");
	printW.focus();
}

// Called from input form window by its onload handler
function printFormLoaded(){
/*	var examples = new Array();  // Trying to add full examples at end 
	var k = 0;
	for(var i = 0; i < exampleURL.length; i++){
		var skip = false;
		for (var j = 0; j < i; j++)
			if (exampleURL[i] == exampleURL[j]) skip = true;
		if (!skip) {
			var rawCode = fileToString(exampleURL[i]);
			examples[k++] = new String(parseApplet.markUpString(rawCode, exampleURL[i]));
		}
	} */
	var theBody;
//	if (isIE){
		theBody = '<h1 id="theTitle">' + document.title + '</h1>' + document.body.innerHTML;
		printW.loadForm(theBody);
/*	}
	else {
		theBody = document.createElement("div");
		var theTitle = document.createElement("h1");
		theTitle.setAttribute("id", "theTitle");
		theTitle.nodeValue = document.title;
		theBody.appendChild(theTitle);
		theBody.appendChild(document.body.cloneNode(true));
		printW.loadForm(theBody);
	}*/
}


/***********************************************************************************
                              Shade support
***********************************************************************************/


function showShade(show){
	var shadeNode = document.getElementById("windowShade");
	if (show) {
//alert("here");
		if (shadeNode == null) {
			shadeNode = document.createElement("div");
			shadeNode.className = "windowShade";
			shadeNode.id = "windowShade";
			shadeNode.style.position = "absolute";
			shadeNode.style.height = 0 + "px";
			document.body.insertBefore(shadeNode,document.body.firstChild);
		}
		shadeNode.style.visibility = "visible";
		shadeNode.style.zIndex = 100;	// Over Top of it all baby!
	} else if (shadeNode != null) {
		shadeNode.style.visibility = "hidden";
		shadeNode.style.zIndex = -1;
	}
}


function setShade(mouseY, height){
	var shadeNode = document.getElementById("windowShade");
	if (shadeNode == null) return;
	if (isIE) {
		if (document.documentElement &&
				document.documentElement.clientWidth||document.documentElement.clientHeight) { // compliant mode
			offsetY = document.documentElement.scrollTop;
			docHeight = document.body.clientHeight;
		} else { // Quirky mode
			offsetY = document.body.scrollTop;
//			docHeight = document.body.clientHeight - 20;  don't know
		}
	 } else {
		offsetY = window.pageYOffset;
		docHeight = document.height;
	 }
	var theTop = mouseY + offsetY;   // top of the document, potentially off the screen
	shadeNode.style.top = theTop + "px";
	shadeNode.style.height = (docHeight - theTop) + "px"; 
}


/************************** Menuing ************************************

		These routines are not central to WebWriter as menuing facilities
		are provided by programs like DreamWeaver. They should probably be
		put into a secondary script file. Getting them to work with Mozilla
		is not a priority (although preliminary tests indicate they already
		do work with it pretty well). 2001.12.21
		
	***********************************************************************/



// Code to write out a standard navigation menu inside a sidebar
// The set of links are arranged in a table that fills the frame
// NOTE: initial version only supports one column


/* CALLING CONVENTION
	writeNavTable(tableHeading,targetFrame,{linkURL, linkLabel}, ....)

	e.g.   writeNavTable("Tutorials","tutorFrame","default.htm", "home", "links/default.htm","links")

*/



var bullet;
var bulletOn;

function writeNavTable(heading,target) {
	if (arguments.length > 2){
//	alert("In navTable");
		document.writeln('<br><table>');
		document.writeln('<tr height="20"> <td><H1>', heading, '</H1><hr></td></tr>');
		document.writeln('<tr><td><TABLE>');

		createBullets();	// name changed 2004.03.20	
		// Write out the link elements (3 args per link)
			for(arg = 2; arg < arguments.length; arg+=3) {
				var refNo = (arg-2)/3;

/* I've got a name here as I was trying to use it to access the table entry and change
 its background. This does not seem to be possible BUT the name doesn't appear to hurt anything
 so I'm leaving it in as a hedge. */
				document.writeln('<tr><td name="tableEntry',refNo,'">');

	// Set initial bullet
				document.writeln('<img src="', getToImages(), 'bullet.gif"');
	//  and give it a unique name - bullet0, bullet1, etc. 
				document.writeln('name="bullet',refNo,'"');
				document.writeln('>');
	/* I think if I put a span in here with a name or id I couldilluminate behind the name */
	// Write out the reference for the content frame
	//			document.writeln('<span id="menu', itemNo, '"
				document.writeln('<a href="',arguments[arg],'" target="',target,'"');

/* This line took forever to debug! The problem here is, we are actually writing code
	We want to generate an onMouseOver call as in the following example
		onMouseOver="return imageOver(4,'prompt line for 4')"

		Notice the quotes around the prompt string which is a constant at this point. I missed
		them because I'm actually using an argument and of course my argument is a string so
		everything is consistent. Wrong!!! I'm writing code, not passing through a function call.
		The quotes must be put in and they must be single quotes, because I need double around the
		whole function call.!!!!! So what I must write dynamically is:

			onMouseOver="return imageOver(arg/3,'arguments[arg+2]')" 

		which requires buckets of arguments and lots of quote switching!!!*/
				document.writeln('onMouseOver="return imageOver(',"'bullet", refNo, "', '", arguments[arg+2], "'",')" ');

				document.writeln('onMouseOut="imageOff(',"'bullet", refNo,"'", ')"');
				document.writeln('>');
		
		// The visible part of the link
				document.writeln(arguments[arg+1],'</a><hr></td></tr>');
			}
		document.writeln('</table></td></tr></table>');
	}
}

function switchMenu(menuPage, target){
	parent.frames[target].location = menuPage;
}
/* I've gotten so efficient, using so few gifs, this is probably not necessary.
   1.11.2001: Repaired to get gifs properly wherever page is located. */

function createBullets(){
		bullet = new Image();
		bullet.src = getToImages() + "bullet.gif";
		bulletOn = new Image();
		bulletOn.src = getToImages() + "bullet_lit.gif";
}

function imageOver(imageName, prompt){
	if (document.images) {
		document[imageName].src=bulletOn.src;
//		alert(examine(document[imageName]));
		status = prompt;
		return true;
	}
	return false;
}

function imageOff(imageName) {
	if (document.images) {
		document[imageName].src = bullet.src;
		status="";
	}
}


/****************** Utilities ***************************************/

/****************************************************************************
	functions for manipulating urls, mostly to generate correct url's from current
	page back to known resources or to work out relative url between two points on
	the site tree
	****************************************************************************/


/*  Returns the relative url between the fromURL & the toURL. The precondition is that
	from and to are in the same tree
*/

function getRelativeTo(fromURL, toURL){
	return oneTimeScript.getRelativeTo(fromURL, toURL);
}

/* Peel the file name off the end of a url leaving everything up to the last '/'
	If there is no '/', returns an empty string.
*/
function peelName(url){
	var end = url.lastIndexOf("/");
	// remove the document name, leaving just the path
	return url.substring(0,end+1);
}

/* Peel the last directory layer off the end of a url. Precondition: fileName already
	peeled, that is, url ends in a '/'.
	If there is no other '/', returns an empty string.
*/
function peelBack(url){
	var end = url.lastIndexOf("/", url.length-2);
	// remove the directory layer, leaving rest of the path
	return url.substring(0,end+1);
}


/*get url for the base of the tree which is where the applet window is located.
 top, top.contents and top.navBarFrame are unreliable because  may include concatenated bookmark url with embedded ?
 in cases where site entered bypassing gateway and then the frames were autoloaded.
*/
function getBaseURL(){
	return oneTimeScript.getBaseURL(); //stripSearch(top.dictionaryFrame.location.href);
}

function getToImages(){
	return nestingDepth + oneTimeScript.getToImages();
}

/* nestingDepth not used here because configurations are relative to TM applet not
 this document. */
function getToConfigurations(){
	return oneTimeScript.getToConfigurations();
}

function getToContent(){
	return oneTimeScript.getToContent();
}

function getToWebWriter(){
	return nestingDepth + oneTimeScript.getToWebWriter();
}

function getToVideos(){
	return nestingDepth + oneTimeScript.getToVideos();
}

function getBaseToHere(){
	return getRelativeTo(getBaseURL(),document.location.href);
}

/* Construct an absolute url from the given relative url & the absolute location
	of the page which calls the function. Relative url does not have to be a descendant
	of the calling node.
*/


/* Theo split this into two. mpbl added in June 21, 2006 */

function getAbsoluteURL( relativeURL ){
	//alert("Inside getAbsoluteURL looking for  " + relativeURL + " relative to " + location.href);
	var path=peelName(location.href);
	//alert("path is: " + path);
	return catenateURLs( path, relativeURL ) ;
}

function catenateURLs( path, relativeURL ) {
	var backUp=/\.\.\//;
	while (relativeURL.match(backUp) != null){
		relativeURL=relativeURL.replace(backUp,"");
		path = peelBack(path);
	}
	return path + relativeURL;
}


// deal with javaScript/Java url differences
function getJavaURL(javaScriptURL){
	// Use real spaces
	var javaURL=javaScriptURL.replace(/%20/g," ");
	// flip '\' to '/'
	javaURL=javaURL.replace(/\/\/\//,"/");
	return javaURL;
}




/* A STAMP FOR THE BOTTOM OF THE PAGE **************************************************/

var daysOfTheWeek = new Array("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
	"Friday", "Saturday");
var months = new Array("January", "February", "March", "April", "May", "June",
	"July", "August", "September", "October", "November", "December"); 


function bottomStamp(isCopyright, isDate) {
	if (isCopyright || isDate) {
		document.write('<div class="bottomStamp">');
		if (isDate && (Date.parse(document.lastModified) != 0)){
			var mod = new Date(document.lastModified);
			document.write('<div class="dateStamp">This page last updated on ');
			document.write(daysOfTheWeek[mod.getDay()] + ", " + months[mod.getMonth()]);
			document.write(" " + mod.getDate() + ", " + mod.getFullYear());
			document.writeln('</div>');
		}
		if (isCopyright)
			document.write('<div class="copyright">'  + copyright + '</div>');
		document.write('</div>');
	}
}

/** USEFUL FUNCTIONS FOR DEBUGGING ************************/

function examine(object){
	var s = "";
	var items=0;

	for(prop in object) {
		s += prop + "   value: " + object[prop] + "\t";
		if (items%3==0) s+="\n";
		if(items++ > 150) break;
	}
	return s;
}


/* Call this function after the </body> tag and you will see the dynamically
created web page. (Must fit in an alert window). 
2004.02.17: First Mozilla win. Works in both.*/

function retrieveSource(){
	alert(document.body.innerHTML);
}

