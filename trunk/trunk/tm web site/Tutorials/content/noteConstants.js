// JavaScript Document

var siteTitle = "TM Tutorials";
var authors = "Michael Bruce-Lockhart & Theodore S. Norvell";
var generator = "DreamWeaver MX2004";
var bottomMark ="Teaching Machine: <em>Tutorials</em>";
var copyright = "";
var courseNo = "1020";

var copyrightTitle = siteTitle;
var copyrightOwner = authors;
var authorRef = "http://www.TheTeachingMachine.org";
var licenseRef = authorRef;

var tmArchive = "tm.jar, qg.jar, tmImagePlugIn.jar";

//  SITE IDENTIFICATION 
// Define either the organization and the course or the siteLogo and the siteTooltip
// If the siteLogo is defined (it should appear in the contentFolder) it will be used in
// the right hand of the webWriter navigation bar. The tooltip will be displayed when
// the mouse hovers over the logo. The logo should be no bigger than 120px wide by 50px high.
// If the logo is "", then the logo will be replaced by two lines of text. The top
// line will contain the organization string while the second will contain the course
// string
var organization = "MUN";
var course = "TM Tutorials";
var siteLogo = "";
var siteTooltip = "";

/****************************************************
 toolBar button control
 Turn to false if button should not be shown at all
 startWithShade only applies if startInLectureMode is true
*****************************************************/

var homeButton = true;
var homeTooltip = "return to TheTeachingMachine.org home page";
var homeUrl = "http://www.TheTeachingMachine.org";


var modeButton = false;
var shadeButton = false;
var printButton = true;
var helpButton = false;
var configButton = false;

var startInLectureMode = false;
var startWithShade = false;

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

defaultConfigurationFile = "default.tmcfg";
configFilesArray = new Array();

