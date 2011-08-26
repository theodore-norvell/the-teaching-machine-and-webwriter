// JavaScript Document

/***************************************************************************************
	USER DEFINED BUTTONS
	Support for user defined buttons that will change when rolled over or pressed and can be greyed out.
	User must provide four gifs for each button, one for each of the states, as well as a
	unique reference no. and an action string.

Add a button. Button does not appear on the page. This routine simply loads the references into a button array.
Each button must (a) have had all its gifs loaded into the cache already (b) be given a unique reference no. by the user.
Permits separate gifs for normal appearance, appearance when rolled over by the mouse, appearance when pressed, and
appearance when greyed out.
use putButton to actual insert button onto page
*/

// Button definition constructor
function ButtonDef(id){
	this.id = id;					// button id - unique within the document 
	this.gifBase = id;				// base name for gifs, defaults to id
	this.gifPath = "";				// optional relative path, default images folder if not specified
	this.actionString = "";			// routine to be called when button clicked, required
	this.tooltip = "";				// button tooltip, recommended field
	
	this.toggleBase = "";			// These are alternate fields for toggle buttons
	this.toggleString = "";			// which specify parameters when toggle button
	this.toggletip = "";			// is down
}

/***************************************************************************************
  new button routines
  ********************************************************************/
  
var stateString = new Array();
	stateString[0] = "Normal.gif";
	stateString[1] = "Over.gif";
	stateString[2] = "Down.gif";
	stateString[3] = "Gray.gif";
/*	stateString[4] = "Toggle.gif";
	stateString[5] = "DownGray.gif"; */


/**** Button Class *****************************************************************/

// Button constructor
function Button(buttonDef){
	this.buttonDef = buttonDef;
	this.ref = Button.nextRef++;  // local button cache reference for retrieving buttons
	this.isRollover = true;
	this.isToggle = false;
	this.isGrayable = false;
}

/* Class attributes *************************/
/* The reference number of the next Button object to be put on this page
 alternatively, the number of Buttons currently on the page
 Note that while button is an object its visble manifestation on the
 page is not the button object but rather an image element.
 Thus each button has an image element which is an object in the DOM tree.
 The button itself is NOT in the DOM tree.
 Thus we keep the buttons separateley in an array (until I can find a way
 to attach them directly to the DOM tree) */
 
Button.nextRef = 0;

// The set of all buttons on the current page
Button.buttons = new Array();	

/**** Button class methods ******************/
/* Button factory method - creates a button with a unique id (within the document) at the
	specified node.
*/
Button.createButton =
function(buttonDef) {
	var button = Button.getById(buttonDef.id);
	if (button == null) {
		button = new Button(buttonDef);
		Button.buttons[button.ref] = button;		// Save it it buttons array
	}
	if (button.buttonDef.toggleBase) {
		button.isToggle = true;
		button.toggleDown = false;
	}
	document.write('<img id="', buttonDef.id, '" src = "', getToImages() + buttonDef.gifBase, 'Normal.gif"');
	document.write('onMouseOver ="Button.getByRef(', button.ref, ').changeState(1);return true;"');
	document.write('onMouseDown ="Button.getByRef(', button.ref, ').changeState(2);return true;"');
	if(button.isToggle) {
		if (button.buttonDef.toggleString=="") button.buttonDef.toggleString = buttonDef.actionString;
		document.write('onMouseUp ="if(Button.getByRef(', button.ref, ').changeState(3)) if(Button.getByRef(', button.ref,').toggleDown)');
		document.write(button.buttonDef.toggleString, '; else ', button.buttonDef.actionString, '; return true;"');
	} else {
		document.write('onMouseUp =  "if(Button.getByRef(', button.ref, ').changeState(3))', buttonDef.actionString, '; return true;"');
	}
	document.write('onMouseOut = "Button.getByRef(', button.ref, ').changeState(0);return true;" ');
	document.write('title = "', buttonDef.tooltip, '"');
	document.write('display = "inline" ');	// Allow button to be turned off
	document.write('class = "button">');	// make all buttons same class for CSS
	
/* Here is the modern approach of creating elements and attaching them to nodes but mousehandlers won't fire in IE
   Hard to know why. May have to do with who owns event when element is inserted this way. */
/*	button.image = document.createElement("img");
	button.image.setAttribute("id", buttonDef.id);
	button.image.setAttribute("src",getToImages() + buttonDef.gifBase + 'Normal.gif');
	button.image.setAttribute("onMouseOver", "Button.getByRef(" + button.ref + ").changeState(1);return true;");
	button.image.setAttribute("onMouseDown", "Button.getByRef(" + button.ref + ").changeState(2);return true;");
	if(button.isToggle) {
		if (button.buttonDef.toggleString=="") button.buttonDef.toggleString = buttonDef.actionString;
		button.image.setAttribute("onMouseUp", "if(Button.getByRef(" + button.ref + ").changeState(3)) if(Button.getByRef(" +
										button.ref + ").toggleDown)" + button.buttonDef.toggleString + "; else " + button.buttonDef.actionString + "; return true;");
//	alert("toggle button" + button);
	} else {
		button.image.setAttribute("onMouseUp", "if(Button.getByRef(" + button.ref + ").changeState(3))" + buttonDef.actionString + "; return true;");
	}
	button.image.setAttribute("onMouseOut", "Button.getByRef(" + button.ref + ").changeState(0);return true;");
	button.image.setAttribute("title", buttonDef.tooltip);
	button.image.setAttribute("display", "inline");	// Allow button to be turned off
	button.image.setAttribute("class", "button");	// Allow button to be turned off
END OF element creation/insertion code */
	return button;
}


