/// <reference path="../src/JSTM.ts" />
/// <reference path="../src/Promise.ts"/>
/// <reference path="../library/jquery.d.ts" />
var jstm;
(function (jstm) {
    jstm.json = { "parameter": [{ "guid": "", "reasonFlag": "", "reason": "", "focus": "", "code": "", "status": "", "expression": "", "filename": "" }],
        "flag": [{ "expEffect": 0, "responseWantedFlag": null }] };
    var concreteJSTM = (function () {
        function concreteJSTM(name) {
            this.name = name;
        }
        //getstatus
        concreteJSTM.prototype.getStatus = function () {
            return jstm.json.parameter[0].status;
        };
        //getMessage    
        concreteJSTM.prototype.getMessage = function () {
            return jstm.json.parameter[0].reason;
        };
        //createRTM   
        concreteJSTM.prototype.createRTM = function () {
            var d = P.defer();
            var p = d.promise();
            var thisJSTM = this;
            $.ajax({
                url: 'createRemoteTM.create',
                dataType: "json",
                type: "POST"
            })
                .done(function (data) {
                var parameterTemp = data;
                //assign back-end json object to the front end parameters in this js file;
                jstm.json.parameter[0].guid = parameterTemp.guid;
                jstm.json.parameter[0].reasonFlag = parameterTemp.reasonFlag;
                jstm.json.parameter[0].reason = parameterTemp.reason;
                jstm.json.parameter[0].status = parameterTemp.status;
                //if there is no error in the createRemoteTM step
                console.log(jstm.json.parameter[0].status);
                console.log(parameterTemp);
                //fullfill the defered state
                d.resolve(thisJSTM);
            })
                .fail(function () { d.reject(this); console.log("error! in createRTM in concreteJSTM"); });
            return p;
        };
        //loadString
        concreteJSTM.prototype.loadString = function (program, filename, guid) {
            var d = P.defer();
            var p = d.promise();
            var thisJSTM = this;
            $.ajax({
                url: 'loadString.load',
                dataType: "json",
                type: "POST",
                data: { 'guid': guid,
                    'Codes': program,
                    'filename': filename }
            })
                .done(function (data) {
                var parameterTemp = data;
                //assign back-end json object to the front end parameters in this js file;
                jstm.json.parameter[0].reasonFlag = parameterTemp.reasonFlag;
                jstm.json.parameter[0].reason = parameterTemp.reason;
                jstm.json.parameter[0].status = parameterTemp.status;
                //if there is no error in the createRemoteTM step
                console.log(jstm.json.parameter[0].status);
                console.log(parameterTemp);
                if (jstm.json.parameter[0].status == '3') {
                    d.resolve(thisJSTM);
                }
                else {
                    document.getElementById('panel').style.display = 'none';
                    alert(jstm.json.parameter[0].reason);
                }
            })
                .fail(function () { d.reject(this); console.log('error! in loadString in concreteJSTM'); });
            return p;
        };
        //initialize  
        concreteJSTM.prototype.initialize = function (guid, responseWantedFlag) {
            var d = P.defer();
            var p = d.promise();
            var thisJSTM = this;
            $.ajax({
                url: 'initializeTheState.initialize',
                dataType: "json",
                type: "POST",
                data: { 'guid': guid,
                    'responseWantedFlag': responseWantedFlag } })
                .done(function (data) {
                var parameterTemp = data;
                jstm.json.parameter[0].reasonFlag = parameterTemp.reasonFlag;
                jstm.json.parameter[0].reason = parameterTemp.reason;
                jstm.json.parameter[0].status = parameterTemp.status;
                console.log(jstm.json.parameter[0].status);
                console.log(parameterTemp);
                //fuilfull
                if (jstm.json.parameter[0].status == '4') {
                    d.resolve(thisJSTM);
                }
                else {
                    document.getElementById('panel').style.display = 'none';
                    alert(jstm.json.parameter[0].reason);
                }
            })
                .fail(function (data) { d.reject(this); console.log('error! in the initialize in concreteJSTM'); });
            return p;
        };
        //go
        concreteJSTM.prototype.go = function (commandString, guid) {
            var d = P.defer();
            var p = d.promise();
            var thisJSTM = this;
            $.ajax({
                url: 'go.g',
                dataType: "json",
                type: "POST",
                data: { 'guid': guid,
                    'command': commandString } })
                .done(function (data) {
                var parameterTemp = data;
                jstm.json.parameter[0].reason = parameterTemp.reason;
                jstm.json.parameter[0].status = parameterTemp.status;
                jstm.json.parameter[0].expression = parameterTemp.expression;
                /**parse the characters of the expression**/
                var expTemp = ExpressionEffect(jstm.json.parameter[0].expression);
                //fuilfull
                d.resolve(thisJSTM);
                if (jstm.json.parameter[0].status != '6' && jstm.json.parameter[0].status != '7') {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<p>" + expTemp + "</p>");
                }
                if (jstm.json.parameter[0].status == '6') {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<font color='black'>" + jstm.json.parameter[0].status + "</font>" + "<br>");
                }
                if (jstm.json.parameter[0].status == '7') {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<font color='black'>" + jstm.json.parameter[0].status + "</font>" + "<br>");
                }
            })
                .fail(function (data) { d.reject(this); console.log('error! in the go in concreteJSTM'); });
            return p;
        };
        //goForward
        concreteJSTM.prototype.goForward = function (guid) {
            var d = P.defer();
            var p = d.promise();
            var thisJSTM = this;
            $.ajax({
                url: 'goForward.goForward',
                dataType: "json",
                type: "POST",
                data: { 'myguid': guid } })
                .done(function (data) {
                var parameterTemp = data;
                jstm.json.parameter[0].reason = parameterTemp.reason;
                jstm.json.parameter[0].status = parameterTemp.status;
                jstm.json.parameter[0].expression = parameterTemp.expression;
                /**parse the characters of the expression**/
                var expTemp = ExpressionEffect(jstm.json.parameter[0].expression);
                //fuilfill
                d.resolve(thisJSTM);
                if (jstm.json.parameter[0].status != '6' && jstm.json.parameter[0].status != '7') {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<span>" + expTemp + "</span>");
                }
                if (jstm.json.parameter[0].status == '6') {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<span>" + jstm.json.parameter[0].status + "</span>" + "<br>");
                }
                if (jstm.json.parameter[0].status == '7') {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<span>" + jstm.json.parameter[0].reason + "</span>" + "<br>");
                }
                ;
            })
                .fail(function () { d.reject(this); console.log('error!'); });
            return p;
        };
        //goBack
        concreteJSTM.prototype.goBack = function (guid) {
            var d = P.defer();
            var p = d.promise();
            var thisJSTM = this;
            $.ajax({
                url: 'goBack.goBack',
                dataType: "json",
                type: "POST",
                data: { 'myguid': guid } })
                .done(function (data) {
                var parameterTemp = data;
                jstm.json.parameter[0].reason = parameterTemp.reason;
                jstm.json.parameter[0].status = parameterTemp.status;
                jstm.json.parameter[0].expression = parameterTemp.expression;
                /**parse the characters of the expression**/
                var expTemp = ExpressionEffect(jstm.json.parameter[0].expression);
                //fuillfill
                d.resolve(thisJSTM);
                if (jstm.json.parameter[0].status != '6' && jstm.json.parameter[0].status != '7') {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<span>" + expTemp + "</span>");
                }
                if (jstm.json.parameter[0].status == '6') {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<font color='black'>" + jstm.json.parameter[0].status + "</font>" + "<br>");
                }
                if (jstm.json.parameter[0].status == '7') {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<font color='black'>" + jstm.json.parameter[0].reason + "</font>" + "<br>");
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
        jstm.json.flag[0].expEffect = 1;
        return exp;
    }
})(jstm || (jstm = {}));
