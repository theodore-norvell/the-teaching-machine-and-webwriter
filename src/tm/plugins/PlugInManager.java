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

/*
 * Created on 6-Jul-2006 by Theodore S. Norvell. 
 */
package tm.plugins;

import java.util.* ;

import tm.configuration.Configuration;
import tm.configuration.ConfigurationServer;
import tm.interfaces.Configurable;
import tm.utilities.Debug;

/** A PlugInManager is responsible for creation of plug-in factory objects.
 * It is also a repository for information about which plug-in factory classes to
 * use for various jack names in the system.
 * 
 * <p>Creating objects using a PlugInManager is a several step process that
 * typically looks like this.
 * <pre>
 * SomePlugInInterface plugIn ;
 * <b>try</b> {
 *     <i>// 0. Obtain the plug-in manager</i>
 *         PlugInManager thePlugInManager = PlugInManager.getSingleton() ;
 *     <i>// 1. Obtain a factory object from the plug-in manager</i>
 *         SomePIFactoryIntf factory
 *            = thePlugInManager.getFactory( jackName, SomePIFactoryIntf.<b>class</b>, <b>true</b> ) ;
 *     <i>// 2. Construct a new plug-in from the factory</i>
 *         plugIn = factory.createPlugIn( <i>...parameters...</i> ) ; }
 * <b>catch</b>( PlugInNotFound ex ) {
 *     <i>...deal with it... </i> }
 * </pre>
 * <p>For jacks that take multiple plug-ins the same steps are taken, but some must be repeated.
 * <pre>
 * SomePlugInInterface[] ps ;
 * <b>try</b> {
 *     <i>// 0. Obtain the plug-in manager</i>
 *         PlugInManager thePlugInManager = PlugInManager.getSingleton() ;
 *     <i>// 1. Obtain a list of factory objects from the plug-in manager</i>
 *         List&lt;SomePIFactoryIntf&gt; factories
 *            = thePlugInManager.getFactoryList( jackName, SomePIFactoryIntf.<b>class</b>, <b>false</b> ) ;
 *     <i>// 2. Construct new plug-ins from the factories</i>
 *         ps = <b>new</b> SomePlugIn[ factories.size() ] ;
 *         <b>for</b>( <b>int</b> i = 0 ; i < fs.size() ; ++i ) {
 *             ps[i] = factories.get(i).createPlugIn( <i>...parameters...</i> ) ;
 * <b>catch</b>( PlugInNotFound ex ) {
 *     <i>...deal with it...</i> }
 * </pre>
 * 
 * <p>
 * The PlugInManager is an {@link Observable}.  {@link Observer}s of it will be
 * notified of any changes to the set of registrations. The first argument to
 * {@link Observer#update(Observable, Object)} will be the PlugInManager. The
 * second will be a {@link Set}&lt;{@link String}&gt; containing all the
 * jacknames of registrations that have been either added or deleted. 
 */
public class PlugInManager extends Observable implements Iterable<PlugInRegistration>, Configurable {

    private static PlugInManager theSingleton ;
    
    private Set<PlugInRegistration> registrations = new TreeSet<PlugInRegistration>() ;
   
    private PlugInManager() {
        ConfigurationServer server = ConfigurationServer.getConfigurationServer();
        server.register(this, "PlugInManager");
        notifyOfLoad( server.getConfiguration(this)) ;
    }
    
    /** The singleton instance of PlugInManager
     * 
     * @return the singleton instance of PlugInManager
     */
    public static PlugInManager getSingleton() {
        if( theSingleton == null ) theSingleton = new PlugInManager() ;
        return theSingleton ;
    }
    
    /** Return a factory object for a given jack name.
     * 
     * @param jackName The name of a jack.
     * @param expectedType A run-time type token in the form of a Class object
     *        representing the interface expected to be implemented by the result.
     *        For example the call <code>getFactory( "jn", SomePIFactoryIntf.class )</code>
     *        returns an object of type <code>SomePIFactoryIntf.class</code>. This inteface must extend
     *        <code>PlugInFactory</code>.
     * @param isMandatory True if there must be a plug-in returned.
     *        This method may return <code><b>null</b></code> iff <code>isManditory==false</code>.
     * @return The active plug-in factory object registered for the jack. If isMandatory is false and there is
     *  plug-in factory registered, then null will be returned.
     * @throws PlugInNotFound Under any of the following circumstances:
     *   <ul>
     *       <li>There is trouble instantiating any factories associated with the given jack name.
     *           (See <code>getFactoryList</code> for a list of all possible causes.)</li>
     *       <li>There is no active plug-in factory registered for the given jack name and isMandatory is true.</li>
     *       <li>There is more than one active plug-in factory registered for the given jack name</li>
     *   </ul>
     *   @see getFactoryList for possible casues of exceptions.
     */
    public <T extends PlugInFactory>  T getFactory( String jackName, Class<T> expectedType, boolean isMandatory  ) throws PlugInNotFound {
        List<T> factoryArray = getFactoryList( jackName, expectedType, isMandatory );
        if( factoryArray.size() == 0 ) {
            return null ;
        } else if( factoryArray.size() > 1 ) {
            throw new PlugInNotFound("Too many plug-ins for '"+jackName+"' found") ;
        }
        return factoryArray.get(0) ;
    }
    
