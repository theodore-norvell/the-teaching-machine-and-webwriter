//! Compile. Run. Expect output = "0" endl.
package expressions;

public class NullConstant01 {
  public static void main( /* String[] args */ ) {
      NullConstant01 p = null ;
      p = null ;
      if( p == null ) {
          if( null == p ) {
              if( !( p != null  ) ) {
                  if( !( null != p )) {
                      p = new NullConstant01() ;
                      if( p != null ) {
                          if( null != p ) {
                              if( !(p == null ) ) {
                                  if( !(null == p ) ) {
                                      System.out.println( 0 ) ;
                                  }
                              }
                          }
                      }
                  }
              }
          }
      }
  }
}