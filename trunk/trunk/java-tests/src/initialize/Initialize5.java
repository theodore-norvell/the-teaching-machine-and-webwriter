package initialize;

//Test initialization of a direct super class 
//! Run expect output = "0" endl "1" endl "3" endl

class GrandParentOfFriend5 {
    static int i = 0 ;
    static { System.out.println( i ) ; }
}
class ParentOfFriend5 extends GrandParentOfFriend5 {
    static int i = 1 ;
    static { System.out.println( i ) ; }
}

class Friend5 extends ParentOfFriend5 {
    static int k = 2;
    static {
        int j = 3 ;
        k = j ;
        System.out.println( k ) ;
    }
}

public class Initialize5 {
  
  public static void main() {
      Friend5.k = 2 ;
      Friend5.k = 2 ;
      return ;
  }
}