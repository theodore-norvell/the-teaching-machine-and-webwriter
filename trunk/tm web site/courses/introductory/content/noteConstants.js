// JavaScript Document

// These constants are used by the header scripts to dynamically write standard tags in the <head> section
// of site pages.

var siteTitle = "An Introduction to Programming";
var authors = "Michael Bruce-Lockhart";
var generator = "DreamWeaver CS3";
var bottomMark ="<b>P1:</b> <em>An Introduction to Programming</em>";
var copyright = "&copy; 2006, 2007, 2008, 2010 Michael Bruce-Lockhart.";
var courseNo = "P1";

var copyrightTitle = siteTitle;
var copyrightOwner = authors;
var authorRef = "http://www.TheTeachingMachine.org";
var licenseRef = authorRef;
var copyright = copyrightOwner;
var copyRightDates = "2002, 2004, 2008, 2010";
var creativeCommons = true;

var tmArchive = "tm.jar, qg.jar, tmImagePlugIn.jar";

/* This is incorrect but let here for the moment (objective is to share webWriter files)

although we haven't tested it, we recommend this be located in the same filesystem as the sites that use it.

If there's only one course or manual set it up like the skeletal webWriter manual
courseRoot 
          -content
		  -style
		  -webWriter
		  root files 
Multiple courses would be better to do the following:

courses
      - course A
	              -content
				  -style
				  root files
	  - course B
	              -content
				  -style
				  root files
	  - course C
	              -content
				  -style
				  root files
	  -webWriter
	  
	  So that all courses use the same copy of webWrter
	  For the first case, wwLocation can be ""
	  
*/
//var wwLocation = "../../";

//  SITE IDENTIFICATION 
// Define either the organization and the course or the siteLogo and the siteTooltip
// If the siteLogo is defined (it should appear in the contentFolder) it will be used in
// the right hand of the webWriter navigation bar. The tooltip will be displayed when
// the mouse hovers over the logo. The logo should be no bigger than 120px wide by 50px high.
// If the logo is "", then the logo will be replaced by two lines of text. The top
// line will contain the organization string while the second will contain the course
// string
var organization = "Memorial University";
var course = "P1 Reference Notes";
var siteLogo = "";
var siteTooltip = "Procedural programming in C++ ";

// Desired buttons and states associated with buttons
var homeButton = true;
var homeTooltip = "Go to TheTeachingMachine.org home page";
var homeUrl = "http://www.TheTeachingMachine.org/";

var modeButton = true;
var startInLectureMode = true;

var printButton = true;

var shadeButton = true;
var startWithShade = true;

var helpButton = false;

var configButton = true;

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


