import java.io.File;
import java.io.IOException;


public class PWD {

	public static void main(String[] args) {
		try {
			System.out.println( new File(".").getCanonicalPath() ) ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
