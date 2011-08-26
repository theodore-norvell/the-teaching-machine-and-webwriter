package exceptions;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class NestedFinallys {
    public static void main(String[] args) throws A {
        try {
            System.out.println(0) ;
            if( x ) throw new A() ; }
        finally { // Status := A
            try {
                System.out.println(1) ; }
            finally { // Status := normal, A
                try {
                    try {
                        System.out.println(2) ;
                        if( y ) throw new B() ;
                        System.out.println("X") ; }
                    finally { // Status := B, normal, A
                        System.out.println(3) ;
                    } // Status := normal, A
                    System.out.println("Y") ; }
                catch(B e ) {
                    System.out.println(4); }
                finally { // Status := normal, normal, A
                    System.out.println(5);
                } // Status := normal, A
                System.out.println(6) ;
            } // Status := A
            System.out.println(7);
        } // Status := empty
        System.out.println("Z");
  }

  static boolean x = true ;
  static boolean y = true ;
  static class A extends Throwable { }
  static class B extends Throwable { }
}