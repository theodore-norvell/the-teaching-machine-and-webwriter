// JavaScript Document
/***************** Math Symbols **********************************
cross-browser support for math symbols
******************************************************************/


function TableEntry(HTMLSymbol, LATexSymbol, Unicode, IEcode){
	this.HTMLSymbol = HTMLSymbol;
	this.LATexSymbol = LATexSymbol;
	this.Unicode = Unicode;
	this.IEcode = IEcode;
}

var mathTable = new Array(
	new TableEntry("alpha","alpha","&#945", "a"),
	new TableEntry("beta","beta","&#946", "b"),
	new TableEntry("gamma","gamma","&#947", "g"),
	new TableEntry("pi","pi","&#960", "p"),
	new TableEntry("Gamma","Gamma","&#915", "G"),
	new TableEntry("Delta","Delta","&#8710", "D"),
	new TableEntry("Theta","Theta","&#920", "Q"),
	new TableEntry("Sigma","Sigma","&#931", "S"),
	new TableEntry("Pi","Pi","&#928", "P"),
	new TableEntry("Omega","Omega","&#8486", "W"),
	new TableEntry("larr","leftarrow","&#8592", "¬"),
	new TableEntry("rarr","rightarrow","&#8594", "®"),
	new TableEntry("lArr","Leftarrow","&#8656", "Ü"),
	new TableEntry("rArr","Rightarrow","&#8658", "Þ"),
	new TableEntry("forall","forall","&#8704", '"'),
	new TableEntry("exist","exists","&#8707", "$"),
	new TableEntry("","pm","&#177", "±"),
	new TableEntry("","cup","&#8746", "È"),
	new TableEntry("","cap","&#8745", "Ç"),
	new TableEntry("","times","&#215", ""),
	new TableEntry("","cdot","&#183", ""),
	new TableEntry("","bullet","&#8226", "."),
	new TableEntry("","div","&#247", ","),
	new TableEntry("","leq","&#8804;", "£"),
	new TableEntry("","geq","&#8805", "³"),
	new TableEntry("","<","&lt", "&lt;"),
	new TableEntry("",">","&gt", "&gt;"),
	new TableEntry("","triangleq","&#8796", "<sup>D</sup>"),
	new TableEntry("","subset","&#8834", "Ì"),
	new TableEntry("","subseteq","&#8838", "Í"),
	new TableEntry("","supset","&#8835", "É"),
	new TableEntry("","supseteq","&#8839", "Ê"),
	new TableEntry("","equiv","&#8801", "º"),
	new TableEntry("","in","&#8712", "Î"),
	new TableEntry("","parallel","&#124&#124", "||")
)

function getForHTML(symbol){
	for (var i = 0; i < mathTable.length;i++)
		if(mathTable[i].HTMLSymbol == symbol)
			return i;
	return -1;
}

function getForLATex(symbol){
	for (var i = 0; i < mathTable.length;i++)
		if(mathTable[i].LATexSymbol == symbol)
			return i;
	return -1;
}

var useLATex = false;
function setSymbolsToLATex(set){
	useLATex = set;
}

function insertSymbol(symbolName){
	var entryNum = useLATex ? getForLATex(symbolName) : getForHTML(symbolName);
	if (isIE && mathTable[entryNum].IEcode != "")
		document.write('<font class="symbol" face="symbol">', mathTable[entryNum].IEcode,'</font>');
	else
		document.write('<font class="symbol">', mathTable[entryNum].Unicode,'</font>');
}
