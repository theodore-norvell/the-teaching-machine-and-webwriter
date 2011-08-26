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

package tm.displayEngine;

import tm.displayEngine.generators.AbstractGenerator;
  
  /* I have chosen NOT to follow the standard layoutManager since that
  is strongly bound to the standard container-component model. While
  our workArea container is a true Java container, our DatumDisplay
  elements are not true Java components. Instead, in the interests of
  efficiency, we rolled our own lightweight components
  */
  
  public interface DisplayLayoutManager{
    
   
    public void layoutDisplay(AbstractGenerator generator);

  }