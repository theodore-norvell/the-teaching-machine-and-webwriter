/// <reference path="JSTM.ts" />
/// <reference path="Promise.ts"/>
/// <reference path="../builder/quizBuilder.ts"/>
/// <reference path="../library/jquery.d.ts" />
var jstm;
(function (jstm_1) {
    var concreteJSTM = (function () {
        //qz:quizBuilder.FITEQuestion;
        function concreteJSTM(guid) {
            this.guid = guid;
        }
        //make disolay
        concreteJSTM.prototype.makeTMDisplay = function () {
            //code object
            var code = [];
            code = this.mirrorData.code;
            var focus = this.mirrorData.focus;
            var focusNumber = focus.lineNumber;
            console.log(code.length);
            var table = document.createElement('table');
            table.setAttribute("class", "dispaly-program");
            console.log(table.className);
            //number of rows
            for (var i = 0; i < code.length; i++) {
                //lineNumber
                var lineNumber = code[i].coords.lineNumber;
                //chars
                var chars = code[i].chars;
                //Array []
                var markUp = code[i].markUp;
                var tr = document.createElement('tr');
                var td1 = document.createElement('td');
                var td2 = document.createElement('td');
                //var text1 = document.createTextNode(lineNumber+':');
                var textafterprocess = this.DisplayEffect(chars, markUp);
                var line = document.createElement('span');
                line.setAttribute('id', lineNumber);
                line.innerHTML = "<span class=tm-red>" + lineNumber + ':' + "</span>";
                var text = document.createElement('pre');
                text.innerHTML = textafterprocess;
                //var text2 = textNode;
                console.log(textafterprocess);
                //td.appendChild(text1);
                td1.appendChild(line);
                td2.appendChild(text);
                tr.appendChild(td1);
                tr.appendChild(td2);
                table.appendChild(tr);
            }
            //
            //console.log(focusNumber);
            //console.log(document.getElementById(focusNumber));
            return table;
        };
        //makeGoForwardButton
        concreteJSTM.prototype.makeGoForwardButton = function (onDone, onFail) {
            this.goForwardButton = document.createElement('button');
            this.goForwardButton.setAttribute('disabled', 'disabled');
            this.goForwardButton.innerHTML = 'goForward';
            return this.goForwardButton;
        };
        //makeGoBackButton
        concreteJSTM.prototype.makeGoBackButton = function (onDone, onFail) {
            this.goBackButton = document.createElement('button');
            this.goBackButton.setAttribute('disabled', 'disabled');
            this.goBackButton.innerHTML = 'goBack';
            return this.goBackButton;
        };
        //makeExpressionDisplay
        concreteJSTM.prototype.makeExpressionDisplay = function (onDone, onFail) {
            this.expressionDisplay = document.createElement('span');
            return this.expressionDisplay;
        };
        //setProgramText
        concreteJSTM.prototype.setprogramText = function (pt) {
            this.programText = pt;
        };
        //getProgramText 
        concreteJSTM.prototype.getprogramText = function () {
            return this.programText;
        };
        //getstatus
        concreteJSTM.prototype.getStatus = function () {
            return this.status;
        };
        //set
        concreteJSTM.prototype.setStatus = function (status) {
            this.status = status;
        };
        //getMessage    
        concreteJSTM.prototype.getMessage = function () {
            return this.message;
        };
        //set
        concreteJSTM.prototype.setMessage = function (message) {
            this.message = message;
        };
        //createRTM   
        concreteJSTM.createRTM = function () {
            var d = P.defer();
            var p = d.promise();
            // var thisJSTM=this;
            $.ajax({
                url: 'createRemoteTM.create',
                dataType: "json",
                type: "POST"
            })
                .done(function (data) {
                var tempVar = data;
                //assign back-end json object to the front end parameters in this js file;
                var guid = tempVar.guid;
                //if there is no error in the createRemoteTM step
                //console.log(status);
                //console.log(thisJSTM.mirrorData);
                //fullfill the defered state
                var thisJSTM = new concreteJSTM(guid);
                d.resolve(thisJSTM);
            })
                .fail(function () { d.reject(this); console.log("error! in createRTM in concreteJSTM"); });
            return p;
        };
        //loadString
        concreteJSTM.prototype.loadString = function (program, filename) {
            var d = P.defer();
            var p = d.promise();
            var thisJSTM = this;
            console.log(thisJSTM);
            console.log(thisJSTM.guid);
            $.ajax({
                url: 'loadString.load',
                dataType: "json",
                type: "POST",
                data: { 'guid': thisJSTM.guid,
                    'Codes': program,
                    'filename': filename }
            })
                .done(function (data) {
                thisJSTM.mirrorData = data;
                //var parameterTemp =data;
                //assign back-end json object to the front end parameters in this js file;
                var reasonFlag = thisJSTM.mirrorData.reasonFlag;
                var reason = thisJSTM.mirrorData.reason;
                var status = thisJSTM.mirrorData.status;
                //if there is no error in the createRemoteTM step
                thisJSTM.setStatus(status);
                //console.log(thisJSTM.getStatus());
                //console.log(thisJSTM.mirrorData); 
                if (thisJSTM.getStatus() == '3') {
                    d.resolve(thisJSTM);
                }
                else {
                    //thisJSTM.qz.fieldSet.style.display='none';
                    thisJSTM.setMessage(reason);
                    alert(thisJSTM.getMessage());
                }
            })
                .fail(function () { d.reject(this); console.log('error! in loadString in concreteJSTM'); });
            return p;
        };
        //initialize  
        concreteJSTM.prototype.initialize = function (responseWantedFlag) {
            var d = P.defer();
            var p = d.promise();
            var thisJSTM = this;
            $.ajax({
                url: 'initializeTheState.initialize',
                dataType: "json",
                type: "POST",
                data: { 'guid': thisJSTM.guid,
                    'responseWantedFlag': responseWantedFlag } })
                .done(function (data) {
                thisJSTM.mirrorData = data;
                //var parameterTemp =data;
                var reasonFlag = thisJSTM.mirrorData.reasonFlag;
                var reason = thisJSTM.mirrorData.reason;
                var status = thisJSTM.mirrorData.status;
                thisJSTM.setStatus(status);
                //console.log(thisJSTM.getStatus());
                //console.log(thisJSTM.mirrorData); 
                //fuilfull
                if (status == '4') {
                    d.resolve(thisJSTM);
                }
                else {
                    //thisJSTM.qz.fieldSet.style.display='none';
                    thisJSTM.setMessage(reason);
                    alert(thisJSTM.getMessage());
                }
            })
                .fail(function (data) { d.reject(this); console.log('error! in the initialize in concreteJSTM'); });
            return p;
        };
        //go
        concreteJSTM.prototype.go = function (commandString) {
            var d = P.defer();
            var p = d.promise();
            var thisJSTM = this;
            $.ajax({
                url: 'go.g',
                dataType: "json",
                type: "POST",
                data: { 'guid': thisJSTM.guid,
                    'command': commandString } })
                .done(function (data) {
                thisJSTM.mirrorData = data;
                //var parameterTemp =data;
                var reason = thisJSTM.mirrorData.reason;
                var status = thisJSTM.mirrorData.status;
                var expression = thisJSTM.mirrorData.expression;
                /**parse the characters of the expression**/
                var expTemp = thisJSTM.ExpressionEffect(expression);
                //fuilfull
                d.resolve(thisJSTM);
                if (status != '6' && status != '7') {
                    thisJSTM.expressionDisplay.innerHTML = '';
                    thisJSTM.expressionDisplay.innerHTML = "<span>" + expTemp + "</span>";
                }
                if (status == '6') {
                    thisJSTM.expressionDisplay.innerHTML = '';
                    thisJSTM.expressionDisplay.innerHTML = "<span>" + ' EXECUTION_COMPLETE ' + "</span>" + "<br>";
                }
                if (status == '7') {
                    thisJSTM.expressionDisplay.innerHTML = "<span>" + ' EXECUTION_COMPLETE ' + "</span>" + "<br>";
                }
            })
                .fail(function (data) { d.reject(this); console.log('error! in the go in concreteJSTM'); });
            return p;
        };
        //goForward
        concreteJSTM.prototype.goForward = function () {
            var d = P.defer();
            var p = d.promise();
            var thisJSTM = this;
            $.ajax({
                url: 'goForward.goForward',
                dataType: "json",
                type: "POST",
                data: { 'myguid': thisJSTM.guid } })
                .done(function (data) {
                //data comes back from the server. 
                thisJSTM.mirrorData = data;
                //var parameterTemp =data;
                var reason = thisJSTM.mirrorData.reason;
                var status = thisJSTM.mirrorData.status;
                var expression = thisJSTM.mirrorData.expression;
                /**parse the characters of the expression**/
                var expTemp = thisJSTM.ExpressionEffect(expression);
                // console.log(document.getElementById(focusNumber));
                //fuilfill
                d.resolve(thisJSTM);
                if (status != '6' && status != '7') {
                    thisJSTM.expressionDisplay.innerHTML = '';
                    thisJSTM.expressionDisplay.innerHTML = "<span>" + expTemp + "</span>";
                }
                if (status == '6') {
                    thisJSTM.expressionDisplay.innerHTML = '';
                    thisJSTM.expressionDisplay.innerHTML = "<span>" + ' EXECUTION_COMPLETE ' + "</span>" + "<br>";
                }
                if (status == '7') {
                    //thisJSTM.qz.expressionDisplay.innerHTML='';
                    thisJSTM.expressionDisplay.innerHTML = "<span>" + ' EXECUTION_COMPLETE ' + "</span>" + "<br>";
                }
                ;
            })
                .fail(function () { d.reject(this); console.log('error!'); });
            return p;
        };
        //goBack
        concreteJSTM.prototype.goBack = function () {
            var d = P.defer();
            var p = d.promise();
            var thisJSTM = this;
            $.ajax({
                url: 'goBack.goBack',
                dataType: "json",
                type: "POST",
                data: { 'myguid': thisJSTM.guid } })
                .done(function (data) {
                thisJSTM.mirrorData = data;
                var reason = thisJSTM.mirrorData.reason;
                var status = thisJSTM.mirrorData.status;
                var expression = thisJSTM.mirrorData.expression;
                /**parse the characters of the expression**/
                var expTemp = thisJSTM.ExpressionEffect(expression);
                //fuillfill
                d.resolve(thisJSTM);
                if (status != '6' && status != '7') {
                    thisJSTM.expressionDisplay.innerHTML = '';
                    thisJSTM.expressionDisplay.innerHTML = "<span>" + expTemp + "</span>";
                }
                if (status == '6') {
                    thisJSTM.expressionDisplay.innerHTML = '';
                    thisJSTM.expressionDisplay.innerHTML = "<span>" + ' EXECUTION_COMPLETE ' + "</span>" + "<br>";
                }
                if (status == '7') {
                    thisJSTM.expressionDisplay.innerHTML = '';
                    thisJSTM.expressionDisplay.innerHTML = "<span>" + ' EXECUTION_COMPLETE ' + "</span>" + "<br>";
                }
            })
                .fail(function () { d.reject(this); console.log('error!'); });
            return p;
        };
        // replace the Unicode to make some font effect.
        concreteJSTM.prototype.ExpressionEffect = function (exp) {
            exp = exp.replace(/[\uffff]/g, "<span class='tm-red'>");
            exp = exp.replace(/[\ufffe]/g, "");
            exp = exp.replace(/[\ufffd]/g, "<span class='tm-underline'>");
            exp = exp.replace(/[\ufffc]/g, "<span class='tm-blue'>");
            exp = exp.replace(/[\ufffb]/g, "</span>");
            var expEffect = 1;
            return exp;
        };
        concreteJSTM.prototype.DisplayEffect = function (chars, markUp) {
            // var PTag = document.createElement('P');
            /**
             var newChar = chars.replace(/[<]/g,'&lt');
             var newChar = newChar.replace(/[>]/g,'&gt');
             **/
            //var newChar = chars;
            var effectChars = chars;
            var effectChars = effectChars.replace(/[<]/g, '&lt');
            var effectChars = effectChars.replace(/[>]/g, '&gt');
            if (markUp == null) {
                chars = chars.replace(/[<]/g, '&lt');
                return chars;
            }
            else {
                var iterateTimes = markUp.length / 2;
                for (var i = 0; i < iterateTimes; i++) {
                    var command = markUp[i * 2].command;
                    if (command == 5) {
                        return effectChars;
                    }
                    var columnDown = markUp[i * 2].column;
                    var columnUp = markUp[i * 2 + 1].column;
                    //console.log(columnDown);
                    //console.log(columnUp);
                    var replacePart = [];
                    replacePart[i] = chars.substring(columnDown, columnUp);
                    switch (command) {
                        //command = 1 means brown. command = 3 means brown
                        case 1:
                            if (i == 0) {
                                var regexp = new RegExp(replacePart[i], "g");
                                effectChars = effectChars.replace(regexp, "<span class='tm-brown'>" + replacePart[i] + "</span>");
                                // console.log(i);
                                console.log(effectChars);
                                break;
                            }
                            else if (replacePart[i] != replacePart[i - 1]) {
                                var regexp = new RegExp(replacePart[i], "g");
                                effectChars = effectChars.replace(regexp, "<span class='tm-brown'>" + replacePart[i] + "</span>");
                                // console.log(i);
                                console.log(effectChars);
                                break;
                            }
                            break;
                        case 3:
                            if (i == 0) {
                                var regexp = new RegExp(replacePart[i], "g");
                                effectChars = effectChars.replace(regexp, "<span class='tm-brown'>" + replacePart[i] + "</span>");
                                console.log(replacePart[i]);
                                console.log(effectChars);
                                break;
                            }
                            else if (replacePart[i] != replacePart[i - 1]) {
                                var regexp = new RegExp(replacePart[i], "g");
                                effectChars = effectChars.replace(regexp, "<span class='tm-brown'>" + replacePart[i] + "</span>");
                                // console.log(i);
                                console.log(effectChars);
                                break;
                            }
                            break;
                        case 4:
                            if (i == 0) {
                                var regexp = new RegExp(replacePart[i], "g");
                                effectChars = effectChars.replace(regexp, "<span class='tm-blue'>" + replacePart[i] + "</span>");
                                // console.log(i);
                                console.log(effectChars);
                                break;
                            }
                            else if (replacePart[i] != replacePart[i - 1]) {
                                var regexp = new RegExp(replacePart[i], "g");
                                effectChars = effectChars.replace(regexp, "<span class='tm-blue'>" + replacePart[i] + "</span>");
                                // console.log(i);
                                console.log(effectChars);
                                break;
                            }
                            break;
                        /**
                        var effectChars = chars.substring(columnDown,columnUp);
                       //subString that need css effect.
                       var spanTag = document.createElement('span');
                       spanTag.innerHTML="<span class='tm.blue'>"+effectChars+"</span>";
                       PTag.appendChild(spanTag);
                       **/
                        // console.log(effectChars);
                        case 5:
                            break;
                        //command = 4 means blue
                        default:
                            break;
                    }
                }
                return effectChars;
            }
        };
        return concreteJSTM;
    })();
    jstm_1.concreteJSTM = concreteJSTM;
})(jstm || (jstm = {}));
