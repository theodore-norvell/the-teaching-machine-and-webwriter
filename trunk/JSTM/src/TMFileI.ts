module jstm {
    export interface TMFileI {

        getFileName : () => string ;

        equals : ( other : Object ) => boolean ;
    }
}
