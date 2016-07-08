module jstm {
    export interface MarkUpI {

        getColumn : () => number ;

        getCommand : () => number ;

        getTagSet : () => TagSetInterface ;
    }
}
