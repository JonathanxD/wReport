package io.github.jonathanxd.wreport.history;

import java.util.Collection;
import java.util.Optional;

import org.spongepowered.api.entity.living.player.Player;

public class PlayerHistory<T> implements IHistory<Player, T>{

	private final IHistoryData<Player, T> historyData = new BaseHistoryData<>();
	
	@Override
	public void addToHistory(Player owner, T content) {
		historyData.add(owner, content);
		
	}

	@Override
	public void removeFromHistory(Player owner, int contentId) {
		historyData.remove(owner, contentId);		
	}

	@Override
	public Optional<Collection<T>> getLastHistory(Player owner, int initIndex, int endIndex) {
		return historyData.get(owner, initIndex, endIndex);
	}
	
	@Override
	public Optional<Collection<T>> getCompleteHistory(Player owner) {		
		return historyData.get(owner);
	}
	
	@Override
	public Optional<Integer> getHistorySize(Player owner) {
		return historyData.historySize(owner);
	}

	@Override
	public void garbageCollect() {
		for(Player player : historyData.getHistory().keySet()){
			if(!player.isOnline()){
				historyData.removeFromHistory(player);
			}
		}
		
	}

}
