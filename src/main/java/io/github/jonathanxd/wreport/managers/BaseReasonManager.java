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
