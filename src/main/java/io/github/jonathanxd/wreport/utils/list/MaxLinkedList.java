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
package io.github.jonathanxd.wreport.utils.list;

import com.google.common.reflect.TypeToken;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import io.github.jonathanxd.wreport.data.Data;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class MaxLinkedList<T> extends LinkedList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8605182249607337685L;

	private final int maxListSize;
	
	public MaxLinkedList(int maxListSize) {
		this.maxListSize = maxListSize;
	}
	
	/**
	 * @see {{@link #add(int, Object)}
	 * @throws MaxLinkedListSizeExceeded if the index exceeds {@link #getMaxListSize()}
	 */
	@Override
	public void add(int index, T element) {
		int finalSize = this.size() + (index + 1);
		if(finalSize < maxListSize)
			super.add(index, element);
		else
			if(index < maxListSize){
				super.remove();
				super.add(index, element);
			}
			else
				throw new MaxLinkedListSizeExceeded(String.format("Cannot add item '%s'", element.toString()), finalSize, this);
	}
	
	@Override
	public boolean add(T e) {
		if(size() == maxListSize){
			super.remove();
		}
		return super.add(e);
	}
	
	@Override
	public void addLast(T e) {
		if(size() == maxListSize){
			super.remove();
		}
		super.addLast(e);
	}
	
	@Override
	public void addFirst(T e) {
		if(size() == maxListSize){
			super.set(0, e);
		}else{
			super.addFirst(e);
		}
		
	}
	
	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean result = true;
		
		for(T element : c){
			boolean addResult = this.add(element);
			if(!addResult){
				result = false;
			}
		}
		
		return result;
	}
	
	/**
	 * @see {@link LinkedList#addAll(int, Collection)}
	 * @throws MaxLinkedListSizeExceeded if the index exceeds {@link #getMaxListSize()}
	 */
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		int finalSize = index+c.size(); 
		if(finalSize < maxListSize){
			return super.addAll(index, c);
		}else{
			throw new MaxLinkedListSizeExceeded(String.format("Cannot add all items of collection '%s'", c.toString()), finalSize, this);
		}
	}
	
	/**
	 * Add all elements of collection using {@link #add(int, Object)} method.
	 * @param index Start of collection add
	 * @param c Collection
	 * @throws MaxLinkedListSizeExceeded if index >= {@link #getMaxListSize()}
	 */
	public void addAllAutoResize(int index, Collection<? extends T> c) {
		if(index < maxListSize)
			for(T element : c){
				add(index, element);
			}
		else
			throw new MaxLinkedListSizeExceeded(String.format("Cannot add any item of collection '%s'", c.toString()), index + 1, this);
		
	}
	
	public int getMaxListSize() {
		return maxListSize;
	}

	public static class Serializer implements io.github.jonathanxd.wreport.serializer.Serializer<MaxLinkedList<?>> {

		@Override
		public void serialize(Data<MaxLinkedList<?>> object, ConfigurationNode node) {
			try {
				node.setValue(TypeToken.of(List.class), object.getData());
			} catch (ObjectMappingException e) {
				e.printStackTrace();
			}


		}

		@SuppressWarnings("unchecked")
		@Override
		public MaxLinkedList<?> deserialize(ConfigurationNode node) {

			MaxLinkedList maxLinkedList = new MaxLinkedList<>(64);

			try {
				List list = node.getValue(TypeToken.of(List.class));
				maxLinkedList.addAll(list);
			} catch (ObjectMappingException e) {
				e.printStackTrace();
			}

			return maxLinkedList;
		}
	}
}
