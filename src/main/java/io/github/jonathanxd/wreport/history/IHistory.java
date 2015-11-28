package io.github.jonathanxd.wreport.history;

import java.util.Collection;
import java.util.Optional;

public interface IHistory<T, E> {
	
	public static final int recommendedMaxSize = 50;
	
	public void addToHistory(T owner, E content);
	public void removeFromHistory(T owner, int contentId);
	public Optional<Collection<E>> getLastHistory(T owner, int initIndex, int endIndex);
	public void garbageCollect();
	
}
