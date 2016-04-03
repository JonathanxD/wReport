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
package io.github.jonathanxd.wreport.data;

import com.github.jonathanxd.iutils.object.Reference;

import java.util.*;

/**
 * Created by jonathan on 03/12/15.
 */
public class BaseData<OWNER, DATA extends Data> extends Data<BaseData<OWNER, DATA>> {

    private Map<OWNER, DATA> dataStoreMap = new HashMap<>();

    public BaseData() {
        super(Reference.aEnd(BaseData.class));
    }

    public final boolean contains(OWNER owner){
        return dataStoreMap.containsKey(owner);
    }

    public final Optional<DATA> getData(OWNER owner){
        return contains(owner) ? Optional.of(dataStoreMap.get(owner))
            : Optional.empty();
    }

    public final Optional<DATA> setOwnerData(OWNER owner, DATA data){

        DATA oldData = dataStoreMap.put(owner, data);
        return Optional.ofNullable(oldData);
    }

    public Map<OWNER, DATA> getDataStoreMap() {
        return Collections.synchronizedMap(dataStoreMap);
    }

    public Set<Map.Entry<OWNER, DATA>> entrySet(){
        return dataStoreMap.entrySet();
    }
}
