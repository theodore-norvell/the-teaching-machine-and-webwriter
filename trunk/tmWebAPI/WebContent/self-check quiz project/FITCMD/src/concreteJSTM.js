/// <reference path="JSTM.ts" />
/// <reference path="Promise.ts"/>
/// <reference path="../builder/quizBuilder.ts"/>
/// <reference path="../library/jquery.d.ts" />
var jstm;
(function (jstm_1) {
    var concreteJSTM = (function () {
        // qz:quizBuilder.Quiz;
        function concreteJSTM(guid) {
            //<span> element for variables
            this.inputVarsSpanElement = new Array();
            //<input> element for variables
            this.inputVarsInputElement = new Array();
            this.guid = guid;
        }
        //
        concreteJSTM.prototype.makeGoForwardButton = function (onDone, onFail) {
            this.goForwardButton = document.getElementById('goFoward');
            return this.goForwardButton;
        };
        concreteJSTM.prototype.makeGoBackButton = function (onDone, onFail) {
            this.goBackButton = document.getElementById('goBack');
            return this.goBackButton;
        };
        //------------------------------------------------
        // i will receive the inputVars object and return inputVarsSpan;
        concreteJSTM.prototype.setquestionDisplay = function (tempIndex) {
            this.questionDisplay = document.getElementById('questionDisplay');
            this.questionDisplay.innerHTML = tempIndex.name + " : " + tempIndex.text;
            this.functiondescription = document.getElementById('functionDisplay');
            this.functiondescription.innerHTML = 'implement the function  ' + tempIndex.functiondescription;
        };
        concreteJSTM.prototype.setInputExpressionSpanElement = function () {
            this.inputExpressionSpanElement = document.createElement('span');
            this.inputExpressionSpanElement.innerHTML = 'implement the function: ';
            return this.inputExpressionSpanElement;
        };
        concreteJSTM.prototype.setinputExpressionInputElement = function () {
            this.inputExpressionInputElement = document.createElement('input');
            this.inputExpressionInputElement.setAttribute('type', 'text');
            this.inputExpressionInputElement.setAttribute('value', '');
            return this.inputExpressionInputElement;
        };
        concreteJSTM.prototype.setinputVarsSpanElement = function (inputVars) {
            // console.log(inputVars);
            var count = Object.keys(inputVars).length;
            for (var i = 0; i < count; i++) {
                this.inputVarsSpanElement[i] = document.createElement('span');
                //this.inputVarsElement[i].setAttribute('id','Z1');
                this.inputVarsSpanElement[i].innerHTML = "Initial Value of :" + inputVars[i].name;
            }
            return this.inputVarsSpanElement;
        };
        //i will receive the inputVars object and return inputVarsInputElement;
        concreteJSTM.prototype.setinputVarsinputElement = function (inputVars) {
            var count = Object.keys(inputVars).length;
            for (var i = 0; i < count; i++) {
                this.inputVarsInputElement[i] = document.createElement('input');
                //this.inputVarsSpanElement[i].setAttribute('id','Z2');
                this.inputVarsInputElement[i].setAttribute('type', 'text');
                this.inputVarsInputElement[i].setAttribute('value', inputVars[i].defaultInitValue);
            }
            return this.inputVarsInputElement;
        };
        //create the html element
        concreteJSTM.prototype.buildInputElement = function () {
            //console.log(tempIndex.inputVars);
            this.setquestionDisplay(quizBuilder.Quiz.tempIndex);
            this.setInputExpressionSpanElement();
            this.setinputExpressionInputElement();
            this.setinputVarsSpanElement(quizBuilder.Quiz.tempIndex.inputVars);
            this.setinputVarsinputElement(quizBuilder.Quiz.tempIndex.inputVars);
        };
        concreteJSTM.prototype.buildOutputElement = function () {
            this.makeGoForwardButton();
            this.makeGoBackButton();
        };
        concreteJSTM.prototype.constructHTMLElement = function () {
            this.buildInputElement();
            this.buildOutputElement();
        };
        //constructDisplay area1!!!!
        // div parameter 
        concreteJSTM.prototype.constructInsertArea1 = function () {
            $('#insert1').empty();
            $('#insert1').append(this.inputExpressionSpanElement);
            $('#insert1').append(this.inputExpressionInputElement);
            $('#insert1').append('<br></br>');
            for (var j = 0; j < this.inputVarsInputElement.length; j++) {
                $('#insert1').append(this.inputVarsSpanElement[j]);
                $('#insert1').append(this.inputVarsInputElement[j]);
                $('#insert1').append("<br></br>");
            }
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
                var parameterTemp = data;
                //assign back-end json object to the front end parameters in this js file;
                var guid = parameterTemp.guid;
                var reasonFlag = parameterTemp.reasonFlag;
                var reason = parameterTemp.reason;
                var status = parameterTemp.status;
                //if there is no error in the createRemoteTM step
                console.log(status);
                console.log(parameterTemp);
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
                var parameterTemp = data;
                //assign back-end json object to the front end parameters in this js file;
                var reasonFlag = parameterTemp.reasonFlag;
                var reason = parameterTemp.reason;
                var status = parameterTemp.status;
                //if there is no error in the createRemoteTM step
                thisJSTM.setStatus(status);
                console.log(thisJSTM.getStatus());
                console.log(parameterTemp);
                if (status == '3') {
                    d.resolve(thisJSTM);
                }
                else {
                    document.getElementById('panel').style.display = 'none';
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
                var parameterTemp = data;
                var reasonFlag = parameterTemp.reasonFlag;
                var reason = parameterTemp.reason;
                var status = parameterTemp.status;
                thisJSTM.setStatus(status);
                console.log(thisJSTM.getStatus());
                console.log(parameterTemp);
                //fuilfull
                if (status == '4') {
                    d.resolve(thisJSTM);
                }
                else {
                    document.getElementById('panel').style.display = 'none';
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
                var parameterTemp = data;
                var reason = parameterTemp.reason;
                var status = parameterTemp.status;
                var expression = parameterTemp.expression;
                /**parse the characters of the expression**/
                var expTemp = ExpressionEffect(expression);
                //fuilfull
                d.resolve(thisJSTM);
                if (status != '6' && status != '7') {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<p>" + expTemp + "</p>");
                }
                if (status == '6') {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<font color='black'>" + ' EXECUTION_COMPLETE ' + "</font>" + "<br>");
                }
                if (status == '7') {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<font color='black'>" + ' EXECUTION_FAILED ' + "</font>" + "<br>");
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
                var parameterTemp = data;
                var reason = parameterTemp.reason;
                var status = parameterTemp.status;
                var expression = parameterTemp.expression;
                /**parse the characters of the expression**/
                var expTemp = ExpressionEffect(expression);
                //fuilfill
                d.resolve(thisJSTM);
                if (status != '6' && status != '7') {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<span>" + expTemp + "</span>");
                }
                if (status == '6') {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<span>" + ' EXECUTION_COMPLETE ' + "</span>" + "<br>");
                }
                if (status == '7') {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<span>" + ' EXECUTION_FAILED ' + "</span>" + "<br>");
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
                var parameterTemp = data;
                var reason = parameterTemp.reason;
                var status = parameterTemp.status;
                var expression = parameterTemp.expression;
                /**parse the characters of the expression**/
                var expTemp = ExpressionEffect(expression);
                //fuillfill
                d.resolve(thisJSTM);
                if (status != '6' && status != '7') {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<span>" + expTemp + "</span>");
                }
                if (status == '6') {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<font color='black'>" + ' EXECUTION_COMPLETE ' + "</font>" + "<br>");
                }
                if (status == '7') {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<font color='black'>" + ' EXECUTION_FAILED ' + "</font>" + "<br>");
                }
            })
                .fail(function () { d.reject(this); console.log('error!'); });
            return p;
        };
        return concreteJSTM;
    })();
    jstm_1.concreteJSTM = concreteJSTM;
    function ExpressionEffect(exp) {
        exp = exp.replace(/[\uffff]/g, "<span class='tm-red'>");
        exp = exp.replace(/[\ufffe]/g, "");
        exp = exp.replace(/[\ufffd]/g, "<span class='tm-underline'>");
        exp = exp.replace(/[\ufffc]/g, "<span class='tm-blue'>");
        exp = exp.replace(/[\ufffb]/g, "</span>");
        var expEffect = 1;
        return exp;
    }
})(jstm || (jstm = {}));
