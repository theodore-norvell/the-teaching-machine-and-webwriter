// <script LANGUAGE="JavaScript">
/******************************************************************************
 JavaScript code by
   	Michael Bruce-Lockhart
	Copyright 2000, 2001, 2004, 2007
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
								
	July, 2007: Refactored to support the quizWriter add-on.

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

var debugging = false;   // set to false to turn off logging

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


function initialize(){
	isLoaded = true;
//	var ww = parent.navBarFrame;
	oneTimeScript.setTitle(document.title);
	oneTimeScript.atPage(thisDoc);
	parent.document.title = document.title;
	setStyleSheet( (oneTimeScript.isLectureMode() ? "lecture" : "notes") );
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
//alert(url + " requested. Window at " + window.location + " and document at " + document.URL);
	var url_contents = null;
	if (loadApplet == null) {
		loadApplet = getApplet("url2String");
	}
	if (loadApplet != null) {
//		consoleDebug("Found applet! " + loadApplet);
		
		url_contents = loadApplet.read_relative_URL(url);
//		consoleDebug(url + " contains " + url_contents);
		//alert("here!");
   		if (!loadApplet.success())
       		consoleError( "Sorry couldn't read URL " + url );
	}
	else consoleError("Couldn't load applet at " + url);
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
//		alert("Looking for applet " + appletName);
		var applet = parent.navBarFrame.document[appletName];
//		alert("Got applet " + applet);
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
var selection = new Array();		// holds selection strings for examples.
var dataFileSet = new Array();   // Holds references to any data file arrays


var TMLink = false;			// boolean-true if a TM link button wanted
var videoLink = false;		// boolean-true if a video link button wanted
var editLink = false;		// boolean-true if a edit link button wanted
var videoRef = null;
var editRef = null;			// probably not needed - hangover from guidedLink which was not being used

/* popups inside of code must be restored to html: added 01/11/01 */
var insidePopup = false;


// This could be redone as an object - in fact, it effectively is
function setButtons(TM, video, edit, videoR, editR){
//	if (noAppletFrame) return; // Most images loaded rel to parent which ain't there
	TMLink = TM;
	videoLink = video;
	editLink = edit;
   if (video) videoRef = videoR;
	if (edit) editRef = editR;
}

/* Definitions required to set up parsing of tmand ww selection strings for
following insertCode function
*/
var allString = "ALL";
var defaultString = "~S&~L";

/* Define operator functions */
function opOr(left, right){
	return left || right;
}

function opAnd(left, right){
	return left && right;
}

function opNot(left){
	return !left;
}
/* Also need an evaluateLeaf function
 Here, any token which is found in the set of current tags is true
 */
function evaluateLeaf(token, set){
	return set.contains(token);
}

/* Table of Op objects - see parser.js */
myTable = new Array();
myTable[0] = new Op("&", "\u2022", 2, opAnd, Op.LEFT);
myTable[1] = new Op(".", "\u2022", 2, opAnd, Op.LEFT);
myTable[2] = new Op("|", "+", 1, opOr, Op.LEFT);
myTable[3] = new Op("+", "+", 1, opOr, Op.LEFT);
myTable[4] = new Op("!", "~", 3, opNot, Op.UNARY);
myTable[5] = new Op("~", "~", 3, opNot, Op.UNARY);

/* parser.js ObTable object */
var opTable = new OpTable(myTable);

/* set up Script and S as well as Lib and L as equivalents */
var equivalents = new Array();
equivalents[0] = new Alias("Script", "S");
equivalents[1] = new Alias("Lib", "L");

/* Create a new parser based on these parameters */

var parser = new Parser(opTable, evaluateLeaf, equivalents);

var lastDataSet = null;

function createDataFileSet(/* any number of file names */){
	if (arguments.length == 0) lastDataSet = null;
	else {
		lastDataSet = new Array();
		for (a = 0; a < arguments.length; a++)
			lastDataSet[a] = arguments[a];
	}
}


/* Insert a code example into the document. It will be automatically marked for syntax colouring
 and annotations will be inserted as needed.
	relativeURL: the file holding the code
	buttonSet: boolean. if true, insert the buttonSet (which must be already defined)
	className: user defined class to allow appearance to be controlled by a cascading style sheet
	configurationFile: relative path to configuration file to be used with TM
	wwSelection: added 10.15.2002. let's file be invoked in multiple places with different
			parts of it being hidden
	tmSelection: 
*/


