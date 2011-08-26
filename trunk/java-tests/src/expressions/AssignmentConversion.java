//! Compile. Run.
package expressions;

public class AssignmentConversion {
  public static void main() {
    double d ;
    d = 12.123e50 ;
    float f ;
    f = -0.9999e-10f ;
    long lng  ;
    lng = -1L ;
    int i ;
    i = 99 ;
    short s ;
    s = -55 ;
    char c ;
    c = 'a' ;
    byte b ;
    b = -128 ;
    boolean q ;
    q = true ;

    // Double
    d = d ;
    d = f ;
    d = lng ;
    d = i ;
    d = s ;
    d = c ;
    d = b ;
    //d = q ; //Error

    // Float
    //f = d ; //Error
    f = f ;
    f = lng ;
    f = i ;
    f = s ;
    f = c ;
    f = b ;
    //f = q ; //Error

    // Long
    //lng = d ; //Error
    //lng = f ;  // Error
    lng = lng ;
    lng = i ;
    lng = s ;
    lng = c ;
    lng = b ;
    //lng = q ; //Error

    // Int
    //i = d ; //Error
    //i = f ;  // Error
    //i = lng ; // Error
    i = i ;
    i = s ;
    i = c ;
    i = b ;
    //i = q ; //Error

    // Short
    //s = d ; //Error
    //s = f ;  // Error
    //s = lng ; // Error
    //s = i ; // Error
    s = s ;
    //s = c ; // Error
    s = b ;
    //s = q ; //Error
    s =   0x7FFF ; // Special case
    s = - 0x8000 ; // Special case (Ignore JBulder's error indicator )
    //s =  0x10000 ; // Error. Constant too big
    //s = -0x80001 ; // Error. Constant too small

    // Char
    //s = d ; //Error
    //c = f ;  // Error
    //c = lng ; // Error
    //c = i ; // Error
    //c = s ; // Error
    c = c ;
    //c = b ; // Error
    //c = q ; //Error
    c = 0 ; // Special case
    c = 0xFFFF ; // Special case ;
    //c = -1 ;  // Error, too small ;
    //c = 0x10000 ; // Error too big ;

    // Byte
    //b = d ; //Error
    //b = f ;  // Error
    //b = lng ; // Error
    //b = i ; // Error
    //b = s ; // Error
    //b = c ; // Error
    b = b ;
    //b = q ; //Error
    b = -128 ; // Special case
    b = 127 ;  // Special case
    //b = -129 ; // Error.  Too small
    //b = 128 ; // Error.  Too big

    // Boolean
    //q = d ; //Error
    //q = f ;  // Error
    //q = lng ; // Error
    //q = i ; // Error
    //q = s ; // Error
    //q = c ; // Error
    //q = b ; // Error
    q = q ;


    i = 0 ;
  }
}