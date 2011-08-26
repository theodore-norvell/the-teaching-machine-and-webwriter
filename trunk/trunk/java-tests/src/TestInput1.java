//! run with input = "ab"
//! expect output "97" endl "98" endl "-1" endl "-1" endl

import java.io.PrintStream ;
import java.io.InputStream ;
import java.io.IOException ;

public class TestInput1 {

  public static void main(/*String[] args*/) {
      try {
          PrintStream out = System.out ;
          InputStream in = System.in ;
          int i = in.read() ;
          out.println( i ) ;
          i = in.read() ;
          out.println( i ) ;
          i = in.read() ;
          out.println( i ) ;
          i = in.read() ;
          out.println( i ) ; }
      catch( IOException ex ) {}
     
  }
}