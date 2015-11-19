//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

package tm.scripting;

import java.awt.Image;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;

import tm.clc.ast.Clc_ASTUtilities;
import tm.clc.ast.ExpressionNode;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractFloatDatum;
import tm.clc.datum.AbstractIntDatum;
import tm.cpp.datum.BoolDatum;
import tm.interfaces.CommandInterface;
import tm.interfaces.Datum;
import tm.interfaces.RegionInterface;
import tm.interfaces.Scriptable;
import tm.javaLang.datum.BooleanDatum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;
/**
* <h2> Overview:</h2>
* <p>The ScriptManager is a <em>Singleton</em> class which handles 
* <em>internal</em> scripting calls, that is scripting calls that
* originate from within the code being run on the Teaching Machine.
* </p>
* 
* @author mpbl
*/



public class ScriptManager implements Scriptable{
	static ScriptManager theManager;
	Vector<Scriptable> registrees;
	static CommandInterface cp = null;
	static final String NO_CP = "TMBigApplet has not registered with the ScriptManager";
	private static int lastImageWidth ;
	private static int lastImageHeight ;
	
/*	private class CachedMethod{
		Method myMethod;
		Scriptable myComponent;
		String methodName;
		Class<?>[] argClasses;
		int argMask;
		int myArgs;
		CachedMethod(Method m, Scriptable c, Class<?>[] aClasses, int aMask, int a){
			myMethod = m;
			myComponent = c;
			argMask = aMask;
			myArgs = a;
		}
	};*/
	
	private Hashtable<String,Object> methodCache = new Hashtable<String,Object>() ;
	
	private ScriptManager(){
	}
		
	/**
	 * Get a reference to the ScriptManager
	 * 
	 * @return a reference to the singleton ScriptManager
	 */
	public static ScriptManager getManager(){
		if (theManager == null){
			theManager = new ScriptManager();
			theManager.registrees = new Vector<Scriptable>();
		}
		return theManager;
	}
	
	/**
	 * clears the list of registrees
	 * 
	 */
	public void reset(){
		registrees.clear();
		methodCache.clear();
	}
	
	/**
	 * register a Scriptable component with the ScriptManager
	 * 
	 */
	public void register(Scriptable comp){
		// Ensure there is no already one with the same name.
		String id = comp.getId() ;
		Scriptable old = find(id) ;
		if( old != null ) deRegister(old) ;
		
		// Add this one.
		registrees.add(comp);
		
//		System.out.println("ScriptManager registering " + comp.getId());
		if (comp.getId().equals("TMBigApplet"))
			cp = (CommandInterface)comp;
	}
	
	/**
	 * deregister a Scriptable component from the ScriptManager
	 * 
	 */
	public void deRegister(Scriptable comp){
		registrees.remove(comp);
//		System.out.println("ScriptManager deRegistering " + comp.getId());
		if (comp.getId().equals("TMBigApplet"))
			cp = null;
	}

	
	/**
	 * register a Scriptable component with the ScriptManager
	 * 
	 * @param id - a String with a unique id for the component
	 * @return the Scriptable component having the specified id
	 *           or null if not found
	 */
	public Scriptable find(String id){
		for (int i = 0; i < registrees.size(); i++)
			if (registrees.elementAt(i).getId().equals(id))
				return registrees.elementAt(i);
		return null;
	}
	
	   

/*	public static void startAnimation(){
		System.out.println("start animation");		
	}
*/
	
	/******** internal scripting *********************************************************
	 *  These are methods for internal scripting. To be made available from within code
	 * being run by the Teaching Machine they must be declared in the tm.scripting library
	 * found in the JavaLang package and they must be handled by the stepper system (which
	 * sends the call on to the ScriptManager)
	 ***************************************************************************************/
	
	/** relay a call to a Scriptable component.
	 * <p>A version of relayCall that returns an int.
	 * See usage note on relayCall</p>
	 * 
	 * @param nd
	 * @param vms
	 * @return returns an int to either Java or C++ code
	 */
	public int relayCallRtnInt(ExpressionNode nd, VMState vms){
        Object object = relayCall(nd,vms);
        Assert.check(object != null && object instanceof Integer,
        		"RelayRtnInt not returning integer");
        return ((Integer)object).intValue();
	}
	
