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
