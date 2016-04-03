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

public interface IHistory<T, E> {

	public static final int recommendedMaxSize = 50;

	/**
	 * Add history to Owner
	 * @param owner Owner of the history
	 * @param content The history
	 */
	public void addToHistory(T owner, E content);

	/**
	 * Remove history based on owner and content index.
	 * @deprecated
	 * @param owner
	 * @param contentId
	 */
	public void removeFromHistory(T owner, int contentId);

	/**
	 * Get last history in a range (init -> end) based on owner
	 * @deprecated Use {@link #getCompleteHistory(Object)}
	 * @param owner Owner of history
	 * @param initIndex Start of list (commonly 0)
	 * @param endIndex End of list (commonly {@link #getHistorySize(Object)})
	 * @return The Optional containing history or empty if history not exists.
	 */
	public Optional<Collection<E>> getLastHistory(T owner, int initIndex, int endIndex);

	/**
	 * Get complete history (range: 0 to {@link #getHistorySize(Object)}) based on owner
	 * @param owner Owner of history
	 * @return The Optional containing history or empty if history not exists.
	 */
	public Optional<Collection<E>> getCompleteHistory(T owner);

	/**
	 * Get history size based on owner
	 * @param owner Owner of history
	 * @return History size or empty if history not exists.
	 */
	public Optional<Integer> getHistorySize(T owner);

	/**
	 * Collect unnecessary history (normally will be removed not present owners from history)
	 */
	public void garbageCollect();

}
