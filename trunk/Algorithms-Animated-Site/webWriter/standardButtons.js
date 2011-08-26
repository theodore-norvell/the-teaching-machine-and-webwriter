// JavaScript Document

/******** dependencies *************************************************************
buttonClass.js must have been included already

In order to start in lecture mode the var startInLectureMode must be true. If it
does not exist due to prior definition it will be created and set to false here.
In addition, homeTooltip can be overridden by previous definition to a less
generic message

************************************************************************************/
if(startInLectureMode == null) startInLectureMode = false;
if(homeTooltip == null) homeTooltip ="return to the site home page";


/****************************************************
Predefined button definitions
This script does create buttons but simply defines standard
buttons. Buttons defined are

homeButton - returns user from a webWriter framework site to a homesite
             in which the framework is embedded - typically a course site
			 
modeButton - switches between lecture mode (stylesheet for projection, shade button)
             and notes mode (stylesheet for personal display, no shade)
			 
shadeButton - turns on the shade which can be pulled up to cover part of the screen
               rather like a blank sheet of paper was used on old transparency projectors
			  
printButton - prepares online document for printing by changing stylesheets as well as
              printing anything in the hidden print container (provided to allow complete
			  program examples to be printed at the end of the topic)
			  
helpButton  - site help, when we finally create it!

*****************************************************/

var homeButtonDef = new ButtonDef("homeButton");
homeButtonDef.actionString = "goHome()";
homeButtonDef.tooltip = homeTooltip;
homeButtonDef.actionString = "goHome()";

var modeButtonDef = new ButtonDef("modeButton");
if (startInLectureMode) {
	modeButtonDef.gifBase = "noteButton";
	modeButtonDef.toggleBase = "lectureButton";
	modeButtonDef.tooltip = "change to notes mode";
	modeButtonDef.toggletip = "change to lecture mode";
} else {
	modeButtonDef.gifBase = "lectureButton";
	modeButtonDef.toggleBase = "noteButton";
	modeButtonDef.tooltip = "change to lecture mode";
	modeButtonDef.toggletip = "change to notes mode";
}
modeButtonDef.actionString = "changeModes()";

var printButtonDef = new ButtonDef("printButton");
printButtonDef.actionString = "invokePrint()";
printButtonDef.tooltip = "generate printing version of this topic";

var shadeButtonDef = new ButtonDef("shadeButton");
shadeButtonDef.tooltip = "turn on the lecturing shade";
shadeButtonDef.actionString = "toggleShade()";

var helpButtonDef = new ButtonDef("helpButton");
helpButtonDef.actionString = "getHelp()";
helpButtonDef.tooltip = "Get help on the website";

var configButtonDef = new ButtonDef("configButton");
configButtonDef.actionString = "showConfigPicker(true)";
configButtonDef.tooltip = "set sitewide default TM configuration file";
