//     Copyright 1998--2013 Michael Bruce-Lockhart and Theodore S. Norvell
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

import java.util.HashMap;

/** A transaction is a record of all that has changed since the last checkpoint.
 * <p>For each variable that changes, the transaction keep a record of its value at the time of the checkpoint.
 * The transaction also keeps a record of which variable have been created or destroyed since the checkpoint.
 * On an undo, the transaction is applied which means:</p>
 *     <li>all variables that were updated are restored to their value as of the checkpoint.
 * </ul>
 * <p>Furthermore, on undo the transaction is modified so that the next time it is applied the state is
 * returned to the way it was. That is an even number of apply operations is a no-op.</p> 
 * @author theo
 *
 */
class Transaction {
	static class State { boolean alive ; Object value ; 
		State(boolean alive, Object value) { this.alive=alive; this.value=value ;} 
	}
	
	private HashMap<BTVar<?>, State> theMap = new HashMap<BTVar<?>, State>() ;
	
	void noteBirth( BTVar<?> var ) {
		if( ! theMap.containsKey( var ) ) theMap.put(var, new State(false, var.currentValue)) ;
	}
	
	void noteDeath( BTVar<?> var ) {
		if( ! theMap.containsKey( var )  ) theMap.put(var, new State(true, var.currentValue) ) ;
	}
	
	void noteUpdate( BTVar<?> var ) {
		if( ! theMap.containsKey( var )  ) theMap.put(var, new State(true, var.currentValue) ) ;
	}
	
	// invariant: lastUsedByteArray == null || byteArraysMap.get( lastUsedByteArray ) == lastUsedHashTable
	private BTByteArray lastUsedByteArray = null ;
	private CacheLine[] lastUsedHashTable = null ;
	// invariant lastUsedByteArray == null ||  lastCacheLineAddress is
	//             the address of a line that has been saved to the hashtable
	private int lastCacheLineAddress = 0 ;
	
	/** Determine whether this byte's initial value has
	* been saved. And, if it hasn't, then save it --along with the rest of 
	* its cache line-- to the hash table.
	* 
	* @param bta A byte array
	* @param abc The address of the byte about to be written.
	*/
	void notePutByte(BTByteArray bta, int abc ) {
		int ab = abc>>cWidth ;
		CacheLine[] hashTable ;
		if( bta == lastUsedByteArray ) {
			// Maybe we just put a byte in the same cache line in the same array
			// If so, there is nothing more to do.
			if( lastCacheLineAddress == ab ) return ;
			hashTable = lastUsedHashTable ; }
		else if( byteArraysMap.containsKey( bta ) ) {
			hashTable = byteArraysMap.get( bta ) ;
			lastUsedByteArray = bta ; lastUsedHashTable = hashTable ; }
		else {
			hashTable = new CacheLine[1<<bWidth] ;
			byteArraysMap.put( bta, hashTable ) ;
			lastUsedByteArray = bta ; lastUsedHashTable = hashTable ; }
		
		lastCacheLineAddress = ab ;
		
		int b = ab & bMask  ;
		CacheLine p = hashTable[b] ;
		
		// Search for an existing cache line in the hash table with the same line address value.
		while( p != null && p.lineAddress != ab ) p = p.next ; 
		// Either p == null or p.lineAddress == ab. 
		// In the first case, there is no record for this cache line. In the second there is.
		// If none, create a new cache line
		if( p == null ) {
			CacheLine newCacheLine = new CacheLine() ;
			newCacheLine.next = hashTable[b] ;
			hashTable[b] = newCacheLine ;
			newCacheLine.lineAddress = ab ;
			int baseAddress = ab << cWidth ;
			int limit = Math.min((1<<cWidth), bta.size-baseAddress) ;
			for( int c = 0 ; c < limit ; ++c ) 
				newCacheLine.bytes[ c ] = bta.a[ c + baseAddress ] ; }
	}
	
	void apply() {
		
		// Deal with variables.
		for( BTVar<?> var : theMap.keySet() ) {
			State state = theMap.get( var ) ;
			theMap.put( var, new State( var.alive, var.currentValue ) ) ;
			var.currentValue = state.value ;
			var.alive = state.alive ; }
		
		// Now deal with all the byte arrays.
		
		// For each byte array that changed:
		for( BTByteArray bta : byteArraysMap.keySet() ) {
			CacheLine[] hashTable = byteArraysMap.get( bta ) ;
			// For each chain in the hash table:
			for( int b = 0 ; b <  hashTable.length ; ++b ) {
				// For each line in the chain:
				for( CacheLine cacheLine = hashTable[b] ; cacheLine != null ; cacheLine = cacheLine.next ) {
					int baseAddress = cacheLine.lineAddress << cWidth ;
					// For each byte in the line: Swap with the bytes in the array.
					int limit = Math.min((1<<cWidth), bta.size-baseAddress) ;
					for( int c = 0 ; c < limit ; ++ c ) {
						byte tempByte = bta.a[baseAddress+c] ; 
						bta.a[baseAddress+c]  = cacheLine.bytes[c]  ;
						cacheLine.bytes[c] = tempByte ; } } } }
	}
	
	// We use a hash table to keep track of bytes in byte arrays that have changed
	// while this transaction is current.  Rather than dealing with each byte location individually,
	// we take them in lines of 2^cWidth bytes where, cWidth is some small natural (e.g. 6).
	// The structure of a hash table is as follows:
	//
	// Each byte address abc is divided into 3 fields, call them a, b, and c.
	// (With a to the left and c to the right.)
	// The width of c is cWidth bits. The width of b is bWidth bits wide.
	// The width of a is 32 - cWidth - bWidth.
	// We use the b value to index the hash table. I.e. each address abc hashes to b and the size of
	// the table is 2^bWidth.
	// We use the c value to index within the a given cache line.
	// We use ab as the address of the cache line.
	// The algorithm to find a location representing byte abc is as follows.
	//  * Look in the hash table at index b.
	//  * Traverse the linked list of cache lines to find one with cache line address of ab. 
	//  * Location c in that line is the right one.
	
	private static final int cWidth = 6 ; // Cache lines are 64 bytes each
	private static final int bWidth = 10 ; // 1024 buckets in each hashtable
	private static final int bMask = (1<<bWidth)-1 ; // 00...01111111111
	
	HashMap<BTByteArray,CacheLine[]> byteArraysMap = new HashMap<BTByteArray,CacheLine[]>() ;
	private class CacheLine {
		int lineAddress ; // The most significant 32-Log2CacheLineSize bits of the address
		byte[] bytes = new byte[ 1<<cWidth] ;
		CacheLine next ;
	}
}