// retrieve button by reference no.
Button.getByRef = function(ref){
	return Button.buttons[ref];
}

// retrieve button by img element id.
Button.getById = function(id){
	for(var i = 0; i < Button.nextRef; i++) {
		var button = Button.buttons[i];
		if (button.buttonDef.id == id) return button;
	}
	return null;
}

/*** Button instance methods *************************/

/****attach ******************************************************************
* attach the button image directly to the specified node (it will
*  be the first child at the time of attachment)
*
* param: buttonNode    the node to which the button's image is to be attached
Button.prototype.attach = function(buttonNode){
	buttonNode.insertBefore(this.image, buttonNode.firstChild);	
}
***** Not USED for document.write approach ***********************************/

/**** append ******************************************************************
* append the button image to the specified node (it will
*  be the last child at the time of appending)
*
* param: buttonNode    the node to which the button's image is to be appended
Button.prototype.append = function(buttonNode){
	buttonNode.appendChild(this.image);	
}
***** Not USED for document.write approach ***********************************/

Button.prototype.getGifBase = function(){
	return getToImages() + ( (this.isToggle && this.toggleDown) ? this.buttonDef.toggleBase : this.buttonDef.gifBase);
}

// GrayOut or un-grayOut a grayable button
Button.prototype.grayOut = function(gray){
//	alert("setting grayOut to " + gray);
	if (this.isGrayable) {
		this.grayedOut = gray;
		document.images["button" + this.ref].src = this.getGifBase() + stateString[3];
	}
		
	else alert("Can't gray out non-grayable " + this.toString());
}

Button.prototype.show = function(showIt){
	var myImage = document.getElementById(this.buttonDef.id);
	myImage.style.display = (showIt ? "inline" : "none");
}


/* change button to reflect the indicated mouse state.
	0 = normal,  1= rollover,  2 = pressed,  3=greyed out
	button must have been put already and MUST be enabled.
*/
Button.prototype.changeState = function (mouseState){
//	if (noAppletFrame) return false; // Most images loaded rel to parent which ain't there
	if(this.isGrayable && this.grayedOut) return false;
//	alert("changeButtonState(" + refNo + ", " + mouseState + ", " + tooltip + ")");

	var buttonImage = document.images[this.buttonDef.id];
//alert("gifBase is " + gifBase);	
	switch (mouseState) {
		case 0: // mouse is out, normal unless button toggled down
			buttonImage.src = this.getGifBase() + stateString[0] ;
			break;
			
		case 1: // over, if rollover button, over gif unless toggled down
			if (this.isRollover)  // rollover - is it up or down
				buttonImage.src = this.getGifBase() + stateString[1] ;
				break;
				
		case 2: // mouse down, button down
			buttonImage.src = this.getGifBase() + stateString[2];
			break;
			
		case 3:  // mouse up, button up unless it's a toggle
			//alert("up, up and away...");
			if (this.isToggle) {
				this.toggleDown = !this.toggleDown;
				buttonImage.title = ( this.toggleDown ? this.buttonDef.toggletip : this.buttonDef.tooltip);
			}
			buttonImage.src = this.getGifBase() + stateString[0 + this.isRollover];
			break;
	}
//	alert("The new src for " + buttonImage.name + " is " + buttonImage.src);
	return true;
}

