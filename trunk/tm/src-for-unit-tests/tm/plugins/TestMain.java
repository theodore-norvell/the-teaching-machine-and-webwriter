package tm.plugins;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;

import tm.interfaces.ImageSourceInterface;
import tm.plugins.PlugInManager;
import tm.plugins.PlugInManagerDialog;
import tm.plugins.PlugInRegistration;

/*
 * Created on 10-Jul-2006 by Theodore S. Norvell. 
 */

public class TestMain {
    /**
     * @param args
     */
    public static void main(String[] args) {
        PlugInManager pim = PlugInManager.getSingleton() ;
        
        pim.registerPlugIn( new PlugInRegistration("tm.plugins.A:B",
                                        "tm.plugins.B1Factory", "") ) ;
        pim.registerPlugIn( new PlugInRegistration("tm.plugins.A:C",
                "tm.plugins.C0Factory", "") ) ;
        pim.registerPlugIn( new PlugInRegistration("tm.plugins.B1:D",
                "tm.plugins.D0", "") ) ;
        
        BPlugInInterface plugIn ;
        try {
            // 0. Obtain the plug-in manager
                PlugInManager thePlugInManager = PlugInManager.getSingleton() ;
            // 1. Obtain a factory object from the plug-in manager
                BFactoryInterface factory
                   = thePlugInManager.getFactory( "tm.plugins.A:B", BFactoryInterface.class, true ) ;
            // 2. Construct a new plug-in from the factory
                plugIn = factory.createPlugIn(  ) ;
                System.out.println("Made PlugIn" ) ;}
        catch( PlugInNotFound ex ) {
            System.out.println("Failed to make plug in: "+ex.getMessage() ) ;  }

        Data data = null ;
        CPlugInInterface[] ps ;
        try {
            // 0. Obtain the plug-in manager
                PlugInManager thePlugInManager = PlugInManager.getSingleton() ;
            // 1. Obtain a list of factory objects from the plug-in manager
                List<CFactoryInterface> factories
                   = thePlugInManager.getFactoryList( "tm.plugins.A:C", CFactoryInterface.class, false ) ;
            // 2. Construct new plug-ins from the factories
                ps = new CPlugInInterface[ factories.size() ] ;
                for( int i = 0 ; i < factories.size() ; ++i ) {
                    ps[i] = factories.get(i).createPlugIn( data ) ;
                    System.out.println("Made PlugIn" ) ; } } 
        catch( PlugInNotFound ex ) {
            System.out.println("Failed to make plug in: "+ex.getMessage() ) ; }

        
        ImageSourceInterface imageSource = new ImageSourceInterface() {

            public java.awt.Image fetchImage(String name) {
                return null ;
            } } ;
        
        PlugInManagerDialog window = new PlugInManagerDialog( pim, imageSource ) ;
        window.setVisible( true ) ;
    }

}
