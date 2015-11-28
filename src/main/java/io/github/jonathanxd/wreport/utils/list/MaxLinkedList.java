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
	
	@Override
	public void add(int index, T element) {
		if(index < maxListSize)
			super.add(index, element);
	}
	
	@Override
	public boolean add(T e) {
		
		return super.add(e);
	}
	
	@Override
	public boolean addAll(Collection<? extends T> c) {
		// TODO Auto-generated method stub
		return super.addAll(c);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		// TODO Auto-generated method stub
		return super.addAll(index, c);
	}
}
