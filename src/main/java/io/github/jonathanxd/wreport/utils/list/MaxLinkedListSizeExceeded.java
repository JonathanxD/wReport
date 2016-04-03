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
package io.github.jonathanxd.wreport.utils.list;

public class MaxLinkedListSizeExceeded extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3960654859472796030L;

	public MaxLinkedListSizeExceeded(String message, int size, MaxLinkedList<?> maxLinkedList) {
		super(String.format("%s. Size: %d. ListMaxSize: %d. Oversized: %d. Required Size: %d",
				message,
				size,
				maxLinkedList.getMaxListSize(), 
				getOverSize(size, maxLinkedList.getMaxListSize()),
				getRequiredSize(size, maxLinkedList.getMaxListSize())
				));
	}

	private static int getOverSize(int size, int maxListSize) {
		return (size - maxListSize);
	}

	private static int getRequiredSize(int size, int maxListSize) {
		return getOverSize(size, maxListSize) + maxListSize;
	}
}
