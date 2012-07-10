var $_, $hxClasses = $hxClasses || {}, $estr = function() { return js.Boot.__string_rec(this,''); }
function $extend(from, fields) {
	function inherit() {}; inherit.prototype = from; var proto = new inherit();
	for (var name in fields) proto[name] = fields[name];
	return proto;
}
var EdgeFunctions = $hxClasses["EdgeFunctions"] = function() { }
EdgeFunctions.__name__ = ["EdgeFunctions"];
EdgeFunctions.prototype = {
	__class__: EdgeFunctions
}
var Hash = $hxClasses["Hash"] = function() {
	this.h = { };
};
Hash.__name__ = ["Hash"];
Hash.prototype = {
	h: null
	,set: function(key,value) {
		this.h["$" + key] = value;
	}
	,get: function(key) {
		return this.h["$" + key];
	}
	,exists: function(key) {
		return this.h.hasOwnProperty("$" + key);
	}
	,remove: function(key) {
		key = "$" + key;
		if(!this.h.hasOwnProperty(key)) return false;
		delete(this.h[key]);
		return true;
	}
	,keys: function() {
		var a = [];
		for( var key in this.h ) {
		if(this.h.hasOwnProperty(key)) a.push(key.substr(1));
		}
		return a.iterator();
	}
	,iterator: function() {
		return { ref : this.h, it : this.keys(), hasNext : function() {
			return this.it.hasNext();
		}, next : function() {
			var i = this.it.next();
			return this.ref["$" + i];
		}};
	}
	,toString: function() {
		var s = new StringBuf();
		s.b[s.b.length] = "{";
		var it = this.keys();
		while( it.hasNext() ) {
			var i = it.next();
			s.b[s.b.length] = i == null?"null":i;
			s.b[s.b.length] = " => ";
			s.add(Std.string(this.get(i)));
			if(it.hasNext()) s.b[s.b.length] = ", ";
		}
		s.b[s.b.length] = "}";
		return s.b.join("");
	}
	,__class__: Hash
}
var IntIter = $hxClasses["IntIter"] = function(min,max) {
	this.min = min;
	this.max = max;
};
IntIter.__name__ = ["IntIter"];
IntIter.prototype = {
	min: null
	,max: null
	,hasNext: function() {
		return this.min < this.max;
	}
	,next: function() {
		return this.min++;
	}
	,__class__: IntIter
}
var List = $hxClasses["List"] = function() {
	this.length = 0;
};
List.__name__ = ["List"];
List.prototype = {
	h: null
	,q: null
	,length: null
	,add: function(item) {
		var x = [item];
		if(this.h == null) this.h = x; else this.q[1] = x;
		this.q = x;
		this.length++;
	}
	,push: function(item) {
		var x = [item,this.h];
		this.h = x;
		if(this.q == null) this.q = x;
		this.length++;
	}
	,first: function() {
		return this.h == null?null:this.h[0];
	}
	,last: function() {
		return this.q == null?null:this.q[0];
	}
	,pop: function() {
		if(this.h == null) return null;
		var x = this.h[0];
		this.h = this.h[1];
		if(this.h == null) this.q = null;
		this.length--;
		return x;
	}
	,isEmpty: function() {
		return this.h == null;
	}
	,clear: function() {
		this.h = null;
		this.q = null;
		this.length = 0;
	}
	,remove: function(v) {
		var prev = null;
		var l = this.h;
		while(l != null) {
			if(l[0] == v) {
				if(prev == null) this.h = l[1]; else prev[1] = l[1];
				if(this.q == l) this.q = prev;
				this.length--;
				return true;
			}
			prev = l;
			l = l[1];
		}
		return false;
	}
	,iterator: function() {
		return { h : this.h, hasNext : function() {
			return this.h != null;
		}, next : function() {
			if(this.h == null) return null;
			var x = this.h[0];
			this.h = this.h[1];
			return x;
		}};
	}
	,toString: function() {
		var s = new StringBuf();
		var first = true;
		var l = this.h;
		s.b[s.b.length] = "{";
		while(l != null) {
			if(first) first = false; else s.b[s.b.length] = ", ";
			s.add(Std.string(l[0]));
			l = l[1];
		}
		s.b[s.b.length] = "}";
		return s.b.join("");
	}
	,join: function(sep) {
		var s = new StringBuf();
		var first = true;
		var l = this.h;
		while(l != null) {
			if(first) first = false; else s.b[s.b.length] = sep == null?"null":sep;
			s.add(l[0]);
			l = l[1];
		}
		return s.b.join("");
	}
	,filter: function(f) {
		var l2 = new List();
		var l = this.h;
		while(l != null) {
			var v = l[0];
			l = l[1];
			if(f(v)) l2.add(v);
		}
		return l2;
	}
	,map: function(f) {
		var b = new List();
		var l = this.h;
		while(l != null) {
			var v = l[0];
			l = l[1];
			b.add(f(v));
		}
		return b;
	}
	,__class__: List
}
var Main = $hxClasses["Main"] = function() { }
Main.__name__ = ["Main"];
Main.tmProxy = null;
Main.neverUsed = null;
Main.main = function() {
}
Main.buildGraph = function(doc) {
	var graph = new TutorialGraph();
	var graphBuilt = true;
	var graphDomNode;
	graphDomNode = doc.getElementById("graph");
	if(graphDomNode == null) {
		haxe.Log.trace("No element in the html file has id 'graph'",{ fileName : "Main.hx", lineNumber : 28, className : "Main", methodName : "buildGraph"});
		return null;
	}
	var startFunctionName = graphDomNode.getAttribute("data-function");
	if(startFunctionName != null) graph.setStartFunctionName(startFunctionName); else {
		haxe.Log.trace("Graph node has no 'data-function' attribute",{ fileName : "Main.hx", lineNumber : 32, className : "Main", methodName : "buildGraph"});
		return null;
	}
	var child = graphDomNode.firstChild;
	while(child != null) {
		if(child.nodeType == NodeTypes.ELEMENT_NODE && child.nodeName == "DIV") {
			var klass = child.getAttribute("class");
			if(klass == null) {
				haxe.Log.trace("A child of the 'graph' element has no class",{ fileName : "Main.hx", lineNumber : 42, className : "Main", methodName : "buildGraph"});
				graphBuilt = false;
			} else if(klass == "vertex") {
				var id = child.getAttribute("id");
				if(id == null) {
					haxe.Log.trace("There is a vertex with no id",{ fileName : "Main.hx", lineNumber : 47, className : "Main", methodName : "buildGraph"});
					graphBuilt = false;
				} else if(graph.vertices.exists(id)) {
					haxe.Log.trace("Duplicate vertex with id '" + id + "'",{ fileName : "Main.hx", lineNumber : 50, className : "Main", methodName : "buildGraph"});
					graphBuilt = false;
				} else {
					haxe.Log.trace("Building vertex " + id,{ fileName : "Main.hx", lineNumber : 53, className : "Main", methodName : "buildGraph"});
					var vertex = new TutorialVertex(id,child);
					graph.vertices.set(id,vertex);
					if(graph.startVertex == null) graph.setStartVertex(vertex);
				}
			} else if(klass == "edge") {
				var id = child.getAttribute("id");
				if(id == null) {
					haxe.Log.trace("There is an edge with no id",{ fileName : "Main.hx", lineNumber : 62, className : "Main", methodName : "buildGraph"});
					graphBuilt = false;
				} else if(graph.edges.exists(id)) {
					haxe.Log.trace("Duplicate edge with id '" + id + "'",{ fileName : "Main.hx", lineNumber : 65, className : "Main", methodName : "buildGraph"});
					graphBuilt = false;
				} else {
					haxe.Log.trace("Building edge " + id,{ fileName : "Main.hx", lineNumber : 68, className : "Main", methodName : "buildGraph"});
					var edge = new TutorialEdge(id,child);
					graph.edges.set(id,edge);
				}
			} else haxe.Log.trace("A child of the 'graph' node has a class '" + klass + "' that is neither 'vertex' nor 'edge'",{ fileName : "Main.hx", lineNumber : 72, className : "Main", methodName : "buildGraph"});
		}
		child = child.nextSibling;
	}
	var $it0 = graph.edges.iterator();
	while( $it0.hasNext() ) {
		var edge = $it0.next();
		var edgeLabel = edge.htmlNode.getAttribute("data-label");
		if(edgeLabel == null) {
			haxe.Log.trace("Edge '" + edge.id + "' is missing its 'data-label' attribute.",{ fileName : "Main.hx", lineNumber : 82, className : "Main", methodName : "buildGraph"});
			graphBuilt = false;
		} else edge.label = edgeLabel;
		var functionName = edge.htmlNode.getAttribute("data-function");
		if(functionName == null) {
			haxe.Log.trace("Edge '" + edge.id + "' is missing its 'data-function' attribute.",{ fileName : "Main.hx", lineNumber : 89, className : "Main", methodName : "buildGraph"});
			graphBuilt = false;
		} else edge.functionName = functionName;
		var sourceId = edge.htmlNode.getAttribute("data-source");
		if(sourceId == null) {
			haxe.Log.trace("Edge '" + edge.id + "' is missing its 'data-source' attribute.",{ fileName : "Main.hx", lineNumber : 97, className : "Main", methodName : "buildGraph"});
			graphBuilt = false;
		} else if(!graph.vertices.exists(sourceId)) {
			haxe.Log.trace("Edge '" + edge.id + "' has 'data-source' attribute of '" + sourceId + "'. But there is no such node.",{ fileName : "Main.hx", lineNumber : 100, className : "Main", methodName : "buildGraph"});
			graphBuilt = false;
		} else {
			edge.source = graph.vertices.get(sourceId);
			if(edgeLabel != null) graph.vertices.get(sourceId).outGoingEdges.set(edge.id,edge);
		}
		var targetId = edge.htmlNode.getAttribute("data-target");
		if(targetId == null) {
			haxe.Log.trace("Edge '" + edge.id + "' is missing its 'data-target' attribute.",{ fileName : "Main.hx", lineNumber : 109, className : "Main", methodName : "buildGraph"});
			graphBuilt = false;
		} else if(!graph.vertices.exists(targetId)) {
			haxe.Log.trace("Edge '" + edge.id + "' has 'data-target' attribute of '" + targetId + "'. But there is no such node.",{ fileName : "Main.hx", lineNumber : 112, className : "Main", methodName : "buildGraph"});
			graphBuilt = false;
		} else edge.target = graph.vertices.get(targetId);
	}
	if(graph.startVertex == null) graphBuilt = false;
	if(graphBuilt) return graph; else return null;
}
Main.onLoad = function() {
	haxe.Log.trace("onLoad starts",{ fileName : "Main.hx", lineNumber : 122, className : "Main", methodName : "onLoad"});
	var graph = Main.buildGraph(js.Lib.document);
	if(graph == null) {
		haxe.Log.trace("Failed to build graph",{ fileName : "Main.hx", lineNumber : 124, className : "Main", methodName : "onLoad"});
		return;
	}
	var applet;
	applet = js.Lib.document.applets["tm_applet"];
	if(applet == null) {
		haxe.Log.trace("Applet not found",{ fileName : "Main.hx", lineNumber : 128, className : "Main", methodName : "onLoad"});
		return;
	}
	haxe.Log.trace("Applet is " + applet,{ fileName : "Main.hx", lineNumber : 130, className : "Main", methodName : "onLoad"});
	Main.tmProxy = new TMProxy(applet);
	Main.executeFunction(graph.startFunctionName);
	Main.switchToVertex(graph.startVertex);
	haxe.Log.trace("onLoad ends",{ fileName : "Main.hx", lineNumber : 134, className : "Main", methodName : "onLoad"});
}
Main.executeFunction = function(name) {
	var klass = Type.resolveClass("EdgeFunctions");
	if(klass == null) haxe.Log.trace("No class 'EdgeFunctions' found.",{ fileName : "Main.hx", lineNumber : 140, className : "Main", methodName : "executeFunction"}); else {
		var fun = Reflect.field(klass,name);
		if(fun == null) haxe.Log.trace("Function named '" + name + "' not found.",{ fileName : "Main.hx", lineNumber : 144, className : "Main", methodName : "executeFunction"}); else fun.apply(klass,[Main.tmProxy]);
	}
}
Main.switchToVertex = function(vertex) {
	var instructionNode = js.Lib.document.getElementById("instructions");
	while(instructionNode.firstChild != null) instructionNode.removeChild(instructionNode.firstChild);
	instructionNode.insertBefore(vertex.htmlNode,null);
	var buttonsNode = js.Lib.document.getElementById("buttons");
	while(buttonsNode.firstChild != null) buttonsNode.removeChild(buttonsNode.firstChild);
	var $it0 = vertex.outGoingEdges.keys();
	while( $it0.hasNext() ) {
		var id = $it0.next();
		var edge = vertex.outGoingEdges.get(id);
		haxe.Log.trace("Edge id " + id,{ fileName : "Main.hx", lineNumber : 161, className : "Main", methodName : "switchToVertex"});
		if(edge.label == "back") {
			haxe.Log.trace("In back, comparing : " + edge.target.id + "& stack top : " + Main.vertexStack.first().id,{ fileName : "Main.hx", lineNumber : 164, className : "Main", methodName : "switchToVertex"});
			if(edge.target.id == Main.vertexStack.first().id) {
				var target = [edge.target];
				var functionName = [edge.functionName];
				var labelNode = js.Lib.document.createTextNode(edge.label);
				var button = js.Lib.document.createElement("button");
				button.insertBefore(labelNode,null);
				button.onclick = (function(functionName,target) {
					return function(event) {
						Main.executeFunction(functionName[0]);
						var temp = Main.vertexStack.pop();
						haxe.Log.trace(temp.id + "popped from stack\n Going to switch to :" + target[0].id,{ fileName : "Main.hx", lineNumber : 175, className : "Main", methodName : "switchToVertex"});
						Main.switchToVertex(target[0]);
					};
				})(functionName,target);
				buttonsNode.insertBefore(button,null);
			} else continue;
		} else {
			var target = [edge.target];
			var functionName = [edge.functionName];
			var labelNode = js.Lib.document.createTextNode(edge.label);
			var button = js.Lib.document.createElement("button");
			button.insertBefore(labelNode,null);
			button.onclick = (function(functionName,target) {
				return function(event) {
					Main.executeFunction(functionName[0]);
					Main.vertexStack.push(vertex);
					haxe.Log.trace(vertex.id + "pushed  on stack\nAbout to switch to target: " + target[0].id,{ fileName : "Main.hx", lineNumber : 192, className : "Main", methodName : "switchToVertex"});
					Main.switchToVertex(target[0]);
				};
			})(functionName,target);
			buttonsNode.insertBefore(button,null);
		}
	}
}
Main.prototype = {
	__class__: Main
}
var NodeTypes = $hxClasses["NodeTypes"] = function() { }
NodeTypes.__name__ = ["NodeTypes"];
NodeTypes.prototype = {
	__class__: NodeTypes
}
var Reflect = $hxClasses["Reflect"] = function() { }
Reflect.__name__ = ["Reflect"];
Reflect.hasField = function(o,field) {
	return Object.prototype.hasOwnProperty.call(o,field);
}
Reflect.field = function(o,field) {
	var v = null;
	try {
		v = o[field];
	} catch( e ) {
	}
	return v;
}
Reflect.setField = function(o,field,value) {
	o[field] = value;
}
Reflect.getProperty = function(o,field) {
	var tmp;
	return o == null?null:o.__properties__ && (tmp = o.__properties__["get_" + field])?o[tmp]():o[field];
}
Reflect.setProperty = function(o,field,value) {
	var tmp;
	if(o.__properties__ && (tmp = o.__properties__["set_" + field])) o[tmp](value); else o[field] = value;
}
Reflect.callMethod = function(o,func,args) {
	return func.apply(o,args);
}
Reflect.fields = function(o) {
	var a = [];
	if(o != null) {
		var hasOwnProperty = Object.prototype.hasOwnProperty;
		for( var f in o ) {
		if(hasOwnProperty.call(o,f)) a.push(f);
		}
	}
	return a;
}
Reflect.isFunction = function(f) {
	return typeof(f) == "function" && f.__name__ == null;
}
Reflect.compare = function(a,b) {
	return a == b?0:a > b?1:-1;
}
Reflect.compareMethods = function(f1,f2) {
	if(f1 == f2) return true;
	if(!Reflect.isFunction(f1) || !Reflect.isFunction(f2)) return false;
	return f1.scope == f2.scope && f1.method == f2.method && f1.method != null;
}
Reflect.isObject = function(v) {
	if(v == null) return false;
	var t = typeof(v);
	return t == "string" || t == "object" && !v.__enum__ || t == "function" && v.__name__ != null;
}
Reflect.deleteField = function(o,f) {
	if(!Reflect.hasField(o,f)) return false;
	delete(o[f]);
	return true;
}
Reflect.copy = function(o) {
	var o2 = { };
	var _g = 0, _g1 = Reflect.fields(o);
	while(_g < _g1.length) {
		var f = _g1[_g];
		++_g;
		o2[f] = Reflect.field(o,f);
	}
	return o2;
}
Reflect.makeVarArgs = function(f) {
	return function() {
		var a = Array.prototype.slice.call(arguments);
		return f(a);
	};
}
Reflect.prototype = {
	__class__: Reflect
}
var Std = $hxClasses["Std"] = function() { }
Std.__name__ = ["Std"];
Std["is"] = function(v,t) {
	return js.Boot.__instanceof(v,t);
}
Std.string = function(s) {
	return js.Boot.__string_rec(s,"");
}
Std["int"] = function(x) {
	return x | 0;
}
Std.parseInt = function(x) {
	var v = parseInt(x,10);
	if(v == 0 && x.charCodeAt(1) == 120) v = parseInt(x);
	if(isNaN(v)) return null;
	return v;
}
Std.parseFloat = function(x) {
	return parseFloat(x);
}
Std.random = function(x) {
	return Math.floor(Math.random() * x);
}
Std.prototype = {
	__class__: Std
}
var StringBuf = $hxClasses["StringBuf"] = function() {
	this.b = new Array();
};
StringBuf.__name__ = ["StringBuf"];
StringBuf.prototype = {
	add: function(x) {
		this.b[this.b.length] = x == null?"null":x;
	}
	,addSub: function(s,pos,len) {
		this.b[this.b.length] = s.substr(pos,len);
	}
	,addChar: function(c) {
		this.b[this.b.length] = String.fromCharCode(c);
	}
	,toString: function() {
		return this.b.join("");
	}
	,b: null
	,__class__: StringBuf
}
var TMExternalCommandInterface = $hxClasses["TMExternalCommandInterface"] = function() { }
TMExternalCommandInterface.__name__ = ["TMExternalCommandInterface"];
TMExternalCommandInterface.prototype = {
	loadString: null
	,loadRemoteFile: null
	,readRemoteConfiguration: null
	,registerRemoteDataFile: null
	,clearRemoteDataFiles: null
	,addInputString: null
	,addProgramArgument: null
	,getOutputString: null
	,reStart: null
	,editCurrentFile: null
	,quit: null
	,initializeTheState: null
	,go: null
	,goBack: null
	,goForward: null
	,microStep: null
	,overAll: null
	,intoExp: null
	,intoSub: null
	,toBreakPoint: null
	,toCursor: null
	,autoRun: null
	,showTM: null
	,isTMShowing: null
	,autoStep: null
	,stopAuto: null
	,setAutoStepRate: null
	,getSnap: null
	,getLastSnapWidth: null
	,getLastSnapHeight: null
	,getComparison: null
	,getLocalInt: null
	,isRunDone: null
	,setSelectionString: null
	,getSelectionString: null
	,__class__: TMExternalCommandInterface
}
var TMProxy = $hxClasses["TMProxy"] = function(applet) {
	this.theTM = applet;
};
TMProxy.__name__ = ["TMProxy"];
TMProxy.__interfaces__ = [TMExternalCommandInterface];
TMProxy.prototype = {
	theTM: null
	,loadString: function(fileName,programSource) {
		this.theTM.loadString(fileName,programSource);
	}
	,loadRemoteFile: function(root,fileName) {
		this.theTM.loadRemoteFile(root,fileName);
	}
	,readRemoteConfiguration: function(fileName) {
		this.theTM.readRemoteConfiguration(fileName);
	}
	,registerRemoteDataFile: function(fileName) {
		this.theTM.registerRemoteDataFile(fileName);
	}
	,clearRemoteDataFiles: function() {
		this.theTM.clearRemoteDataFiles();
	}
	,addInputString: function(input) {
		this.theTM.addInputString(input);
	}
	,addProgramArgument: function(argument) {
		this.theTM.addProgramArgument(argument);
	}
	,getOutputString: function() {
		return this.theTM.getOutputString();
	}
	,reStart: function() {
		this.theTM.reStart();
	}
	,editCurrentFile: function() {
		this.theTM.editCurrentFile();
	}
	,quit: function() {
		this.theTM.quit();
	}
	,initializeTheState: function() {
		this.theTM.initializeTheState();
	}
	,go: function(commandString) {
		this.theTM.go(commandString);
	}
	,goBack: function() {
		this.theTM.goBack();
	}
	,goForward: function() {
		this.theTM.goForward();
	}
	,microStep: function() {
		this.theTM.microStep();
	}
	,overAll: function() {
		this.theTM.overAll();
	}
	,intoExp: function() {
		this.theTM.intoExp();
	}
	,intoSub: function() {
		this.theTM.intoSub();
	}
	,toBreakPoint: function() {
		this.theTM.toBreakPoint();
	}
	,toCursor: function(fileName,cursor) {
		this.theTM.toCursor(fileName,cursor);
	}
	,autoRun: function() {
		this.theTM.autoRun();
	}
	,showTM: function(visible) {
		this.theTM.showTM(visible);
	}
	,isTMShowing: function() {
		return this.theTM.isTMShowing();
	}
	,autoStep: function(fileName,lineNo) {
		if(fileName == null) this.theTM.autoStep(); else this.theTM.autoStep(fileName,lineNo);
	}
	,stopAuto: function() {
		this.theTM.stopAuto();
	}
	,setAutoStepRate: function(rateConstant) {
		this.theTM.setAutoStepRate(rateConstant);
	}
	,getSnap: function(plugIn,id) {
		return this.theTM.getSnap(plugIn,id);
	}
	,getLastSnapWidth: function() {
		return this.theTM.getLastSnapWidth();
	}
	,getLastSnapHeight: function() {
		return this.theTM.getLastSnapHeight();
	}
	,getComparison: function(plugIn,n) {
		return this.theTM.getComparison(plugIn,n);
	}
	,getLocalInt: function(datumName) {
		return this.theTM.getLocalInt(datumName);
	}
	,isRunDone: function() {
		return this.theTM.isRunDone();
	}
	,setSelectionString: function(str) {
		this.theTM.setSelectionString(str);
	}
	,getSelectionString: function() {
		return this.theTM.getSelectionString();
	}
	,__class__: TMProxy
}
var TutorialEdge = $hxClasses["TutorialEdge"] = function(id,htmlNode) {
	this.id = id;
	this.htmlNode = htmlNode;
};
TutorialEdge.__name__ = ["TutorialEdge"];
TutorialEdge.prototype = {
	id: null
	,htmlNode: null
	,source: null
	,target: null
	,functionName: null
	,label: null
	,__class__: TutorialEdge
}
var TutorialGraph = $hxClasses["TutorialGraph"] = function() {
	this.vertices = new Hash();
	this.edges = new Hash();
	this.startVertex = null;
	this.startFunctionName = null;
};
TutorialGraph.__name__ = ["TutorialGraph"];
TutorialGraph.prototype = {
	vertices: null
	,edges: null
	,startVertex: null
	,startFunctionName: null
	,setStartVertex: function(n) {
		this.startVertex = n;
	}
	,setStartFunctionName: function(name) {
		this.startFunctionName = name;
	}
	,__class__: TutorialGraph
}
var TutorialVertex = $hxClasses["TutorialVertex"] = function(id,htmlNode) {
	this.id = id;
	this.htmlNode = htmlNode;
	this.outGoingEdges = new Hash();
};
TutorialVertex.__name__ = ["TutorialVertex"];
TutorialVertex.prototype = {
	id: null
	,outGoingEdges: null
	,htmlNode: null
	,__class__: TutorialVertex
}
var ValueType = $hxClasses["ValueType"] = { __ename__ : ["ValueType"], __constructs__ : ["TNull","TInt","TFloat","TBool","TObject","TFunction","TClass","TEnum","TUnknown"] }
ValueType.TNull = ["TNull",0];
ValueType.TNull.toString = $estr;
ValueType.TNull.__enum__ = ValueType;
ValueType.TInt = ["TInt",1];
ValueType.TInt.toString = $estr;
ValueType.TInt.__enum__ = ValueType;
ValueType.TFloat = ["TFloat",2];
ValueType.TFloat.toString = $estr;
ValueType.TFloat.__enum__ = ValueType;
ValueType.TBool = ["TBool",3];
ValueType.TBool.toString = $estr;
ValueType.TBool.__enum__ = ValueType;
ValueType.TObject = ["TObject",4];
ValueType.TObject.toString = $estr;
ValueType.TObject.__enum__ = ValueType;
ValueType.TFunction = ["TFunction",5];
ValueType.TFunction.toString = $estr;
ValueType.TFunction.__enum__ = ValueType;
ValueType.TClass = function(c) { var $x = ["TClass",6,c]; $x.__enum__ = ValueType; $x.toString = $estr; return $x; }
ValueType.TEnum = function(e) { var $x = ["TEnum",7,e]; $x.__enum__ = ValueType; $x.toString = $estr; return $x; }
ValueType.TUnknown = ["TUnknown",8];
ValueType.TUnknown.toString = $estr;
ValueType.TUnknown.__enum__ = ValueType;
var Type = $hxClasses["Type"] = function() { }
Type.__name__ = ["Type"];
Type.getClass = function(o) {
	if(o == null) return null;
	if(o.__enum__ != null) return null;
	return o.__class__;
}
Type.getEnum = function(o) {
	if(o == null) return null;
	return o.__enum__;
}
Type.getSuperClass = function(c) {
	return c.__super__;
}
Type.getClassName = function(c) {
	var a = c.__name__;
	return a.join(".");
}
Type.getEnumName = function(e) {
	var a = e.__ename__;
	return a.join(".");
}
Type.resolveClass = function(name) {
	var cl = $hxClasses[name];
	if(cl == null || cl.__name__ == null) return null;
	return cl;
}
Type.resolveEnum = function(name) {
	var e = $hxClasses[name];
	if(e == null || e.__ename__ == null) return null;
	return e;
}
Type.createInstance = function(cl,args) {
	switch(args.length) {
	case 0:
		return new cl();
	case 1:
		return new cl(args[0]);
	case 2:
		return new cl(args[0],args[1]);
	case 3:
		return new cl(args[0],args[1],args[2]);
	case 4:
		return new cl(args[0],args[1],args[2],args[3]);
	case 5:
		return new cl(args[0],args[1],args[2],args[3],args[4]);
	case 6:
		return new cl(args[0],args[1],args[2],args[3],args[4],args[5]);
	case 7:
		return new cl(args[0],args[1],args[2],args[3],args[4],args[5],args[6]);
	case 8:
		return new cl(args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7]);
	default:
		throw "Too many arguments";
	}
	return null;
}
Type.createEmptyInstance = function(cl) {
	function empty() {}; empty.prototype = cl.prototype;
	return new empty();
}
Type.createEnum = function(e,constr,params) {
	var f = Reflect.field(e,constr);
	if(f == null) throw "No such constructor " + constr;
	if(Reflect.isFunction(f)) {
		if(params == null) throw "Constructor " + constr + " need parameters";
		return f.apply(e,params);
	}
	if(params != null && params.length != 0) throw "Constructor " + constr + " does not need parameters";
	return f;
}
Type.createEnumIndex = function(e,index,params) {
	var c = e.__constructs__[index];
	if(c == null) throw index + " is not a valid enum constructor index";
	return Type.createEnum(e,c,params);
}
Type.getInstanceFields = function(c) {
	var a = [];
	for(var i in c.prototype) a.push(i);
	a.remove("__class__");
	a.remove("__properties__");
	return a;
}
Type.getClassFields = function(c) {
	var a = Reflect.fields(c);
	a.remove("__name__");
	a.remove("__interfaces__");
	a.remove("__properties__");
	a.remove("__super__");
	a.remove("prototype");
	return a;
}
Type.getEnumConstructs = function(e) {
	var a = e.__constructs__;
	return a.copy();
}
Type["typeof"] = function(v) {
	switch(typeof(v)) {
	case "boolean":
		return ValueType.TBool;
	case "string":
		return ValueType.TClass(String);
	case "number":
		if(Math.ceil(v) == v % 2147483648.0) return ValueType.TInt;
		return ValueType.TFloat;
	case "object":
		if(v == null) return ValueType.TNull;
		var e = v.__enum__;
		if(e != null) return ValueType.TEnum(e);
		var c = v.__class__;
		if(c != null) return ValueType.TClass(c);
		return ValueType.TObject;
	case "function":
		if(v.__name__ != null) return ValueType.TObject;
		return ValueType.TFunction;
	case "undefined":
		return ValueType.TNull;
	default:
		return ValueType.TUnknown;
	}
}
Type.enumEq = function(a,b) {
	if(a == b) return true;
	try {
		if(a[0] != b[0]) return false;
		var _g1 = 2, _g = a.length;
		while(_g1 < _g) {
			var i = _g1++;
			if(!Type.enumEq(a[i],b[i])) return false;
		}
		var e = a.__enum__;
		if(e != b.__enum__ || e == null) return false;
	} catch( e ) {
		return false;
	}
	return true;
}
Type.enumConstructor = function(e) {
	return e[0];
}
Type.enumParameters = function(e) {
	return e.slice(2);
}
Type.enumIndex = function(e) {
	return e[1];
}
Type.allEnums = function(e) {
	var all = [];
	var cst = e.__constructs__;
	var _g = 0;
	while(_g < cst.length) {
		var c = cst[_g];
		++_g;
		var v = Reflect.field(e,c);
		if(!Reflect.isFunction(v)) all.push(v);
	}
	return all;
}
Type.prototype = {
	__class__: Type
}
var haxe = haxe || {}
haxe.Log = $hxClasses["haxe.Log"] = function() { }
haxe.Log.__name__ = ["haxe","Log"];
haxe.Log.trace = function(v,infos) {
	js.Boot.__trace(v,infos);
}
haxe.Log.clear = function() {
	js.Boot.__clear_trace();
}
haxe.Log.prototype = {
	__class__: haxe.Log
}
var js = js || {}
js.Boot = $hxClasses["js.Boot"] = function() { }
js.Boot.__name__ = ["js","Boot"];
js.Boot.__unhtml = function(s) {
	return s.split("&").join("&amp;").split("<").join("&lt;").split(">").join("&gt;");
}
js.Boot.__trace = function(v,i) {
	var msg = i != null?i.fileName + ":" + i.lineNumber + ": ":"";
	msg += js.Boot.__string_rec(v,"");
	var d = document.getElementById("haxe:trace");
	if(d != null) d.innerHTML += js.Boot.__unhtml(msg) + "<br/>"; else if(typeof(console) != "undefined" && console.log != null) console.log(msg);
}
js.Boot.__clear_trace = function() {
	var d = document.getElementById("haxe:trace");
	if(d != null) d.innerHTML = "";
}
js.Boot.__string_rec = function(o,s) {
	if(o == null) return "null";
	if(s.length >= 5) return "<...>";
	var t = typeof(o);
	if(t == "function" && (o.__name__ != null || o.__ename__ != null)) t = "object";
	switch(t) {
	case "object":
		if(o instanceof Array) {
			if(o.__enum__ != null) {
				if(o.length == 2) return o[0];
				var str = o[0] + "(";
				s += "\t";
				var _g1 = 2, _g = o.length;
				while(_g1 < _g) {
					var i = _g1++;
					if(i != 2) str += "," + js.Boot.__string_rec(o[i],s); else str += js.Boot.__string_rec(o[i],s);
				}
				return str + ")";
			}
			var l = o.length;
			var i;
			var str = "[";
			s += "\t";
			var _g = 0;
			while(_g < l) {
				var i1 = _g++;
				str += (i1 > 0?",":"") + js.Boot.__string_rec(o[i1],s);
			}
			str += "]";
			return str;
		}
		var tostr;
		try {
			tostr = o.toString;
		} catch( e ) {
			return "???";
		}
		if(tostr != null && tostr != Object.toString) {
			var s2 = o.toString();
			if(s2 != "[object Object]") return s2;
		}
		var k = null;
		var str = "{\n";
		s += "\t";
		var hasp = o.hasOwnProperty != null;
		for( var k in o ) { ;
		if(hasp && !o.hasOwnProperty(k)) {
			continue;
		}
		if(k == "prototype" || k == "__class__" || k == "__super__" || k == "__interfaces__" || k == "__properties__") {
			continue;
		}
		if(str.length != 2) str += ", \n";
		str += s + k + " : " + js.Boot.__string_rec(o[k],s);
		}
		s = s.substring(1);
		str += "\n" + s + "}";
		return str;
	case "function":
		return "<function>";
	case "string":
		return o;
	default:
		return String(o);
	}
}
js.Boot.__interfLoop = function(cc,cl) {
	if(cc == null) return false;
	if(cc == cl) return true;
	var intf = cc.__interfaces__;
	if(intf != null) {
		var _g1 = 0, _g = intf.length;
		while(_g1 < _g) {
			var i = _g1++;
			var i1 = intf[i];
			if(i1 == cl || js.Boot.__interfLoop(i1,cl)) return true;
		}
	}
	return js.Boot.__interfLoop(cc.__super__,cl);
}
js.Boot.__instanceof = function(o,cl) {
	try {
		if(o instanceof cl) {
			if(cl == Array) return o.__enum__ == null;
			return true;
		}
		if(js.Boot.__interfLoop(o.__class__,cl)) return true;
	} catch( e ) {
		if(cl == null) return false;
	}
	switch(cl) {
	case Int:
		return Math.ceil(o%2147483648.0) === o;
	case Float:
		return typeof(o) == "number";
	case Bool:
		return o === true || o === false;
	case String:
		return typeof(o) == "string";
	case Dynamic:
		return true;
	default:
		if(o == null) return false;
		return o.__enum__ == cl || cl == Class && o.__name__ != null || cl == Enum && o.__ename__ != null;
	}
}
js.Boot.__init = function() {
	js.Lib.isIE = typeof document!='undefined' && document.all != null && typeof window!='undefined' && window.opera == null;
	js.Lib.isOpera = typeof window!='undefined' && window.opera != null;
	Array.prototype.copy = Array.prototype.slice;
	Array.prototype.insert = function(i,x) {
		this.splice(i,0,x);
	};
	Array.prototype.remove = Array.prototype.indexOf?function(obj) {
		var idx = this.indexOf(obj);
		if(idx == -1) return false;
		this.splice(idx,1);
		return true;
	}:function(obj) {
		var i = 0;
		var l = this.length;
		while(i < l) {
			if(this[i] == obj) {
				this.splice(i,1);
				return true;
			}
			i++;
		}
		return false;
	};
	Array.prototype.iterator = function() {
		return { cur : 0, arr : this, hasNext : function() {
			return this.cur < this.arr.length;
		}, next : function() {
			return this.arr[this.cur++];
		}};
	};
	if(String.prototype.cca == null) String.prototype.cca = String.prototype.charCodeAt;
	String.prototype.charCodeAt = function(i) {
		var x = this.cca(i);
		if(x != x) return undefined;
		return x;
	};
	var oldsub = String.prototype.substr;
	String.prototype.substr = function(pos,len) {
		if(pos != null && pos != 0 && len != null && len < 0) return "";
		if(len == null) len = this.length;
		if(pos < 0) {
			pos = this.length + pos;
			if(pos < 0) pos = 0;
		} else if(len < 0) len = this.length + len - pos;
		return oldsub.apply(this,[pos,len]);
	};
	Function.prototype["$bind"] = function(o) {
		var f = function() {
			return f.method.apply(f.scope,arguments);
		};
		f.scope = o;
		f.method = this;
		return f;
	};
}
js.Boot.prototype = {
	__class__: js.Boot
}
js.Lib = $hxClasses["js.Lib"] = function() { }
js.Lib.__name__ = ["js","Lib"];
js.Lib.isIE = null;
js.Lib.isOpera = null;
js.Lib.document = null;
js.Lib.window = null;
js.Lib.alert = function(v) {
	alert(js.Boot.__string_rec(v,""));
}
js.Lib.eval = function(code) {
	return eval(code);
}
js.Lib.setErrorHandler = function(f) {
	js.Lib.onerror = f;
}
js.Lib.prototype = {
	__class__: js.Lib
}
js.Boot.__res = {}
js.Boot.__init();
{
	Math.__name__ = ["Math"];
	Math.NaN = Number["NaN"];
	Math.NEGATIVE_INFINITY = Number["NEGATIVE_INFINITY"];
	Math.POSITIVE_INFINITY = Number["POSITIVE_INFINITY"];
	$hxClasses["Math"] = Math;
	Math.isFinite = function(i) {
		return isFinite(i);
	};
	Math.isNaN = function(i) {
		return isNaN(i);
	};
}
{
	String.prototype.__class__ = $hxClasses["String"] = String;
	String.__name__ = ["String"];
	Array.prototype.__class__ = $hxClasses["Array"] = Array;
	Array.__name__ = ["Array"];
	var Int = $hxClasses["Int"] = { __name__ : ["Int"]};
	var Dynamic = $hxClasses["Dynamic"] = { __name__ : ["Dynamic"]};
	var Float = $hxClasses["Float"] = Number;
	Float.__name__ = ["Float"];
	var Bool = $hxClasses["Bool"] = Boolean;
	Bool.__ename__ = ["Bool"];
	var Class = $hxClasses["Class"] = { __name__ : ["Class"]};
	var Enum = { };
	var Void = $hxClasses["Void"] = { __ename__ : ["Void"]};
}
{
	if(typeof document != "undefined") js.Lib.document = document;
	if(typeof window != "undefined") {
		js.Lib.window = window;
		js.Lib.window.onerror = function(msg,url,line) {
			var f = js.Lib.onerror;
			if(f == null) return false;
			return f(msg,[url + ":" + line]);
		};
	}
}
Main.vertexStack = new List();
NodeTypes.ELEMENT_NODE = 1;
NodeTypes.ATTRIBUTE_NODE = 2;
NodeTypes.TEXT_NODE = 3;
NodeTypes.CDATA_SECTION_NODE = 4;
NodeTypes.ENTITY_REFERENCE_NODE = 5;
NodeTypes.ENTITY_NODE = 6;
NodeTypes.PROCESSING_INSTRUCTION_NODE = 7;
NodeTypes.COMMENT_NODE = 8;
NodeTypes.DOCUMENT_NODE = 9;
NodeTypes.DOCUMENT_TYPE_NODE = 10;
NodeTypes.DOCUMENT_FRAGMENT_NODE = 11;
NodeTypes.NOTATION_NODE = 12;
js.Lib.onerror = null;
Main.main()