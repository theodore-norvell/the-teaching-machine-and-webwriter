/*#TS*/
/*
 * Created on 21-Jul-2008
 * Library classes act as cutout relays. The TM treats them as regular libraries.
 * The declarations are used by the TM compilers to check for correctness
 * but the runtime engine won't step into the library implementation code.
 * The native keyword simply says that at runtime the methods are implemented in java.
 * The TM calls the methods to process the data but won't let their workings be examined.
 *
 */

/**
 * @author mpbl
 *
 */
class ScriptManager {

/* relayCall can in fact have an number of arguments following the first two. Internally
they will be treated as Datums and will have to conform to whatever expectations of them
the target method may have. This relaxes the normal compiler type checking but
will result in a run time error if a target method cannot be matched up with
its arguments */	
	public:
	 static void relay(char* configId, char* call, ...);
	 static int relayRtnInt(char* configId, char* call, ...);
	 static double relayRtnDouble(char* configId, char* call, ...);
	
/* Again, the datums to be compared are not specified for the compiler. There must be two of
them and they should both be descendents of Datum subtypes which can be compared.
Currently removed as it requires another compiler exception
	static boolean compareDatums(char* configId); */
		
	static void snapShot(char*  configId, char*  portholeId);
	static void autoRun();
	static void showTM(bool visible);
	static bool isTMShowing();	
	static void stopAuto();
} ;

class HG {
public:
	static int assertNode(/*AnyTypeAtAll node*/);
	static void addChild(/*AnyTypeAtAll node, AnyTypeAtAll parent*/);
	static void assertEdge (/*AnyTypeAtAll startNode, AnyTypeAtAll endNode*/);
	static void switchView(long view);
	static int getViewNum();
	static void setDefaultNodeColor(long c);
	static void setDefaultEdgeColor(long c);
	static void setDefaultBranchColor(long c);
	static void setNodeColor(/* Datum d,*/ long c);
	static void setNodeShape(/* Datum d,*/ long s);
	static void setNodeStroke(/* Datum d,*/ long s);
	static void setEdgeColour(/* Datum d,*/ long c);
	static void setEdgeShape(/* Datum d,*/ long s);
	static void setEdgeStroke(/* Datum d,*/ long s);
	static void setBranchColour(/* Datum d,*/ long c);
	static void setBranchShape(/* Datum d,*/ long s);
	static void setBranchStroke(/* Datum d,*/ long s);
};
