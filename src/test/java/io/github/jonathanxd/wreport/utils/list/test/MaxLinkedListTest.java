/*
 *
 * 	wReport - An Sponge plugin to report bad players and start a vote kick.
 *     Copyright (C) 2016 TheRealBuggy/JonathanxD (Jonathan Ribeiro Lopes) <jonathan.scripter@programmer.net>
 *
 * 	GNU GPLv3
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
