//! Compile. Run.

package expressions;

public class BinaryNumericPromotions {
  public static void main() {
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
    double d ;
    d = 12.123e50 ;
    float f ;
    f = -0.9999e-10f ;

    // Long
    long k ;
    k = lng + i ;
    k = i + lng ;
    k = lng + s ;
    k = s + lng ;
    k = lng + c ;
    k = c + lng ;
    k = lng + b ;
    k = b + lng ;
    //k = lng + q ;
    //k = q + lng ;
    double e ;
//    e = lng + d ;
//    e = d + lng ;
//    e = lng + f ;
//    e = f + lng ;
//
//    // INT
//    k = i + s ;
//    k = s + i ;
//    k = i + c ;
//    k = c + i ;
//    k = i + b ;
//    k = b + i ;
//    // k = i + q ;
//    // k = q + i ;
//    e = i + d ;
//    e = d + i ;
//    e = i + f ;
//    e = f + i ;
//
//    // SHORT
//    k = s + c ;
//    k = c + s ;
//    k = s + b ;
//    k = b + s ;
//    //k = s + q ;
//    //k = q + s ;
//    e = s + d ;
//    e = d + s ;
//    e = s + f ;
//    e = f + s ;
//
//    // CHAR
//    k = c + b ;
//    k = b + c ;
//    //k = c + q ;
//    //k = q + c ;
//    e = c + d ;
//    e = d + c ;
//    e = c + f ;
//    e = f + c ;
//
//    // BYTE
//    //k = b + q ;
//    //k = q + b ;
//    e = b + d ;
//    e = d + b ;
//    e = b + f ;
//    e = f + b ;
//
//    // BOOLEAN ;
    // k = q + d ;
    // k = d + q ;
    // k = q + e ;
    // k = e + q ;
    // boolean p ;
    // p = q==lng ;
    // p = lng==q ;
//
//      // DOUBLE
//      e = d + f ;
//      e = f + d ;
  }
}