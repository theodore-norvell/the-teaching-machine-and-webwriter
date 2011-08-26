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

package tm.backtrack;

/** A class for objects that can be backed up
 */
class BTValManager<T> {
    private BTRecord<T> head ;

	public BTValManager( ) {
		head = null ;
    }

    private void backupToThisTimeLine(BTTime currentTime ) {
        if( head == null ) return ;
        BTTime crux = BTTime.leastCommonTime( head.end, currentTime ) ;
        while( head != null && head.end.epoch > crux.epoch ) {
            if( head.end == head.start ) {
                head = head.prev ; }
            else {
                head.end = head.end.prev ; } }
    }

	public void set(BTTime currentTime, T value) {
        backupToThisTimeLine( currentTime ) ;
        if( head == null ) {
            head = new BTRecord<T>( value, currentTime, currentTime, null ) ; }
        else if( head.end == head.start && head.end == currentTime ) {
            head.value = value ; }
        else {
            head.end = currentTime.prev ;
            head = new BTRecord<T>( value, currentTime, currentTime, head ) ; }
    }

	public T get( BTTime currentTime ) {
	    backupToThisTimeLine( currentTime ) ;
	    if( head == null ) return null ;
	    else {
	        head.end = currentTime ;
	        return head.value ; }
	}
}