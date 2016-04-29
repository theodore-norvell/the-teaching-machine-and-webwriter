var jstm;
(function(jstm) {
	var MirrorState = (function() {
		function MirrorState() {
			this.expression = "original value";
		}
		// MirrorState.prototype.setExpression = function (exp) {
		// this.expression = exp;
		// };

		MirrorState.prototype.setExpression = function(k) {
			switch (k) {
			case 0:
				this.expression = "";
				break;
			case 1:
				this.expression = "\ufffetempF\ufffb = (tempC * 5 / 9) + 32 ";
				break;
			case 2:
				this.expression = "\ufffctempF\ufffb = (\ufffetempC\ufffb * 5 / 9) + 32 ";
				break;
			case 3:
				this.expression = "\ufffctempF\ufffb = (\ufffe\ufffctempC\ufffb\ufffb * 5 / 9) + 32 ";
				break;
			case 4:
				this.expression = "\ufffctempF\ufffb = (\uffff10\ufffb * \ufffe5\ufffb / 9) + 32 ";
				break;
			case 5:
				this.expression = "\ufffctempF\ufffb = (\ufffe\uffff10\ufffb * \uffff5.0\ufffb\ufffb / 9) + 32 ";
				break;
			case 6:
				this.expression = "\ufffe\ufffctempF\ufffb = \uffff50.0\ufffb\ufffb";
				break;
			case 7:
				this.expression = "\uffff50.0\ufffb";
				break;
			}
		};
		MirrorState.prototype.getExpression = function() {
			var _this = this;
			// console.log("getExpression" + this.k);
			// var exp = null;
			// switch (this.k) {
			// case 0:
			// exp = "";
			// break;
			// case 1:
			// exp = "\ufffetempF\ufffb = (tempC * 5 / 9) + 32 ";
			// break;
			// case 2:
			// exp = "\ufffctempF\ufffb = (\ufffetempC\ufffb * 5 / 9) + 32 ";
			// break;
			// case 3:
			// exp = "\ufffctempF\ufffb = (\ufffe\ufffctempC\ufffb\ufffb * 5 /
			// 9) + 32 ";
			// break;
			// case 4:
			// exp = "\ufffctempF\ufffb = (\uffff10\ufffb * \ufffe5\ufffb / 9) +
			// 32 ";
			// break;
			// case 5:
			// exp = "\ufffctempF\ufffb = (\ufffe\uffff10\ufffb *
			// \uffff5.0\ufffb\ufffb / 9) + 32 ";
			// break;
			// case 6:
			// exp = "\ufffe\ufffctempF\ufffb = \uffff50.0\ufffb\ufffb";
			// break;
			// case 7:
			// exp = "\uffff50.0\ufffb";
			// break;
			// }
			// this.k = (this.k + 1) % this.numStates;
			return this.expression;
		};
		return MirrorState;
	})();
	jstm.MirrorState = MirrorState;
})(jstm || (jstm = {}));
// # sourceMappingURL=MirrorState.js.map
