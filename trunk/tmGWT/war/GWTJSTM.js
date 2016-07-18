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
        GWTJSTM.prototype.setMirrorState = function () {
            setMirrorState(this.expDisp, this.objState); // call method defined in TmGWT via JSNI
        };
        GWTJSTM.prototype.updateExpression = function (newExp) {
            this.objState.setExpression(newExp);
        };
        GWTJSTM.prototype.getExpression = function () {
            return this.objState.getExpression();
        };
        return GWTJSTM;
    })();
    jstm.GWTJSTM = GWTJSTM;
})(jstm || (jstm = {}));
//# sourceMappingURL=GWTJSTM.js.map