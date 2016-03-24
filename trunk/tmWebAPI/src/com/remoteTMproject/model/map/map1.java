package com.remoteTMproject.model.map;

import java.util.HashMap;

public class map1 {
public static void main(String args[]) {
// 构造hashmap
HashMap newmap = new HashMap(); 

// 给hashmap赋值
newmap.put(1, "tutorials");
newmap.put(2, "point");
newmap.put(3, "is best");

System.out.println("Values before remove: "+ newmap);

// 移除key为2的value
newmap.remove(2);

System.out.println("Values after remove: "+ newmap);
} 
}