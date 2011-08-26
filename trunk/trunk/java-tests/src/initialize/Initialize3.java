package initialize;

//Test initialization of a class prior to a static call
//! Run expect output = "2" endl
class Friend3 {

    static int k = 0 ;
    static {
        k = 1 ;
        }
    static int x = k + 1 ;
    static {
        System.out.println(x);
    }
    static {}
    
    static void foo() { }
}

public class Initialize3 {
  public static void main() {
      Friend3.foo() ;
      Friend3.foo() ;
      return ;
  }
}