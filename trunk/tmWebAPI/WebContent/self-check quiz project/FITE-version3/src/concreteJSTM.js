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
        //makeVariableWatcher
        concreteJSTM.prototype.makeVariableWatcher = function (Name, initValue) {
            var VariableWatcherSpan;
            VariableWatcherSpan = document.createElement('span');
            VariableWatcherSpan.innerHTML = Name;
            var VariableWatcherInput;
            VariableWatcherInput = document.createElement('input');
            VariableWatcherInput.setAttribute('type', 'text');
            VariableWatcherInput.setAttribute('value', initValue);
            this.VariableWatcher = document.createElement('div');
            this.VariableWatcher.appendChild(VariableWatcherSpan);
            this.VariableWatcher.appendChild(VariableWatcherInput);
            return this.VariableWatcher;
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
        return concreteJSTM;
    })();
    jstm_1.concreteJSTM = concreteJSTM;
})(jstm || (jstm = {}));
