package library ;

public class ParseIntTest {

	private int numberOfSteps = 5;
	private int arraySize = 12;
	private int sortType = 0;

	public ParseIntTest(String args[]){
		// This tests that arguments are being passed in - as strings
		for (int i = 0; i < args.length; i++) {
			System.out.print("args[");
			System.out.print(i);
			System.out.print("] = ");
			System.out.println(args[i]);
		}

    	String arg = args[0];
    	if (args.length > 0)
            arraySize = Integer.parseInt(arg);
    	if (args.length > 1) {
    		arg = args[1];
    		numberOfSteps = Integer.parseInt(arg);
       	}
    	 if (args.length > 2) {
    		arg = args[2];
    		sortType = Integer.parseInt(arg);
    	 }
          
        System.out.println( arraySize ) ;
        System.out.println( numberOfSteps ) ;
        System.out.println( sortType ) ; 
	}
	
	public static void main(String args[]) {
		args = new String[] {"123", "42", "13" } ;
		System.out.println(Math.PI);
		ParseIntTest mt = new ParseIntTest(args);
	}
}