    /** Get all active plug-in factories registered for a given jack name.
     * 
     * @param jackName The name of a jack.
     * @param expectedType A run-time type token in the form of a Class object
     *        representing the interface expected to be implemented by the results.
     *        For example the call <code>getFactoryList( "jn", SomePIFactoryIntf.class )</code>
     *        returns an object of type <code>List&lt;SomePIFactoryIntf&gt;</code>. This inteface must extend
     *        <code>PlugInFactory</code>.
     * @param isMandatory True if there must be at least one active plug-in factory returned.
     *        This method may return an empty list iff <code>isManditory==false</code>.
     * @return A list of factory objects registered for the given jack name.
     *         If isMandatory is true, the list will be non empty.
     * @throws PlugInNotFound if there is difficulty instantiating any of the
     *         factories.  In particular the following things can go wrong:
     *         <ul><li>The class of the factory can not be loaded.</li>
     *             <li>The class of the factory does not have a public static method
     *                 called <code>createInstance</code> that takes a single <code>String</code>
     *                 parameter</li>
     *             <li>The call to <code>createInstance</code> throws something.</li>
     *             <li>The call to <code>createInstance</code> returns an object that
     *                 is not an instance of the <code>expectedType</code> class</li>
     *             <li>isMandatory is true and there is no factory registered under the jackName.
     *         </ul>
     *         The message of the exception will record one of these reasons.
     */
    public <T extends PlugInFactory> List<T> getFactoryList( String jackName, Class<T> expectedType, boolean isMandatory ) throws PlugInNotFound {
        Set<PlugInRegistration> applicable = new TreeSet<PlugInRegistration>() ;
        for( PlugInRegistration r : registrations ) {
            if( ! r.isActive() ) continue ;
            if( r.getJackName().equals( jackName ) )
                applicable.add( r ) ;
        }
        List<T> result = new ArrayList<T>( applicable.size() ) ;
        for( PlugInRegistration r : applicable ) {
            PlugInFactory plugInFactory = r.createFactoryObject() ;
            if( ! (expectedType.isAssignableFrom( plugInFactory.getClass() ) ) )
                throw new PlugInNotFound(
                        "Factory of class '"+plugInFactory.getClass().getName()
                        +"' is not assignable to expected type '"+expectedType.getName() ) ;
            result.add( (T)plugInFactory ) ; } // Ignore the warning.
        
        if( isMandatory && result.size() == 0 )
            throw new PlugInNotFound("Expected at least one plug-in in jack "+jackName) ;
        
        return result ;
    }

    /** Adds a new plug-in registration to the set of registrations. 
     * <p>Note that duplicates are not added.
     * 
     * @param plugInRegistration The registration to add
     */
    public void registerPlugIn( PlugInRegistration plugInRegistration ) {
        registrations.add( plugInRegistration ) ;
        Set<String> jackNameSet = new HashSet<String>() ;
        jackNameSet.add( plugInRegistration.getJackName() ) ;
        setChanged() ;
        notifyObservers();
    }
    
    /** Remove a registration */
    public void deRegisterPlugIn( PlugInRegistration plugInRegistration ) {
        registrations.remove( plugInRegistration ) ;
        Set<String> jackNameSet = new HashSet<String>() ;
        jackNameSet.add( plugInRegistration.getJackName() ) ;
        setChanged() ;
        notifyObservers(jackNameSet);
    }
    
    /** Iterate over all registrations */
    public Iterator<PlugInRegistration> iterator() {
        return registrations.iterator() ;
    }
    
	public String getConfigId(){return "PlugInManager";}	


    public void notifyOfSave(Configuration config) {
    	config.setValue("NumberOfRegistrations", Integer.toString(registrations.size()) ) ;
        int i=0 ;
        for( PlugInRegistration reg : registrations ) {
        	config.setValue( "JackName."+i, reg.getJackName() ) ;
        	config.setValue( "FactoryClassName."+i, reg.getClassName() ) ;
        	config.setValue( "IsActive."+i, reg.isActive() ? "true" : "false" ) ;
            config.setValue( "Parameter."+i, reg.getParameter() ) ;
            i += 1 ;
        }
    }

    public void notifyOfLoad(Configuration config) {
        Debug.getInstance().msg(Debug.ALWAYS, "Notify Of Load");
        String temp = config.getValue("NumberOfRegistrations" ) ;
        if( temp == null ) return ;
        Debug.getInstance().msg(Debug.ALWAYS, "NumberOfRegistrations is "+temp);
        // jackNameSet is the set of jackNames that have changed.
        // It is used for notifying observers.
        Set<String> jackNameSet = new HashSet<String>() ;
        for( PlugInRegistration reg : registrations )
            jackNameSet.add( reg.getJackName()) ;
        registrations.clear() ;
        int count = Integer.parseInt( temp ) ;
        for( int i = 0 ; i < count ; ++i ) {
            String jackName = config.getValue( "JackName."+i) ;
            String className = config.getValue( "FactoryClassName."+i) ;
            String isActiveStr = config.getValue( "IsActive."+i) ;
            boolean isActive = isActiveStr == null ? true : isActiveStr.equals( "true" ) ;
            String parameter = config.getValue( "Parameter."+i) ;
            if( jackName != null && className != null && parameter != null ) {
                jackNameSet.add( jackName ) ;
//            	System.out.println("PlugInManager registering " + className + " with parameter " + parameter + " for jack " + jackName);
                PlugInRegistration reg = new PlugInRegistration( jackName, className, parameter, isActive ) ;
                registrations.add( reg ) ; }}
//        System.out.println("Notifying observers");
        setChanged() ;
        notifyObservers( jackNameSet );
    }

}