	/** relay a call to a Scriptable component.
	 * <p>A version of relayCall that returns an double.
	 * See usage note on relayCall</p>
	 * 
	 * @param nd
	 * @param vms
	 * @return returns a double to either Java or C++ code
	 */
	public double relayCallRtnDouble(ExpressionNode nd, VMState vms){
        Object object = relayCall(nd,vms);
        Assert.check(object != null && object instanceof Double,
        		"RelayRtnDouble not returning double");
        return ((Double)object).doubleValue();
	}
	
	/** relay a call to a Scriptable component.
	 * <p><b>Usage Note</b> relayCall is invoked from within the
	 * code being run on the TeachingMachine as follows:</br>
	 * <code>ScriptManager.relayCall(plugInName, methodName, arg1, ...)</code></br>
	 * where <code>plugInName</code> and <code>methodName</code> are strings defining
	 * <ul><li> the name of the plugIn to which the call is to be relayed</li>
	 * <li>the name of the method in the plugIn being called</li></ul>
	 * any number of arguments may be given.</p>
	 * <p>Note that all arguments will actually be Datums. For example
	 * a C++ int will be a tm.cpp.IntDatum and a java int will be a
	 * tm.javaLang.IntDatum. However, both inherit from and can be treated
	 * as tm.clc.AbstractIntDatum and so can be handled in a language
	 * independent way.
	 * 
	 * @param nd
	 * @param vms
	 * @return while inside the TM Java this returns an object, from the point
	 *         of view of the example code being run on the TM it is a void
	 *         (ie no return).
	 * */
	public Object relayCall(ExpressionNode nd, VMState vms){
		Assert.check(cp != null, NO_CP);
		// This is read only. the scripting stepper handles
		// adjusting the vm state appropriately
//		System.out.println("relayCall");
    	String componentId = getStringArg(nd, vms, 0);
    	String targetMethodName = getStringArg(nd, vms, 1);//"setColor" ; //extractString(datum);
//  	System.out.println("looking for method " + targetMethodName + " in " + componentId);
    	Scriptable targetComponent = find(componentId);
    	Assert.scriptingError(targetComponent != null, "Unable to find " + componentId);
    	int args = nd.childCount()-2;
    	Class<?>[] argumentClasses = new Class<?>[args];   	
		Object[] arguments = new Object[args];
		Object[] reverted = new Object[args]; // arguments if we revert to datums
		/* The argsMask is a bit mask with one bit per argument, the 1 bit corresponding
		 * to the last argument. The bit for a particular argument is set iff a
		 * conversion from Datum to a java type was successfully carried out.
		 * We want to first convert every argument possible and then see if a
		 * method exists in the targetComponent corresponding to this maximally
		 * converted argument set.
		 * If no such method exists, we try all combinations of converted and
		 * unconverted arguments possible, starting from the right. That is
		 * the last converted argument reverts to Datum.class first.
		 * It is useful to think of the mask as an integer on the
		 * range [0,2^args -1]
		 * 
		 * For example, suppose a function has four arguments and that
		 * the first and third were converted whereas the 2nd and 4th were
		 * not. The bit mask is then 1010 which we denote 10 in decimal.
		 * 
		 * No number above 10 can correspond to a valid mask
		 * If we start with 10 (all possible conversions) we generate the
		 * following sequence:
		 * 10    1010
		 *  9    1001
		 *  8    1000
		 *  7    0111
		 *  6    0110    and so on
		 *  Clearly only 8 produces a valid mask since 9, 7 and 6 require
		 *  us to set 0's in the original mask to 1's which implies converting
		 *  datums we couldn't convert in the first place.
		 *  Is there an easy way to disqualify the invalid masks?
		 *  Yes, AND any mask with the complement of the original mask,
		 *  for example 9 & ~10 is 1001 & 0101 which produces a 1 in the unit
		 *  position, precisely the place where we are trying to convert an
		 *  unconvertable datum.
		 *  
		 *  So our simple algorithm decrements the mask until the AND with
		 *  the complement of the original mask is 0, allowing us to find
		 *  all possible combinations 
		 */
		int argsMask = 0;
    	for (int a = 0; a < args; a++){
    		/* nd.child_exp(a+2) returns the argument expression node
    		 * vms.top() returns an "Evaluation" from the top of the
    		 * vms stack. This appears to be the value of the node
    		 * taken from a list of values representing values at different
    		 * times
    		 * Now how to derive type? AT first blush, TypeNodes seem
    		 * the way to go but there are no fundamental types in the
    		 * CLC - they are all language specific.
    		 * Seems best to use just the Datums
    		 */
    		Datum datum = (Datum)(vms.top().at(nd.child_exp(a+2)));
    		translateDatum(datum, vms, cp.getLanguage(), argumentClasses, arguments, a);
    		if (argumentClasses[a] == Datum.class)
    			arguments [a] = datum;
    		else argsMask += 1 << args-1-a;  // set bit in argument mask
    		reverted[a] = datum;   // reverted arguments are always the datum
    	}   	
    	
    	int argsMaskComp = ~argsMask;
    	Method method = null;
    /*    	CachedMethod cm = null;
    	
    	if (methodCache.containsKey(targetMethodName))
    		cm = (CachedMethod)methodCache.get(targetMethodName);
     	if (cm != null && cm.myArgs == args){
     		method = cm.myMethod;
     		return callMethodInComponent(targetComponent, method, arguments);
     	}*/
     	
     	
    	while (argsMask >= 0){
    		method = (Method)methodCache.get(makeKey(componentId, targetMethodName, argumentClasses, args, argsMask));
    		if (method == null){
    			method = findMethodInComponent(targetComponent, targetMethodName, argumentClasses, args, argsMask);
    			if (method != null)
    				methodCache.put(makeKey(componentId, targetMethodName, argumentClasses, args, argsMask), method);
    		}// else System.out.println("Using cached " + targetMethodName);
 //       	System.out.println("trying " + targetMethodName + " with argsMask " + argsMask);
    		if (method != null) break;  // found it!
    		while((--argsMask & argsMaskComp) != 0)  // step down to next valid argsMask
    			if (argsMask < 0) break;
    	}
    	// Note caching fudge - only caches methods for which all args are long
/*    If caching is needed it will have to be done better. We will have to cache
 *   argMask as well as the method so we know which arguments to convert
 *   Will also have to match number and type of original args	
 * boolean cacheIt = true;
    	for(int a = 0; a < args; a++){
    		if (argumentClasses[a] != long.class)
    			cacheIt = false;
    	}
    	if (cacheIt)
        	methodCache.put(targetMethodName, new CachedMethod(method, args));
*/    	
//    	System.out.println("found " + targetMethodName + " using argsMask " + argsMask);
    	/* if any argument class has been reverted to Datum.class
    	 * we need to revert the argument to datum
    	 */
    	
		Object[] actualArgs = new Object[args]; // arguments if we revert to datums
		int bitPick = 1;
    	for (int a = 0; a < args; a++){   		
    		actualArgs[a] = (argsMask & (bitPick << args - 1 - a) ) == 0 ? reverted[a] : arguments[a];
    		if (argumentClasses[a] == boolean.class)
    			actualArgs[a] = !actualArgs[a].equals(new Long(0));
    	}
    	 
    	return makeTheCall(targetComponent, targetMethodName, method, actualArgs);
	}
	
	
	/** make a snapshot (an image) of the current contents of a Display Plug-In 
	 * 
	 * @param nd
	 * @param vms
	 */
	
