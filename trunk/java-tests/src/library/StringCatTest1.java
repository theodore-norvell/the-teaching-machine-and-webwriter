package library ;

public class StringCatTest1 {

	public static void main(String args[]) {
	    String hello = "Hello" ;
        String space = " " ;
        String world = "world" ;
        String helloWorld1 =  hello.concat( space ).concat( world ) ;
        System.out.print( hello ) ;
        System.out.print( space ) ;
        System.out.println( world ) ;
        System.out.println( helloWorld1 ) ;
	}
}
