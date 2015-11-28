package io.github.jonathanxd.wreport.history;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BaseHistoryData<T, E> implements IHistoryData<T, E> {

	Map<T, List<E>> historyDataMap = new HashMap<>();

	@Override
	public Map<T, List<E>> getHistory() {		
		Map<T, List<E>> historyDataMapClone = Collections.unmodifiableMap(historyDataMap);
		
		return historyDataMapClone;
	}
	
	@Override
	public void add(T owner, E element) {
		add(owner, Arrays.asList(element));
	}
	
	@Override
	public void add(T owner, List<E> elementList) {		
		List<E> mapElementList;

		if (!historyDataMap.containsKey(owner)) {
			mapElementList = new ArrayList<>();
		} else {
			mapElementList = historyDataMap.get(owner);
		}
		
		mapElementList.addAll(elementList);

		historyDataMap.put(owner, mapElementList);
	}
	
	/**
	 * Remove element from the history based on index
	 * 
	 * @param owner Owner of the History
	 * @param elementIndex Index to remove from history
	 * @return A optional that indicate element removed or empty if element not present
	 * 
	 */
	@Override
	public Optional<E> remove(T owner, int elementIndex) {
		Optional<Collection<E>> removeReturn = remove(owner, Arrays.asList(elementIndex));
		
		if(!removeReturn.isPresent()){
			return Optional.empty();
		}
		
		Collection<E> removeReturnList = removeReturn.get();
		
		return removeReturnList.stream().findFirst();
	}
	
	/**
	 * Remove elements from the history based on index list
	 * 
	 * @param owner Owner of the History
	 * @param elementIndexList Indexes to remove from history
	 * @return A list containing all removed elements
	 * @throws IndexOutOfBoundsException if informed range out of bound
	 */
	@Override
	public Optional<Collection<E>> remove(T owner, Collection<Integer> elementIndexList) {
		
		if(!historyDataMap.containsKey(owner)){
			return Optional.empty();
		}

		List<E> removedElementsList = new ArrayList<>();
		List<E> mapElementList = historyDataMap.get(owner);
		
		for(int currentElementIndex : elementIndexList){
			if(currentElementIndex > -1 && currentElementIndex < elementIndexList.size()){
				E removedElement = mapElementList.remove(currentElementIndex);
				
				if(removedElement != null){
					removedElementsList.add(removedElement);
				}
			
			}else{
				throw new IndexOutOfBoundsException(String.format("Index: %d. List size: %d", currentElementIndex, elementIndexList.size()));
			}
		}
		
		return Optional.of(removedElementsList);
	}
	
	@Override
	public Optional<Collection<E>> get(T owner) {
		if (!historyDataMap.containsKey(owner)) {
			return Optional.empty();
		}

		List<E> elementList = historyDataMap.get(owner);

		return Optional.of(elementList);
	}
	
	@Override
	public boolean historyExists(T owner) {		
		return historyDataMap.containsKey(owner);
	}
	
	@Override
	public Optional<Integer> historySize(T owner) {
		if(!historyExists(owner)){
			return Optional.empty();
		}
		
		return Optional.of(historyDataMap.get(owner).size());
	}
	
	@Override
	public Optional<Collection<E>> removeFromHistory(T owner) {
		if(historyExists(owner)){
			Optional.of(historyDataMap.remove(owner));			
		}
		return Optional.empty();
	}

	@Override
	public Optional<Collection<E>> get(T owner, int startIndex, int endIndex) {
		if(!historyExists(owner)){
			Optional.empty();
		}
		
		List<E> historyDataMapList = historyDataMap.get(owner);
		List<E> selectedElements = new ArrayList<>();
		
		for(int index = startIndex; index < endIndex+1; ++index){
			E element = historyDataMapList.get(index);
			selectedElements.add(element);
		}
		
		return Optional.of(selectedElements);
	}
}
