/*
 *      wReport - An Sponge plugin to report bad players and start a vote kick. <https://github.com/JonathanxD/io.github.jonathanxd.wreport.wReport/>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2016 TheRealBuggy/JonathanxD (Jonathan Ribeiro Lopes) <jonathan.scripter@programmer.net>
 *      Copyright (c) contributors
 *
 *
 *      Permission is hereby granted, free of charge, to any person obtaining a copy
 *      of this software and associated documentation files (the "Software"), to deal
 *      in the Software without restriction, including without limitation the rights
 *      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *      copies of the Software, and to permit persons to whom the Software is
 *      furnished to do so, subject to the following conditions:
 *
 *      The above copyright notice and this permission notice shall be included in
 *      all copies or substantial portions of the Software.
 *
 *      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *      THE SOFTWARE.
 */
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
