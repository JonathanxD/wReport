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
package io.github.jonathanxd.wreport.registry;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import io.github.jonathanxd.wreport.reports.reasons.Reason;

public interface IReasonRegister {
	
	/**
	 * Register specifics reasons
	 * @param reasons Reason to register
	 * @return A collection of NOT registered reasons.
	 */
	Optional<Collection<Reason>> register(Reason... reasons);
	
	/**
	 * Register a collection of reasons (using {@link #register(Reason...)})
	 * @param reasonCollection Collection of reasons
	 * @return A collection of NOT registered reasons.
	 */
	Optional<Collection<Reason>> registerCollection(Collection<Reason> reasonCollection);
	
	/**
	 * Unregister specifics reasons
	 * @param reasons Reason to remove
	 * @return A collection of NOT removed reasons.
	 */
	Optional<Collection<Reason>> unregister(Reason... reasons);
	
	/**
	 * Remove a collection of reason (using {@link #unregister(Reason...)})
	 * @param reasonCollection Collection of reasons to remove
	 * @return A collection of NOT removed reasons.
	 */
	Optional<Collection<Reason>> unregisterCollection(Collection<Reason> reasonCollection);
	
	/**
	 * Check if a reason is registered
	 * @param reason Reason to check
	 * @return True if registered, false case not registered
	 */
	boolean isReasonRegistered(Reason reason);
	
	Collection<Reason> getRegisteredReasons();
	Map<String, Reason> mapOfRegisteredReasonsName();
}