	public void snapShot(ExpressionNode nd, VMState vms){
		Assert.check(cp != null, NO_CP);
    	String componentId = getStringArg(nd, vms, 0); 
    	String porthole = getStringArg(nd, vms, 1);
    	Scriptable targetComponent = find(componentId);
    	Assert.scriptingError(targetComponent != null, "Unable to find " + componentId);
/*    	AppletContext context = ((Applet)cp).getAppletContext();
    	Applet registrar = context.getApplet("PortholeRegistrar");
    	System.out.println("registrar is " + registrar);
    	if (registrar == null) return;
    	System.out.println("Trying to get graphics for " + porthole);
    	graphics = PortholeRegistrar.getGraphics(porthole); */
    	Class<?>[] argumentClasses = new Class<?>[1];
    	argumentClasses[0] = String.class; 
		Object[] arguments = new Object[ 1 ] ;
		arguments [0] = porthole;
		
    	callMethodInComponent(targetComponent, "snapshot", argumentClasses, arguments);
	}
	
	/** use the current state of the generator datums of a display as the reference state
	 * 
	 * @param nd
	 * @param vms
	 */
	
	public void setReference(ExpressionNode nd, VMState vms){
		Assert.check(cp != null, NO_CP);
    	String componentId = getStringArg(nd, vms, 0); 
    	Scriptable targetComponent = find(componentId);
    	Assert.scriptingError(targetComponent != null, "Unable to find " + componentId);		
    	callMethodInComponent(targetComponent, "setReference", null, null);
	}

/** compare the current state of the generator datum set of a component to the reference state
 * 
 * @param nd
 * @param vms
 * @return true if the states are the same, false otherwise
 */
	
