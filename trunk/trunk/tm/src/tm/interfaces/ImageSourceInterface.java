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

package tm.interfaces;

// ImageSourceInterface
// Describes objects that can turn a file name into a string.
// This is very similar to getImage of the Applet class, but it
// does not require a URL.  In reality, the image will loaded
// from the file system or else from a .jar file.
// The reason for introducing this class is because the Applat.getImage
// method does not currently work in Netscape Navigator. This interface
// allows an alternative implementation to be used.

import java.awt.Image ;

public interface ImageSourceInterface
{
    public Image fetchImage( String name ) ;
}