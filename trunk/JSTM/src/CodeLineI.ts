module jstm {
    export interface CodeLineI {

        getChars() : Array<char> ;

        getCoords : () => SourceCoordsI ;

        markUp : () => Array<MarkUpI>;
    }
}
