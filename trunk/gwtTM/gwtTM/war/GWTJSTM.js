/// <reference path="JSState.ts" />
/// <reference path="MirrorState.ts" />
var jstm;
(function (jstm) {
    var GWTJSTM = (function () {
        function GWTJSTM() {
            this.objState = null;
            this.expDisp = null;
        }
        GWTJSTM.prototype.createState = function () {
            if (this.objState == null)
                this.objState = new jstm.MirrorState();
            //                this.objState = new JSState();
        };
      //to get GWT Canvas for expression engine
        GWTJSTM.prototype.makeExpressionDisplay = function () {
            var expDisp = getCanvasElement();
            return expDisp;
        };
        
        GWTJSTM.prototype.makeGoForwardButton = function () {
            var _this = this;
            var button = document.createElement("button");
            button.setAttribute("class", "tm-button");
            button.onclick = function () { return goForward(); };
            button.innerHTML = "-&gt;";
            return button;
        };
        GWTJSTM.prototype.makeGoBackButton = function () {
            var _this = this;
            var button = document.createElement("button");
            button.setAttribute("class", "tm-button");
            button.onclick = function () { return backForward(); };
            button.innerHTML = "&lt;-";
            return button;
        };
        
        GWTJSTM.prototype.getState = function () {
            if (this.objState == null)
                this.createState();
            return this.objState;
        };
        
        GWTJSTM.prototype.getExpDisObj = function () {
            if (this.expDisp == null)
                this.newExpressionDisplay();
            return this.expDisp;
        };
        
        //call Java code to refresh the page
        GWTJSTM.prototype.refresh = function () {
            var x = refresh(this.getExpDisObj(), this.getState()); // call method defined in GWT via JSNI
            return x;
        };
        GWTJSTM.prototype.newExpressionDisplay = function () {
            this.expDisp = getExpressionDisplay();
        };
        return GWTJSTM;
    })();
    jstm.GWTJSTM = GWTJSTM;
})(jstm || (jstm = {}));
//# sourceMappingURL=GWTJSTM.js.map