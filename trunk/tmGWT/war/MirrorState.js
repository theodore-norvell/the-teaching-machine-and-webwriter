var jstm;
(function (jstm) {
    var MirrorState = (function () {
        function MirrorState() {
            this.expression = "initial value from JS";
        }
        MirrorState.prototype.setExpression = function (exp) {
            this.expression = exp;
        };
        MirrorState.prototype.getExpression = function () {
            return this.expression;
        };
        return MirrorState;
    })();
    jstm.MirrorState = MirrorState;
})(jstm || (jstm = {}));
//# sourceMappingURL=MirrorState.js.map