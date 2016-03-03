/// <reference path="JSTM.ts" />
/// <reference path="Promise.ts"/>
/// <reference path="../library/jquery.d.ts" />
var jstm;
(function (jstm) {
    var concreteJSTM = (function () {
        function concreteJSTM(guid) {
            this.guid = guid;
        }
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
    jstm.concreteJSTM = concreteJSTM;
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
