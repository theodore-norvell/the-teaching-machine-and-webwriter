/// <reference path="JSTM.ts" />
var jstm;
(function (jstm) {
    var MockJSTM = (function () {
        function MockJSTM() {
            this.expressionDisplays = new Array();
            this.varVals_tempC = new Array();
            this.varVals_tempF = new Array();
            this.numStates = 8;
            this.k = 0;
            
            this.globalExp = null;
            this.globalTempC = null;
            this.globalTempF = null;
        }
        MockJSTM.prototype.expressionStringToHTML = function (str) {
            var i;
            var html = "";
            for (i = 0; i < str.length; ++i) {
                var c = str.charAt(i);
                switch (c) {
                    case "<":
                        html += "&lt;";
                        break;
                    case ">":
                        html += "&gt;";
                        break;
                    case "&":
                        html += "&amp;";
                        break;
                    case "\uffff":
                        html += '<span class="tm-red">';
                        break;
                    case "\ufffe":
                        html += '<span class="tm-underline">';
                        break;
                    case "\ufffc":
                        html += '<span class="tm-blue">';
                        break;
                    case "\ufffb":
                        html += '</span>';
                        break;
                    default: html += c;
                }
            }
            return html;
        };
        
        MockJSTM.prototype.updateGlobalVariables = function (i, exp) {
            switch (i) {
                case 0:
                    this.globalExp = exp;
                    break;
                case 1:
                    this.globalTempC = exp;
                    break;
                case 2:
                    this.globalTempF = exp;
                    break;
            }
        };
        MockJSTM.prototype.getCState = function () {
            return this.k;
        };
        
        MockJSTM.prototype.updateDisplays = function () {
        	var _this = this;
        	updateStateValue();//send request to server which update state value based on current state
            console.log("updateDisplays, k is " + this.k);
            if (this.globalExp != null) {
                this.expressionDisplays.forEach(function (d) {
                    return d.innerHTML = _this.expressionStringToHTML(_this.globalExp);
                });
            }
            if (this.globalTempC != null) {
                this.varVals_tempC.forEach(function (d) {
                    return d.innerHTML = _this.globalTempC;
                });
            }
            if (this.globalTempF != null) {
                this.varVals_tempF.forEach(function (d) {
                    return d.innerHTML = _this.globalTempF;
                });
            }
        };
        MockJSTM.prototype.makeExpressionDisplay = function () {
            var expDisp = makeDivElement();//document.createElement("div");
//            expDisp.setAttribute("class", "tm-expression-display");
            this.expressionDisplays.push(expDisp);
            return expDisp;
        };
        MockJSTM.prototype.makeGoForwardButton = function () {
            var _this = this;
            var button = makeButton("");//document.createElement("button");
            button.setAttribute("class", "tm-button");
            button.onclick = function () { return _this.goForward(); };
            button.innerHTML = "-&gt;";
            return button;
        };
        MockJSTM.prototype.makeGoBackButton = function () {
            var _this = this;
            var button = makeButton("");//document.createElement("button");
            button.setAttribute("class", "tm-button");
            button.onclick = function () { return _this.goBack(); };
            button.innerHTML = "&lt;-";
            return button;
        };
        MockJSTM.prototype.makeVariableWatcher = function (varName) {
            var varWatcher = document.createElement("span");
            varWatcher.setAttribute("class", "tm-var-watcher");
            var text = document.createTextNode(varName + " : ");
            var value = document.createElement("span");
            value.setAttribute("class", "tm-var-value");
            value.innerHTML = "";
            varWatcher.appendChild(text);
            varWatcher.appendChild(value);
            if (varName === "tempC") {
                this.varVals_tempC.push(value);
            }
            else if (varName === "tempF") {
                this.varVals_tempF.push(value);
            }
            return varWatcher;
        };
        MockJSTM.prototype.loadString = function (program, language) {
        };
        MockJSTM.prototype.getStatus = function () {
            return 3;
        };
        MockJSTM.prototype.go = function (commandString) {
        };
        MockJSTM.prototype.goForward = function () {
            this.k = (this.k + 1) % this.numStates;
            this.updateDisplays();
        };
        MockJSTM.prototype.goBack = function () {
            this.k = (this.k + this.numStates - 1) % this.numStates;
            this.updateDisplays();
        };
        return MockJSTM;
    })();
    jstm.MockJSTM = MockJSTM;
})(jstm || (jstm = {}));
//# sourceMappingURL=MockJSTM.js.map