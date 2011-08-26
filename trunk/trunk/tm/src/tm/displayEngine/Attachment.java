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

import java.awt.Point;

import tm.utilities.Assert;

public class Attachment extends Object{
    private boolean input = false;  // true if input attached to this point
    private Link myLink = null;    // the link attached 
    private int attachNo;      // which reference point on LinkedDatumDisplay
    private LinkedDatumDisplay myOwner;
    
    Attachment(LinkedDatumDisplay owner, int n){
        myOwner=owner;
        attachNo = n;
    }
    
    boolean isConnected(){
        return (myLink != null);
    }
    
    boolean isInput(){
        Assert.check(isConnected());
        return input;
    }
    
    Point getPoint(){
        return myOwner.getAPoint(attachNo);
    }
    
    Point getStub(){
        return myOwner.getAStub(attachNo);
    }
    
    LinkedDatumDisplay getOwner(){
        return myOwner;
    }
    
    int getAttachNo(){
        return attachNo;
    }

    
    void makeConnect(Link link, boolean in){
        myLink = link;
        input = in;
    }
    
    void breakConnect(){
        myLink = null;
    }
    
    public String toString(){
    	return "attachment #" + attachNo + " of " + myOwner.toString();
    }
}