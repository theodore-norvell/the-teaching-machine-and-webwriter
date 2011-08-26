//! run
package package0 ;

import package1.Imported ;

class Importer {
    Importer() {
        Imported x = new Imported() ;
    }
    
    public static void main() {
        new Importer() ;
    }
}