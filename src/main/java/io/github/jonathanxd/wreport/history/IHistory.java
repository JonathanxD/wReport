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
