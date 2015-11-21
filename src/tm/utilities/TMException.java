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

package tm.utilities;

import tm.interfaces.SourceCoords;

public class TMException extends RuntimeException {

    protected SourceCoords coords = SourceCoords.UNKNOWN ;

    public TMException( ) {
        super(  )  ;
    }

    public TMException( String message ) {
        super( message )  ;
    }

    public TMException( Throwable e ) {
        super( e )  ;
    }

    public String getMessage() {
        String m = super.getMessage() ;
        m = "Near or on line "+coords.getLineNumber()+". "+m ;
        m = "File \""+coords.getFile().getFileName()+"\". "+m ;
        return m;
    }

    public void setSourceCoords( SourceCoords coords ) {
        this.coords = coords ; }

    public SourceCoords getSourceCoords() {
        return coords ; }
}