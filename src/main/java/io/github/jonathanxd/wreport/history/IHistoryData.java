package io.github.jonathanxd.wreport.history;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IHistoryData<T, E> {
	Map<T, List<E>> getHistory();
	
	void add(T owner, E element);
	void add(T owner, List<E> elementList);

	Optional<Collection<E>> get(T owner);
	Optional<Collection<E>> get(T owner, int startIndex, int endIndex);

	Optional<Collection<E>> removeFromHistory(T owner);
	
	Optional<E> remove(T owner, int elementList);
	Optional<Collection<E>> remove(T owner, Collection<Integer> elementList);

	
	Optional<Integer> historySize(T owner);
	boolean historyExists(T owner);
}
