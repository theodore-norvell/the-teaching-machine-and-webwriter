//! Compile. Run.
package expressions;

public class BinaryOpsNoErrors {
  static public void main() {
    {
      boolean b ;
      long i ;
      long j ;
      long k ;
      i = 12 ;
      j = i ;
      k = i+j ;
      k = i-j ;
      k = i*j ;
      k = i/j ;
      k = i/7 ;
      k = i%7 ;
      k = i-j ;

      i = 0x7FFFFFFFFFFFFFFFL ;
      j = i ;
      k = i+j ;
      k = i-j-j-1-1 ;
      k = i*j*j ;
      k = i/j ;
      k = i/7 ;
      k = i%7 ;
      k = i-j ;

      i = 1 ;
      j = 4 ;
      // true
      b = i < j ;
      b = i <= j ;
      b = j > i ;
      b = j >= i ;
      b = i != j ;
      // false
      b = j < i;
      b = j <= i;
      b = i > j ;
      b = i >= j ;
      b = i == j ;

      i = 12 ;
      j = 6 ;
      k = i & j ;
      k = i | j ;
      k = i ^ j ;

      long m ;
      i = 0x8000000000000001L ;
      m = i << 4 ; //  16
      m = i << 64 ; // 0
      m = i >> 100 ;  // -1
      m = i >>> 52 ; // 4095
      i = 0x8000000000000001L ;
      m = i << 4L ; // 16
      m = i << 64L ; // Should give 0
      m = i >> 100L ; // -1
      m = i >>> 52L ; // large +ive number

      i = 4;
      j = 4;
      //true
      b = i <= j ;
      b = j <= i ;
      b = i >= j ;
      b = i >= j ;
      b = i==j ;
      b = j==i ;
      // false
      b = i < j ;
      b = j < i ;
      b = i != j ;
      b = j != i ;

      i = 0 ;
    }
    {
      boolean b ;
      short i ;
      short j ;
      int k ;
      i = 12 ;
      j = i ;
      k = i+j ;
      k = i-j-j ;
      k = i*j ;
      k = i/j ;
      k = i/7 ;
      k = i%7 ;
      k = i-j ;


      i = 0x7FFF ;
      j = i ;
      k = i+j ;
      k = i-j-j-1-1 ;
      k = i*j*j ;
      k = i/j ;
      k = i/7 ;
      k = i%7 ;
      k = i-j ;

      i = 1 ;
      j = 4 ;
      // true
      b = i < j ;
      b = i <= j ;
      b = j > i ;
      b = j >= i ;
      b = i != j ;
      // false
      b = j < i;
      b = j <= i;
      b = i > j ;
      b = i >= j ;
      b = i == j ;

      i = 12 ;
      j = 6 ;
      k = i & j ;
      k = i | j ;
      k = i ^ j ;

      int m ;
      i = 0-0x8000 ; // int converts to short
      m = i << 4 ; //  -524288.  short converts to int
      m = i << 35 ; // 0
      m = i >> 20 ;  // -1
      m = i >>> 20 ; // 4095
      i = 0-0x8000 ;
      m = i << 4L ; // -524288
      m = i << 35L ; // Should give 0
      m = i >> 20L ; // -1
      m = i >>> 20L ; // large +ive number

      i = 4;
      j = 4;
      //true
      b = i <= j ;
      b = j <= i ;
      b = i >= j ;
      b = i >= j ;
      b = i==j ;
      b = j==i ;
      // false
      b = i < j ;
      b = j < i ;
      b = i != j ;
      b = j != i ;

      i = 0 ;
    }
    {
      boolean b ;
      byte i ;
      byte j ;
      int k ;
      i = 12 ;
      j = i ;
      k = i+j ;
      k = i-j ;
      k = i*j ;
      k = i/j ;
      k = i/7 ;
      k = i%7 ;
      k = i-j ;

      i = 0x7F ;
      j = i ;
      k = i+j ;
      k = i-j-j-1-1 ;
      k = i*j*j ;
      k = i/j ;
      k = i/7 ;
      k = i%7 ;
      k = i-j ;

      i = 1 ;
      j = 4 ;
      // true
      b = i < j ;
      b = i <= j ;
      b = j > i ;
      b = j >= i ;
      b = i != j ;
      // false
      b = j < i;
      b = j <= i;
      b = i > j ;
      b = i >= j ;
      b = i == j ;

      i = 12 ;
      j = 6 ;
      k = i & j ;
      k = i | j ;
      k = i ^ j ;

      int m ;
      i = 0-128 ; // int converts to byte
      m = i << 4 ; //  -2048
      m = i << 35 ; // 0
      m = i >> 20 ;  // -1
      m = i >>> 20 ; // 4095
      i = 0-128 ;
      m = i << 4L ; // -2048
      m = i << 35L ; // Should give 0
      m = i >> 20L ; // -1
      m = i >>> 20L ; // large +ive number

      i = 4;
      j = 4;
      //true
      b = i <= j ;
      b = j <= i ;
      b = i >= j ;
      b = i >= j ;
      b = i==j ;
      b = j==i ;
      // false
      b = i < j ;
      b = j < i ;
      b = i != j ;
      b = j != i ;

      i = 0 ;
    }
    {
      boolean b ;
      int i ;
      int j ;
      i = 12 ;
      j = i ;
      i = i+j ;
      i = i-j ;
      i = i*j ;
      i = i/j ;
      i = i/7 ;
      i = i%7 ;
      i = i-j ;

      i = 1 ;
      j = 4 ;
      // true
      b = i < j ;
      b = i <= j ;
      b = j > i ;
      b = j >= i ;
      b = i != j ;
      // false
      b = j < i;
      b = j <= i;
      b = i > j ;
      b = i >= j ;
      b = i == j ;

      int k ;
      i = 12 ;
      j = 6 ;
      k = i & j ;
      k = i | j ;
      k = i ^ j ;

      i = 1 << 31 ;
      k = i >> 20 ;
      k = i >>> 20 ;
      i = 1 << 31L ;
      k = i >> 20L ;
      k = i >>> 20L ;

      i = 4;
      j = 4;
      //true
      b = i <= j ;
      b = j <= i ;
      b = i >= j ;
      b = i >= j ;
      b = i==j ;
      b = j==i ;
      // false
      b = i < j ;
      b = j < i ;
      b = i != j ;
      b = j != i ;

      i = 0 ;
    }
    {
      boolean b;
      char i ;
      char j ;
      i = 12 ;
      j = i ;
      int k ;
      k = i+j ;
      k = i-j-j-1-1 ;
      k = i*j ;
      k = i/j ;
      k = i/7 ;
      k = i%7 ;
      k = i-100 ;

      i = 0xFFFF ;
      j = i ;
      k = i+j ;
      k = i-j-j-1-1 ;
      k = i*j*j ;
      k = i/j ;
      k = i/7 ;
      k = i%7 ;
      k = i-j ;


      i = 12 ;
      j = 6 ;
      k = i & j ;
      k = i | j ;
      k = i ^ j ;

      i = '\u00F0' ; // 00F0
      k = i << 4 ;
      k = i >> 4 ;
      k = i >>> 4 ;

      i = '\u000F' << 12 ; // u000F << 12 == uF000
      k = i << 4 ; // x0F0000 = 983040
      k = i >> 4 ; // 0xF00 = 3840
      k = i >>> 4 ; // 0xF00 = 3840

      i = 1 ;
      j = 4 ;
      // true
      b = i < j ;
      b = i <= j ;
      b = j > i ;
      b = j >= i ;
      b = i != j ;
      // false
      b = j < i;
      b = j <= i;
      b = i > j ;
      b = i >= j ;
      b = i == j ;

      i = 4;
      j = 4;
      //true
      b = i <= j ;
      b = j <= i ;
      b = i >= j ;
      b = i >= j ;
      b = i==j ;
      b = j==i ;
      // false
      b = i < j ;
      b = j < i ;
      b = i != j ;
      b = j != i ;

      i = 0 ;
    }

  }
}