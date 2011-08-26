// JavaScript Document

if (!nestingDepth) var nestingDepth = "";

writeHeader();

function writeHeader(){
	document.write('<meta NAME="Author" CONTENT="', authors, '">');
	document.write('<meta NAME="Generator" CONTENT="', generator, '">');
	document.write('<meta NAME="copyright" CONTENT="', copyright, '">');
	document.write('<meta NAME="resource-type" CONTENT="document">');
	document.write('<meta NAME="distribution" CONTENT="global">');
	document.write('<link rel="stylesheet" type="text/css" media="screen" href="');
		document.write(nestingDepth, styleFolder, 'navBar.css" title="webWriter navigation bar stylesheet">');
	document.write('<link rel="stylesheet" type="text/css" media="print" href="');
		document.write(nestingDepth, styleFolder, 'navBarprint.css" title="webWriter navigation bar Print stylesheet">');
}


