module jstm {
    export class MirrorState {
        private expression = "initial value from JS";
        setExpression(exp: string): void {
            this.expression = exp;
        }
        getExpression(): string {
            return this.expression;
        }
    }
}
