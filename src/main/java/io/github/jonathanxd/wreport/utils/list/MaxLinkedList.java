package io.github.jonathanxd.wreport.utils.list;

import java.util.Collection;
import java.util.LinkedList;

public class MaxLinkedList<T> extends LinkedList<T>{

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
}
