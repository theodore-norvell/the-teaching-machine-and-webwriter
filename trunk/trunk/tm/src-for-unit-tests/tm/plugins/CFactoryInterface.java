/*
 * Created on 10-Jul-2006 by Theodore S. Norvell. 
 */
package tm.plugins;

import tm.plugins.PlugInFactory;

/**
 * @author theo
 */
public interface CFactoryInterface extends PlugInFactory {
    public CPlugInInterface createPlugIn( Data theData ) ;
}
