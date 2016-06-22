/// <reference path="JSTM.ts" />
/// <reference path="MirrorState.ts" />
var jstm;
(function (jstm) {
    var GWTJSTM = (function () {
        function GWTJSTM() {
            this.objState = null;
            this.expDisp = null;
        }
        GWTJSTM.prototype.init = function () {
            this.expDisp = getExpressionDisplay(); // call method defined in TmGWT via JSNI
            this.objState = new jstm.MirrorState();
        };
        //        updateExpression(): void {
        //            refreshExpression(this.expDisp, this.objState);// call method defined in TmGWT via JSNI
        //        }
        GWTJSTM.prototype.setMirrorState = function () {
            setMirrorState(this.expDisp, this.objState); // call method defined in TmGWT via JSNI
        };
        GWTJSTM.prototype.test = function (newExp) {
            this.objState.setExpression(newExp);
        };
        return GWTJSTM;
    })();
    jstm.GWTJSTM = GWTJSTM;
})(jstm || (jstm = {}));
//# sourceMappingURL=GWTJSTM.js.map