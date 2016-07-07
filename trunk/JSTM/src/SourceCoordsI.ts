module jstm {
    export interface SourceCoordsI  {

        getFile : () => TMFileI ;

        getLine : () => number ;

        toString : () => string ; 

        equals : (other : Object ) => boolean ;
    }
}
