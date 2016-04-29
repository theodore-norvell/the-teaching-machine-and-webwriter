var jstm;
(function (jstm) {
    var JSState = (function () {
        function JSState() {
            this.expression = "";
        }
        JSState.prototype.setExpression = function (exp) {
            this.expression = exp;
        };
        JSState.prototype.getExpression = function () {
            return this.expression;
        };
        return JSState;
    })();
    jstm.JSState = JSState;
})(jstm || (jstm = {}));
//# sourceMappingURL=JSState.js.map