	public boolean compareReference(ExpressionNode nd, VMState vms){
		Assert.check(cp != null, NO_CP);
    	String componentId = getStringArg(nd, vms, 0); 
    	Scriptable targetComponent = find(componentId);
    	Assert.scriptingError(targetComponent != null, "Unable to find " + componentId);		
    	boolean result = ((Boolean)callMethodInComponent(targetComponent, "compareReference", null, null)).booleanValue();
//    	System.out.println("returns " + (result ? "true." : "false."));
//    	if (comparisonSet == null) comparisonSet = new Vector<Boolean>();
//    	comparisonSet.add(new Boolean(result));
    	return result;
	}
	

	/***************************************************************************
	 *  These are the external scripting calls handled by the ScriptManager
	 * usually because they are being passed on to a component. To be available
	 * externally they have to be in the command interface 
	 ****************************************************************************/
	
	
	
	public Image getSnap(String componentId, String porthole){
		Assert.check(cp != null, NO_CP);
    	Scriptable targetComponent = find(componentId);
    	Assert.scriptingError(targetComponent != null, "Unable to find " + componentId);
/*    	AppletContext context = ((Applet)cp).getAppletContext();
    	Applet registrar = context.getApplet("PortholeRegistrar");
    	System.out.println("registrar is " + registrar);
    	if (registrar == null) return;
    	System.out.println("Trying to get graphics for " + porthole);
    	graphics = PortholeRegistrar.getGraphics(porthole); */
    	Class<?>[] argumentClasses = new Class<?>[1];
    	argumentClasses[0] = String.class; 
 		Object[] arguments = new Object[ 1 ] ;
		arguments [0] = porthole;
		
    	lastImageHeight = ((Integer)callMethodInComponent(targetComponent, "getSnapHeight", argumentClasses, arguments)).intValue();
    	lastImageWidth = ((Integer)callMethodInComponent(targetComponent, "getSnapWidth", argumentClasses, arguments)).intValue();
    	return (Image)callMethodInComponent(targetComponent, "getSnap", argumentClasses, arguments);
	}
	
	public boolean getComparison(String componentId, int n){
		Assert.check(cp != null, NO_CP);
    	Scriptable targetComponent = find(componentId);
    	Assert.scriptingError(targetComponent != null, "Unable to find " + componentId);
    	Class<?>[] argumentClasses = new Class<?>[1];
    	argumentClasses[0] = int.class; 
 		Object[] arguments = new Object[ 1 ] ;
		arguments [0] = n;
		
		return ((Boolean)callMethodInComponent(targetComponent, "getComparison", argumentClasses, arguments)).booleanValue();
	}

	public int getLastSnapHeight(){
		return lastImageHeight;	
	}
	
	public int getLastSnapWidth(){
		return lastImageWidth;	
	}
	
	public long getLocalInt(String datumName){
		Assert.check(cp != null, NO_CP);
		RegionInterface stack = cp.getStackRegion();		
		for (int i = stack.getFrameBoundary() + 1; i < stack.getNumChildren(); i++){
			Datum local = stack.getChildAt(i);
			if(local.getName().equals(datumName)){
				Assert.check(local.getTypeString().equals("int"), "getLocalInt must name an int datum");
				return ((AbstractIntDatum)local).getValue();
			}
		}
		Assert.scriptingError("Unable to find local datum called " + datumName);
		return -1;
	}
	

	
	public static void autoRun(){
		Assert.check(cp != null, NO_CP);
		cp.autoRun();
	}
	
	public static void showTM(boolean visible){
		Assert.check(cp != null, NO_CP);
		cp.showTM(visible);
		
	}
	
	public static boolean isTMShowing(){
		Assert.check(cp != null, NO_CP);
		return cp.isTMShowing();
		
	}
	
