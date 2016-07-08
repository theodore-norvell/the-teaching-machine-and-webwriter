module jstm {
    export interface SourceCoordsI  {

        getFile : () => TMFileI ;

        getLineNumber : () => number ;

        toString : () => string ; 

        equals : (other : Object ) => boolean ;
    }
}
