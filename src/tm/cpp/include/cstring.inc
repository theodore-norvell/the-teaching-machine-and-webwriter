/*#TS*/
/* cstring.inc */

//typedef int size_t ;

char *  strcpy ( char * dest, const char * src ); 

/*size_t*/int  strlen ( const char * string ); 

char *  strcat ( char * dest, const char * src ); 

// const char * strstr ( const char * string1, const char * string2 );
char *  strstr ( const char * string1, const char * string2 ); 

int  strcmp ( const char * string1, const char * string2 ); 

/******************** Implementation ***********************/

// The implementation should be moved out of the include file

char *  strcpy ( char * dest, const char * src ) {
    char * d = dest ;
    while( *src) { *(d++) = *(src++) ; }
    *d = '\0' ;
    return dest ;
}

/*size_t*/int  strlen ( const char * string ) {
    /*size_t*/int i=0 ;
    while( string[i] ) ++i ;
    return i ;
}

char *  strcat ( char * dest, const char * src ) {
    char * d = dest ;
    while( *d ) ++d ;
    strcpy( d, src ) ;
    return dest ;
}
    
char *  strstr ( const char * string1, const char * string2 ) {
    while( *string1 ) {
        int i = 0 ;
        while( string1[i] && string2[i] && string1[i]==string2[i] ) ++i ;
        if( string2[i] == 0 ) return string1 ;
        ++string1 ; }
    return 0 ;
}

int  strcmp ( const char * string1, const char * string2 ) {
    int i=0 ;
    while( *string1 && *string2 && *string1==*string2 ) { ++string1 ; ++string2 ; }
    if( !(*string1) &&  !(*string2) ) return 0 ;
    if( !(*string1) ) return -1 ;
    if( !(*string2) ) return +1 ;
    return *string2 - *string1 ;
}