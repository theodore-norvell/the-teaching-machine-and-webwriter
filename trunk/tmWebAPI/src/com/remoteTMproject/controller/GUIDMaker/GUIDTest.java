package com.remoteTMproject.controller.GUIDMaker;

import tm.utilities.Debug;
import tm.utilities.StringFileSource;
import tm.utilities.TMFile;

import com.remoteTMproject.model.RTM.remoteTM;
import com.remoteTMproject.model.map.mapForRTM;

public class GUIDTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*instantiate a GUIDMaker object and call method to generate a guid*/
		GUIDMaker guid=GUIDMaker.getInstance();
		String myguid=guid.nextGUID();
		//System.out.println(myguid);
		String[] guidParts=myguid.split(":");
		//the RTM name
		String RTMname=guidParts[0];
		//THE guid number
		String guidNumber=guidParts[1];
		//test for output
		System.out.print("RTM is instantiated at this time:-------");
		System.out.println(RTMname);
		System.out.print("guid for the RTM is:-------------------");
		System.out.println(guidNumber);
		
		remoteTM tm = mapForRTM.getInstance(guidNumber);
		
		String programText = "#include <iostream>\nusing namespace std;\n\nint main(){\n    cout << 2+3/2.0 ;\n    return 0 ; }\n" ;
		StringFileSource fs = new StringFileSource() ;
		String fileName="test1";
		fs.addString( fileName, programText ) ;
		TMFile tmf = new TMFile( fs, fileName ) ;

		Debug debug = Debug.getInstance() ;
		debug.deactivate(); 

		tm.loadTMFile(CPP_LANG, tmf);

		
	}
	public static final int CPP_LANG = 1 ;
}
