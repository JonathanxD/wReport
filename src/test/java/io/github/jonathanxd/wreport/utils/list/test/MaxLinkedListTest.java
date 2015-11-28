package io.github.jonathanxd.wreport.utils.list.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import io.github.jonathanxd.wreport.utils.list.MaxLinkedList;

public class MaxLinkedListTest {

	public static void main(String[] args) {
		MaxLinkedList<String> maxLinkedList = new MaxLinkedList<>(10);		
		show(maxLinkedList);
		
		for(int x = 0; x < 10; ++x)
			maxLinkedList.add("Aba ["+x+"]");
		
		show(maxLinkedList);
		maxLinkedList.add(0, "Exceeding");
		show(maxLinkedList);
		maxLinkedList.addFirst("Exceeding 2");
		show(maxLinkedList);
		maxLinkedList.addLast("Exceeding 2");
		show(maxLinkedList);
		maxLinkedList.addAll(Arrays.asList("Collection Exceeding", "Collection Exceeding 2"));
		show(maxLinkedList);
		
		Collection<String> collectionToAdd = new ArrayList<>();
		for(int x = 0; x < 11; ++x)
			collectionToAdd.add("Collection Exceeding ["+x+"]");
		
		// maxLinkedList.addAll(collectionToAdd);
		// show(maxLinkedList);

		// maxLinkedList.addAll(maxLinkedList.size(), collectionToAdd);
		// show(maxLinkedList);
		
		maxLinkedList.addAllAutoResize(2, collectionToAdd);
		show(maxLinkedList);
	}
	
	public static void show(MaxLinkedList<?> maxLinkedList){
		System.out.printf("List: %s, Size: %d, MaxSize: %d%n", maxLinkedList.toString(), maxLinkedList.size(), maxLinkedList.getMaxListSize());
	}

}
