/// <reference path="JSTM.ts" />
/// <reference path="MirrorState.ts" />
module jstm {
    export class GWTJSTM {
        private objState = null;
        private expDisp = null;

        init(): void {
            this.expDisp = getExpressionDisplay();// call method defined in TmGWT via JSNI
            this.objState = new MirrorState();
        }

        setMirrorState(): void {
            setMirrorState(this.expDisp, this.objState);// call method defined in TmGWT via JSNI
        }

        updateExpression(newExp: string) {
            this.objState.setExpression(newExp);
        }
        
        getExpression(): String{
        	return this.objState.getExpression();
        }
    }
}