Button.prototype.toString = function(){
	var myString = "Button #" + this.ref + "{\n";
	myString += "    buttonDef = {\n";
	myString += "        id: " + this.buttonDef.id + "\n";
	myString += "        gifBase: " + this.buttonDef.gifBase + "\n";
	myString += "        gifPath: " + this.buttonDef.gifPath + "\n";
	myString += "        actionString: " + this.buttonDef.actionString + "\n";
	myString += "        tooltip: " + this.buttonDef.tooltip + "\n";
	myString += "        toggleBase: " + this.buttonDef.toggleBase + "\n";
	myString += "        toggleString: " + this.buttonDef.toggleString + "\n";
	myString += "        toggletip: " + this.buttonDef.toggletip + "\n    }\n";
	myString += "    image: " + this.buttonDef.image + "\n";
	myString += "    isRollover: " + this.isRollover +"\n";
	myString += "    isToggle: " + this.isToggle +"\n";
	myString += "    isGrayable: " + this.isGrayable +"\n}";
	return  myString;
}


/**** End of Button Class *****************************************************/

/**** ButtonSet Class *****************************************************************/

/*** ButtonSet constructor ******************************************
* Constructs a set of buttons allowing their spacing to be controlled
* readily in a stylesheet
* @params: setId    the unique id for this set
*          arrayOfButtonss an array containing the Button objects which
*  					    make up the buttonSet. Their position in the array
*                       controls their order of appearance
*          horizontal  true for horizontal buttons, false for vertical
*
* implementation note. Tables don't work in IE
function ButtonSet(setId, arrayOfButtons, horizontal){
	this.buttonSet = document.createElement("div");
	this.buttonSet.setAttribute("id", setId);
	this.buttonSet.setAttribute("class", "buttonSet");
*** ELEMENT CREATE & ATTACH ***************************************************/
	
/* Can't control horizontal/vertical in either browser. Did it beautifully with tables
  but buttons didn't render at all in IE. Think it had something to do with
  layout algorithms. I'm not specifiying widths or heights but letting them be fit
  to the button gif size. Somehow, I think, in IE table cells were not getting
  set correctly. If button was placed outside a buttonSet it displayed fine in IE.
  Mind you, setting img height and width artificially didn't help.
  */
/*** ELEMENT CREATE & ATTACH ***************************************************
	this.buttonSet.setAttribute("display", (horizontal ? "compact" : "block") );
	
	for(var i = 0; i < arrayOfButtons.length; i++) {
//		var buttonInSet = document.createElement("div");
//		buttonInSet.setAttribute("class", "buttonPlace"); 
		this.buttonSet.appendChild(arrayOfButtons[i].image);
	}
}

ButtonSet.prototype.append = function(arrayNode){
//	alert("ButtonArray.this is " + this);
	arrayNode.appendChild(this.buttonSet);	
}
*** ELEMENT CREATE & ATTACH ***************************************************/

/*** DOCUMENT.WRITE ALTERNATIVE*************************************************/

/*** ButtonSet constructor ******************************************
* Constructs a set of buttons allowing their spacing to be controlled
* readily in a stylesheet
* @params: setId    the unique id for this set
*          arrayOfButtonss an array containing the Button objects which
*  					    make up the buttonSet. Their position in the array
*                       controls their order of appearance
*          horizontal  true for horizontal buttons, false for vertical
*
* implementation note. Tables don't work in IE
**********************************************************************/
function ButtonSet(setId, arrayOfDefs, horizontal){
	this.id = setId;
	this.arrayOfDefs = arrayOfDefs;
	document.write('<div id = "', setId, '" class = "buttonSet">');
	for(var i = 0; i < arrayOfDefs.length; i++)
		Button.createButton(arrayOfDefs[i]);
	document.write('</div>');
}


/**** End of ButtonSet Class *****************************************************/


//******************** END of Buttons **********************************************