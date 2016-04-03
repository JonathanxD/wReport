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