function insertCode(relativeURL, buttonSet, className, configurationFile, wwSelection, tmSelection){
	if(noAppletFrame) return;
	if(!wwSelection || wwSelection == "" || wwSelection.toUpperCase() == "DEFAULT") wwSelection = defaultString;
	if(!tmSelection || tmSelection == "" || tmSelection.toUpperCase() == "DEFAULT") tmSelection = defaultString;
	parser.setParseString(tmSelection.toUpperCase());
	var parseTree = parser.eParser();
	var tmParseString = parseTree.toString();
	
	/* Current system. Delete these lines to switch once TM is ready ***************/
	tmParseString = tmSelection;  
	/* Current system. Delete these lines to switch once TM is ready ***************/
	
	
	parser.setParseString(wwSelection.toUpperCase());
	parseTree = parser.eParser();
	/* This is for backward compatability with the old H and D switches which are now deprecated. */
	{
		var stringRep = parseTree.toString();
		if (stringRep.length == 1 || stringRep == allString)
			setSwitchLetter(stringRep);
	}
	/********* end of backward compatability ***/

	
//	alert("insertCode.");
	// Normalize to the absolute url
	var theURL = getAbsoluteURL(relativeURL);
//	 alert("InsertCode: absoluteURL is " + theURL + "\nwhile navBarFrame doc url is " + getBaseURL());
	// construct the differential url to the applet document

	theURL = oneTimeScript.getRelativeTo(oneTimeScript.getBaseURL(),theURL);
//	 alert("InsertCode: differentialURL is " + theURL);
	// Now use it to get the code, making sure url is in Java format
//    alert("InsertCode: JavaURL is " + getJavaURL(theURL));
	rawCode = loadCode(theURL, configurationFile, tmParseString);
//	var javaURL = getJavaURL(theURL);
//	rawCode = fileToString(javaURL);
	if (rawCode != null) {
		if (parseApplet == null) {
			parseApplet = getApplet("markUp");
		}
		if (parseApplet != null) {
//			alert("rawCode is " + rawCode);
		// In business! Is a TM link wanted?
//			currentCode++;		// track no of examples on this page
//			exampleURL[currentCode] = javaURL;
			document.write('<div class="tmContainer">');
			document.write('<table class="tmBar" width = "100%"> <tr><td width="120px" align="left">');
			document.write('<img src="', getToImages() + "greenBoard.gif" + '"></td><td align="left">');
			if(buttonSet) {
				document.write('<div id="buttonContainer', currentCode, '"></div>');
				var buttonSetArray = new Array();
				var setButtons = 0;

				if ((TMLink||editLink) && (TMApplet == null))
					TMApplet = parent.navBarFrame.document.teachingMachine;
				if (TMLink){
					var linkButtonDef = new ButtonDef("runButton" + currentCode);
					linkButtonDef.gifBase = "runButton";
					linkButtonDef.actionString = "invokeTM(" + currentCode + ")";
					linkButtonDef.tooltip = "Run " + theURL + " in the Teaching Machine";
					buttonSetArray[setButtons++] = linkButtonDef;
				}
				if (videoLink) {
					var videoButtonDef = new ButtonDef("videoButton" + currentCode);
					videoButtonDef.gifBase = "videoButton";
					videoButtonDef.actionString = "invokeVideo('" + videoRef +"')" ;
					videoButtonDef.tooltip = "See a video of " + theURL + " being run in the Teaching Machine";
					buttonSetArray[setButtons++] = videoButtonDef;
				}
				if (editLink) {
					var editButtonDef = new ButtonDef("editButton" + currentCode);
					editButtonDef.gifBase = "editButton";
					editButtonDef.actionString = "invokeEdit(" + currentCode +")" ;
					editButtonDef.tooltip = "Change the example temporarily";
					buttonSetArray[setButtons++] = editButtonDef;
				}
				if (buttonSetArray.length > 0) {
					var TMButtons = new ButtonSet("TMButtons" + currentCode, buttonSetArray, true);
//					TMButtons.append(document.getElementById("buttonContainer" + currentCode));
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
				writeAnnotated(stainedCode, parseTree);
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

/* Hoisted out of invokeCode in June, 2007, to allow code sharing with new runCode routine.
	This function fetches the raw code for the new example. It also loads the TM version of
	of the code (plus associated information) to the array of examples for this page and
	updates the page global variable currentCode
*/
function loadCode(theURL, configurationFile, tmParseString){
	var javaURL = getJavaURL(theURL);
	rawCode = fileToString(javaURL);
	currentCode++;		// track no of examples on this page
	exampleURL[currentCode] = javaURL;
	baseCode[currentCode] = getTMCode(rawCode);
	dataFileSet[currentCode] = lastDataSet;
// Updated 2007.02.02 to use new configuration filetype, ignoring any type specified in the call
	if (configurationFile && configurationFile!= "")
		config[currentCode] = getToConfigurations() + configurationFile;
	else
		config[currentCode] = "";
	if (tmParseString)
		selection[currentCode] = tmParseString;
	else
		selection[currentCode] = null;
	return rawCode;
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
		if(dataFileSet[example] != null) {
			var fileSet = dataFileSet[example];
			for (var i = 0; i < fileSet.length; i++) {
				var fileLoc = peelName(getBaseToHere()) + fileSet[i];
//				alert ("fileLoc is " + fileLoc);
				TMApplet.registerRemoteDataFile( fileLoc );
			}
		}
//	 alert(exampleURL[example]);
	// String language, String fileName, String programSource
		TMApplet.loadRemoteFile(exampleURL[example]);
		
// Changed March, 2008, to use sitewide default config file if none was specified originally
		var useConfigFile = (config[example] == "" ? getDefaultConfigFile() : config[example]);
		useConfigFile = peelFileType(useConfigFile) + getConfigSuffix() + ".tmcfg"
		alert(useConfigFile);
		TMApplet.readRemoteConfiguration(useConfigFile);
			
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
//			alert(config[example]);
			TMApplet.readRemoteConfiguration(config[currentExample]);
		}
		else TMApplet.readRemoteConfiguration(oneTimeScript.getDefaultConfig());
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
var hideCodeSwitch = "";	// letter for hiding different pieces, supports deprecated H and D 

function setSwitchLetter(switchLetter){
	hideCodeSwitch = switchLetter;
}

			
function writeAnnotated(annotated, parseTree){
	//document.writeln('<p class="error">Annotated is ',Object.prototype.toString.apply(annotated),'</p>');
	
	//document.write("parseTree = " + parseTree);
	var tagSet = new Set();
//	tagSet.add(allString); // All is implicitly added to everything to allow for untagged code
	//Look for an annotation mark (it will have been syntax stained)
	hideCode = !parseTree.evaluate(tagSet);
	//document.write("hideCode initially " + (hideCode ? "true\n" : "false\n"));
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
		annotated = writeAnnotation(annotated, parseTree, tagSet, false); 
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

function writeAnnotation(annotation, tree, tagSet, closing){
	//alert("write " + (closing ? "closing" : "opening") + " annotation: " + annotation );
	switch (annotation.substr(0,1)){
		case "/":		// closing, strip leading '/'
			annotation = annotation.substring(1);
			return writeAnnotation(annotation, tree, tagSet, true);
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
			
		case "I":
			tagSet.add("S");
			if (tree.evaluate(tagSet)){ // Write the code inside the comment
				var e = annotation.search(/\*\/<\/SPAN>/);
				writeCode(annotation.substring(1,e));
			}
			tagSet.remove("S");				
			hideCode = false;
		    break; //Just eat the rest of the comment which eats the code embedded inside it as well
			
		case "T":
			var tag = annotation.substring(1);  // Strip T
			var p = tag.search(/\S/); // Find first non-whitespace
			tag = tag.substring(p);	// and strip it away
			p = tag.search(/[\s\*]/); // Find trailing whitespace or start of comment, whichever is first
			tag = tag.substring(0, p).toUpperCase(); // extract tag & normalize
			if (tag == "SCRIPT") tag = "S";		// SCRIPT is equivalent to S
			else if (tag == "LIB") tag = "L";       // LIB is equivalent to L
			if (closing)
				tagSet.remove(tag);
			else
				tagSet.add(tag);
			//document.write("tagSet: " + tagSet +"\n");
			//document.write("tree: " + tree + "\n");
			hideCode = !tree.evaluate(tagSet);
			break;
			
		case "H":
/* 10.15.2002: Added switchLetter to allow single piece of code to be displayed
	multiple times with different pieces showing 
	11.08.2002: Added special hideSwitchCode "all" to allow full display
		of example regardless of display instructions.*/
			if (hideCodeSwitch != allString){
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
			if (hideCodeSwitch != allString){
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
	var found = annotation.search(/\*\/<\/SPAN>/);
	return annotation.substring(found+9);
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
			document.write('<div class="multilineComment" style="display:inline"><SPAN class="codeComment">/*');
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
			expandElement.style.display = "inline";
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



/* Postion the popUp & make it visible
*/
/* Rough size of link: can't measure in Mozilla although probably in ie */
var linkWidth = 75;
var linkHeight = 20;

function overPopUp(e, name,dx,dy) {
	//alert("name is " + name + " and event is " + (event ? event.type : "null"));
	var popUp = document.getElementById(name).style;	// Fetch the popUp
	if (popUp != null && !isPersistent(popUp)) {
	  	//alert(examine(popUp));
	  	//alert("popUp is @ (" + popUp.offsetLeft + ", " + popUp.offsetTop + ") and is " + popUp.offsetWidth + " x " + popUp.offsetHeight + " relative to " );
  
         // Establish current geometry taking into account different browser definitions for window size and scroll position
		var winWidth = windowWidth();		// With thanks to softcomplex.com
		var winHeight = windowHeight();
		var offsetX = scrollLeft();
		var offsetY = scrollTop();
		if (isIE) {
       		mouseX = window.event.clientX;
        	mouseY = window.event.clientY;
		 } else {  
			mouseX = e.pageX - offsetX;		
			mouseY = e.pageY - offsetY;
		 }
/*		 alert("mouse is at ( " + mouseX + ",  " + mouseY + ") and scroll at (" + offsetX + ",  " + offsetY + ")" +
		 "\nThis window is " + winWidth + " x " + winHeight +
		 "\npopUp.width is " + popUp.width + " and popUp.visibility is " + popUp.visibility);
*/
// Compute popUp position from geometry		 
		 popUp.left = popPosition(mouseX, parseInt(popUp.width), linkWidth, winWidth, offsetX);
		 popUp.top = popPosition(mouseY, parseInt(popUp.height), linkHeight, winHeight, offsetY);
//		 alert("Placing popUp at (" + popUp.left + ", " + popUp.top + ")");
//		alert("popUp class is "	+ document.getElementById(name).getAttribute("className"));			 
		 popUp.zIndex = 1;
   	     popUp.visibility = "visible";
      }
}
/***************************************************************************************************
	These functions were taken (with minor adaptations) from an excellent page on browser differences
	in handling window size and scrollbar positions: -
	
	http://www.softcomplex.com/docs/get_window_size_and_scrollbar_position.html
	
	Instead of using browser detection, it uses all three sets of possible variables, discarding
	any that are undefined and taking the smallest of what's left. See referenced page for more
	details
***************************************************************************************************/
	
function windowWidth() {
	return filterResults (
		window.innerWidth ? window.innerWidth : 0,
		document.documentElement ? document.documentElement.clientWidth : 0,
		document.body ? document.body.clientWidth : 0
	);
}
function windowHeight() {
	return filterResults (
		window.innerHeight ? window.innerHeight : 0,
		document.documentElement ? document.documentElement.clientHeight : 0,
		document.body ? document.body.clientHeight : 0
	);
}
function scrollLeft() {
	return filterResults (
		window.pageXOffset ? window.pageXOffset : 0,
		document.documentElement ? document.documentElement.scrollLeft : 0,
		document.body ? document.body.scrollLeft : 0
	);
}
function scrollTop() {
	return filterResults (
		window.pageYOffset ? window.pageYOffset : 0,
		document.documentElement ? document.documentElement.scrollTop : 0,
		document.body ? document.body.scrollTop : 0
	);
}
function filterResults(n_win, n_docel, n_body) {
	var n_result = n_win ? n_win : 0;
	if (n_docel && (!n_result || (n_result > n_docel)))
		n_result = n_docel;
	return n_body && (!n_result || (n_result > n_body)) ? n_body : n_result;
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
	document.write('<a class="poplink" onMouseOver="overPopUp(event,\'', name, '\', ', dx, ', ', dy, ')" ');
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

function toggleNote(thisNode){
//	alert("thisNode = " + thisNode);
	var noteDiv = thisNode.nextSibling;
	if (noteDiv != null && noteDiv.nodeName == "DIV"  && noteDiv.className == "hiddenNote"){
		if(noteDiv.style.display=="none")
			noteDiv.style.display = "block";
		else
			noteDiv.style.display = "none";
	}
}

var noteRef = 0;

function startNote(){
//	document.write('<hr>');
	noteRef++;
	document.write('<div class="HNContainer" id="HNContainer', noteRef,'">');
	
	var noteButtonDef = new ButtonDef("note" + noteRef);
	noteButtonDef.gifBase = "extraNote";
	noteButtonDef.actionString = "toggleNote(this)";
	noteButtonDef.tooltip = "turn on hidden note";
	noteButtonDef.toggleBase = "extraNoteToggle";
	noteButtonDef.toggleString = "toggleNote(this)";
	noteButtonDef.toggletip = "turn off hidden note";
	Button.createButton(noteButtonDef);
	document.write('<div class="hiddenNote" id="hiddenNote', noteRef,'" style="display:none;">');
}

// Notes won't nest
function endNote(){
	document.write('</div></div>');
/*
var noteButtonDef = new ButtonDef("note" + noteRef);
	noteButtonDef.gifBase = "extraNote";
	noteButtonDef.actionString = "toggleNote(this)";
	noteButtonDef.tooltip = "turn on hidden note";
	noteButtonDef.toggleBase = "extraNoteToggle";
	noteButtonDef.toggleString = "toggleNote(this)";
	noteButtonDef.toggletip = "turn off hidden note";
	buttonNode = document.getElementById("HNContainer" + noteRef);
	if(buttonNode){
		//alert("buttonNode: " + buttonNode);
		var myButton = Button.createButton(noteButtonDef);
		myButton.attach(buttonNode);
	}
***************************************************/
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


/*
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
*/
/* I've got a name here as I was trying to use it to access the table entry and change
 its background. This does not seem to be possible BUT the name doesn't appear to hurt anything
 so I'm leaving it in as a hedge. */
/*				document.writeln('<tr><td name="tableEntry',refNo,'">');

	// Set initial bullet
				document.writeln('<img src="', getToImages(), 'bullet.gif"');
	//  and give it a unique name - bullet0, bullet1, etc. 
				document.writeln('name="bullet',refNo,'"');
				document.writeln('>'); */
	/* I think if I put a span in here with a name or id I couldilluminate behind the name */
	// Write out the reference for the content frame
	//			document.writeln('<span id="menu', itemNo, '"
/*				document.writeln('<a href="',arguments[arg],'" target="',target,'"');*/

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
/*				document.writeln('onMouseOver="return imageOver(',"'bullet", refNo, "', '", arguments[arg+2], "'",')" ');

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
}*/
/* I've gotten so efficient, using so few gifs, this is probably not necessary.
   1.11.2001: Repaired to get gifs properly wherever page is located. */

/*function createBullets(){
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
}*/


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

/* Peel the file type off the end of a url leaving everything up to the last '.'
	If there is no '.', returns an empty string.
*/
function peelFileType(fileName){
	var end = fileName.lastIndexOf(".");
	// remove the document type, including the "." - won't handle multiple dot
	if (end > -1) fileName = fileName.substring(0,end);
	return fileName;
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

/* Added March 2008. The idea now is that if no config file is specified there would be
a default one which is specified in the constants file. This was added along
with support to change the default file so different ones could be used in different rooms
*/
function getDefaultConfigFile(){
	return oneTimeScript.getDefaultConfig();
}

function getConfigSuffix(){
	return oneTimeScript.getConfigSuffix();
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


function getAbsoluteURL(relativeURL){
	//alert("Inside getAbsoluteURL looking for  " + relativeURL + " relative to " + location.href);
	var path=peelName(location.href);
	//alert("path is: " + path);
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

/*** Console Logging **************************************************
These functions are intended to work with the Firebug extension. They will use alerts in other browsers
or if Firebug is not present in Firefox. Note testing for console being undefined that works
robustly across browsers (that is with IE!)
*/

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
		if (isCopyright) {
			document.write('<div class="copyright" style="position:relative;width:60%">');
			document.write('<div style="position:absolute; top:0; left:0;">');
			document.write('<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/2.5/ca/">');
			document.write('<img alt="Creative Commons License" style="border-width:0;" src="http://i.creativecommons.org/l/by-nc-sa/2.5/ca/88x31.png" />');
			document.write('</a></div>');
			document.write('<div style="position:absolute; top:0; left:100px;">');
			document.write('<span xmlns:dc="http://purl.org/dc/elements/1.1/" href="http://purl.org/dc/dcmitype/Text" property="dc:title" rel="dc:type">');
			document.write(copyrightTitle + '</span> by');
			document.write('<a xmlns:cc="http://creativecommons.org/ns#" href=' + authorRef + '" property="cc:attributionName" rel="cc:attributionURL">');
			document.write(copyrightOwner + '</a> is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/2.5/ca/">');
			document.write('Creative Commons Attribution-Noncommercial-Share Alike 2.5 Canada License</a>. ');
			document.write('Permissions beyond the scope of this license may be available at '); 
			document.write('<a xmlns:cc="http://creativecommons.org/ns#" href="' + licenseRef + '" rel="cc:morePermissions">' + licenseRef + '</a>.');
			document.write('</div></div>');
		}
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