	public static boolean isInAuto(){
		Assert.check(cp != null, NO_CP);
		return cp.isInAuto();
		
	}
	
	
	public static void stopAuto(){
		Assert.check(cp != null, NO_CP);
		cp.stopAuto();		
	}
	

	
	public String getId() {
		return "ScriptManager";
	}

	/* if the Datum can be readily evaluated, sets the class and value of the argument  
	 * 
	 */
	private void translateDatum(Datum datum, VMState vms, int language, Class<?>[] argumentClasses, Object[] arguments, int a){
		String datumString;
		// Language independent translations
		if (datum instanceof BooleanDatum){
			argumentClasses[a] = boolean.class;
			arguments[a] =((BooleanDatum)datum).getValue();
		}
		else if (datum instanceof BoolDatum){
			argumentClasses[a] = boolean.class;
			arguments[a] =((BoolDatum)datum).getValue();
		}
		else if (datum instanceof AbstractIntDatum){ // All int types treated as long
	 		argumentClasses[a] = long.class;
			arguments[a] =((AbstractIntDatum)datum).getValue();
		} else if (datum instanceof AbstractFloatDatum){// All float types treated as double
			argumentClasses[a] = double.class;
			arguments[a] =((AbstractFloatDatum)datum).getValue();
		}else if ((datumString  = getStringArg((AbstractDatum)datum, vms))!= null){
	 		argumentClasses[a] = String.class;
			arguments[a] = datumString;
//			System.out.println("found a String parameter: "+ datumString);
		} else {
			argumentClasses[a] = Datum.class;
			
		}
}

	private String makeKey(String componentId, String methodName, Class<?>[] argClasses, int args, int argsMask){
		String key = componentId + "." + methodName;
		for (int i = 0; i < args; i++)
			key += argClasses[i];
		key += argsMask;
//		System.out.println(key);
		return key;
	}
		
	private Method findMethodInComponent(Scriptable component, String methodName, Class<?>[] argClasses){
    	Class<?> targetClass = component.getClass();

    	Method method = null;
		try {
			method = targetClass.getMethod(methodName, argClasses);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			// That's ok, just return null;
		}
		return method;
	}
	
	private Method findMethodInComponent(Scriptable component, String methodName, Class<?>[] argClasses, int args, int argMask){
		Class<?>[] masked = new Class<?>[args];
		int argPick = 1;
		for (int a = 0; a < args; a++){
			masked[a] = (argPick << (args-1 - a) & argMask)==0 ? Datum.class : argClasses[a];
		}
		return findMethodInComponent(component, methodName, masked);
	}

	
	private Object callMethodInComponent(Scriptable component, String methodName, Class<?>[] argClasses, Object[] arguments){
    	Method method = findMethodInComponent(component, methodName, argClasses);
		return makeTheCall(component, methodName, method, arguments);
	}


	private Object makeTheCall(Scriptable component, String methodName, Method method, Object[] arguments){
		//System.out.println("calling " + methodName + " id: " + method + " with args " + arguments);
		Assert.scriptingError(method != null, "ScriptManager unable to find method "+methodName+" in " + component);
		try {
			return method.invoke(component, arguments);
		} catch (IllegalArgumentException e) {
		    Assert.check(e) ; // Shouldn't happen
		} catch (IllegalAccessException e) {
            Assert.check(e) ; // Shouldn't happen
		} catch (InvocationTargetException e) {
            Throwable e1 = e.getCause() ;
            if( e1 instanceof Error ) {
                throw (Error)e1 ; }
            else if( e1 instanceof java.lang.RuntimeException ) {
                throw (java.lang.RuntimeException)e1 ; }
            else {
                Assert.check(e) ; } // Shouldn't happen
            
		}
		return null;		
	}


	
	
	
	private String getStringArg(ExpressionNode nd, VMState vms, int o){
	    Clc_ASTUtilities util = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
		AbstractDatum datum = (AbstractDatum) vms.top().at(nd.child_exp(o));
		Assert.scriptingError( util.isString(datum), "Expected string arument") ;
		return (String) util.getNativeValue(datum, vms) ;
	}

	private String getStringArg(AbstractDatum datum, VMState vms){
	    Clc_ASTUtilities util = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
		return util.isString(datum) ? (String) util.getNativeValue(datum, vms) : null ;
	}

}
