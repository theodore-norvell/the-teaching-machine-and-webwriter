package com.remoteTMproject.model.map;

import java.util.HashMap;

public class map1 {
public static void main(String args[]) {
// ����hashmap
HashMap newmap = new HashMap(); 

// ��hashmap��ֵ
newmap.put(1, "tutorials");
newmap.put(2, "point");
newmap.put(3, "is best");

System.out.println("Values before remove: "+ newmap);

// �Ƴ�keyΪ2��value
newmap.remove(2);

System.out.println("Values after remove: "+ newmap);
} 
}