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
package io.github.jonathanxd.wreport.history;

import java.util.Collection;
import java.util.Optional;

import org.spongepowered.api.entity.living.player.User;

public class PlayerHistory<T> implements IHistory<User, T> {

	private final IHistoryData<User, T> historyData = new BaseHistoryData<>();
	
	@Override
	public void addToHistory(User owner, T content) {
		historyData.add(owner, content);
		
	}

	@Override
	public void removeFromHistory(User owner, int contentId) {
		historyData.remove(owner, contentId);		
	}

	@Override
	public Optional<Collection<T>> getLastHistory(User owner, int initIndex, int endIndex) {
		return historyData.get(owner, initIndex, endIndex);
	}
	
	@Override
	public Optional<Collection<T>> getCompleteHistory(User owner) {
		return historyData.get(owner);
	}
	
	@Override
	public Optional<Integer> getHistorySize(User owner) {
		return historyData.historySize(owner);
	}

	@Override
	public void garbageCollect() {
		for(User player : historyData.getHistory().keySet()){
			if(!player.isOnline()){
				historyData.removeFromHistory(player);
			}
		}
		
	}

}
