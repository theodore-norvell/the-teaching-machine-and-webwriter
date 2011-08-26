// JavaScript Document
//Most pages to be printed are two levels down
if (!nestingDepth) var nestingDepth = "../../";

writeHeader();

function writeHeader(){
	document.write('<meta NAME="Author" CONTENT="', authors, '">');
	document.write('<meta NAME="Generator" CONTENT="', generator, '">');
	document.write('<meta NAME="copyright" CONTENT="', copyright, '">');
	document.write('<meta NAME="resource-type" CONTENT="document">');
	document.write('<meta NAME="distribution" CONTENT="global">');
	document.write('<link rel="stylesheet" type="text/css" media="screen" title="lecture" href="');
		document.write(nestingDepth, styleFolder, 'notesPrint.css">');
	document.write('<link rel="stylesheet" type="text/css" media="print" href="');
		document.write(nestingDepth, styleFolder, 'notesPrint.css">');
}


