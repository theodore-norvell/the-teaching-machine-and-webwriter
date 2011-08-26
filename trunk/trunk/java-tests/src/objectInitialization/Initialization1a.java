//! Run. Expect output "".

//Initialization with default constructor.

package objectInitialization;

public class Initialization1a {
 int i = 98 ;
 
 Initialization1a() {
     i = i+1 ;
 }

 public static void main(/*String[] args*/) {
     Initialization1a p = new Initialization1a() ;
 }
}
