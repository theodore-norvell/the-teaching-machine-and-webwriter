// JavaScript Document

// These constants are used by the header scripts to dynamically write standard tags in the <head> section
// of site pages.

var siteTitle = "Higraph TM CookBook";
var authors = "Michael Bruce-Lockhart";
var generator = "DreamWeaver CS3";
var bottomMark ="PDV CookBook";
var copyright = "&copy; 1461 " + authors +".";
var courseNo = "";

var copyrightTitle = siteTitle;
var copyrightOwner = authors;
var authorRef = "http://www.teachingMachine.org";
var licenseRef = authorRef;
var copyright = copyrightOwner;
var copyRightDates = "14610";
var creativeCommons = true;

var tmArchive = "tm.jar, qg.jar, tmImagePlugIn.jar";


//  SITE IDENTIFICATION 
// Define either the organization and the course or the siteLogo and the siteTooltip
// If the siteLogo is defined (it should appear in the contentFolder) it will be used in
// the right hand of the webWriter navigation bar. The tooltip will be displayed when
// the mouse hovers over the logo. The logo should be no bigger than 120px wide by 50px high.
// If the logo is "", then the logo will be replaced by two lines of text. The top
// line will contain the organization string while the second will contain the course
// string
var organization = "Memorial University";
var course = "";
var siteLogo = "";
var siteTooltip = "How to use the Teaching Machine to create Program Dependent Visualizations for Algorithms and Data Structures";

// Desired buttons and states associated with buttons
var homeButton = true;
var homeTooltip = "Go to the course administrative website";
var homeUrl = "http://www.teachingMachine.org/cookBook/default.html";

var modeButton = false;
var startInLectureMode = false;

var printButton = true;

var shadeButton = true;
var startWithShade = true;

var helpButton = false;

var configButton = false;

/******************************************************
	Configuration file index
	
******************************************************/

defaultConfigurationFile = "default.tmcfg";
configFilesArray = new Array();
configFilesArray[0] = "default.tmcfg";
configFilesArray[1] = "full800x600.tmcfg";
configFilesArray[2] = "full1024x768.tmcfg";
configFilesArray[3] = "full1280x1024.tmcfg";
configFilesArray[4] = "localGlobal800x600.tmcfg";
configFilesArray[5] = "localGlobal1024x768.tmcfg";
configFilesArray[6] = "localGlobal1280x1024.tmcfg";
configFilesArray[7] = "engr1020IncoCentre.tmcfg";
configFilesArray[8] = "engr1020AngusBruneau.tmcfg";
configFilesArray[9] = "engr1020AngusBruneauArrayBar.tmcfg";


/***********************************************************
	although these are in the form of variables they
	SHOULD NOT BE CHANGED BY AUTHORS as webWriter has
	to have some of these folders hard coded into it.
******************************************************/
var contentFolder = "content/"
var styleFolder = "style/";
var wwFolder = "webWriter/";
var imagesFolder = wwFolder + "images/";
var configurationsFolder = styleFolder + "configurations/";
/***********************************************************
    end of non-editable content
***********************************************************/

var videosFolder = contentFolder + "videos/";


