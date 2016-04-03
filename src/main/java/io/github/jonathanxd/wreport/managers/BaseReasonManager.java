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
package io.github.jonathanxd.wreport.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import io.github.jonathanxd.wreport.registry.IReasonRegister;
import io.github.jonathanxd.wreport.reports.reasons.Reason;

public class BaseReasonManager implements IReasonRegister{
	
	private Set<Reason> registeredReasonSet = new HashSet<>();

	@Override
	public Optional<Collection<Reason>> register(Reason... reasons) {
		
		Collection<Reason> alreadyRegisteredReasons = new ArrayList<>();
		
		for(Reason currentReason : reasons){
			if(isReasonRegistered(currentReason)){
				alreadyRegisteredReasons.add(currentReason);
				continue;
			}
			
			registeredReasonSet.add(currentReason);
		}
		
		return !alreadyRegisteredReasons.isEmpty() ? Optional.of(alreadyRegisteredReasons) : Optional.empty();
	}

	@Override
	public Optional<Collection<Reason>> registerCollection(Collection<Reason> reasonCollection) {
		Reason[] reasonArray = reasonCollection.toArray(new Reason[reasonCollection.size()]);
		
		return register(reasonArray);
	}

	@Override
	public Optional<Collection<Reason>> unregister(Reason... reasons) {
		Collection<Reason> notRegisteredReasons = new ArrayList<>();
		
		for(Reason currentReason : reasons){
			if(!isReasonRegistered(currentReason)){
				notRegisteredReasons.add(currentReason);
				continue;
			}
			
			registeredReasonSet.remove(currentReason);
		}
		
		return !notRegisteredReasons.isEmpty() ? Optional.of(notRegisteredReasons) : Optional.empty();
	}

	@Override
	public Optional<Collection<Reason>> unregisterCollection(Collection<Reason> reasonCollection) {
		Reason[] reasonArray = reasonCollection.toArray(new Reason[reasonCollection.size()]);
		
		return unregister(reasonArray);
	}
	
	@Override
	public boolean isReasonRegistered(Reason reason) {
		
		String reasonName = reason.getClass().getSimpleName();
		
		for(Reason currentReason : registeredReasonSet){
			String currentReasonName = currentReason.getClass().getSimpleName();
			
			if(currentReasonName.equals(reasonName)){
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public Collection<Reason> getRegisteredReasons() {
		return Collections.unmodifiableSet(registeredReasonSet);
	}
	
	@Override
	public Map<String, Reason> mapOfRegisteredReasonsName(){
		Map<String, Reason> namesMap = new HashMap<>();
		
		for(Reason currentReason : registeredReasonSet){
			String currentReasonName = currentReason.getClass().getSimpleName();
			namesMap.put(currentReasonName, currentReason);
		}
		
		return namesMap;
	}
}
