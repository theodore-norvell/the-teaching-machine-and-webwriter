// Compile. Run. expect output = "36" endl "24" endl "288" endl "24" endl "0" endl;
package expressions;

public class BinaryOpsCombinedP0 {
  static public void main() {
    {
      boolean b ;
      long i ;
      long j ;
      long k ;
      i = 12 ;
      j = i ;
      k = i+j ;
      k += i;
      System.out.println(k);
      k -= i ;
      System.out.println(k);
      k *= i;
      System.out.println(k);
      k /= i;
      System.out.println(k);
      k %= i;
      System.out.println(k);

    }

  }
}