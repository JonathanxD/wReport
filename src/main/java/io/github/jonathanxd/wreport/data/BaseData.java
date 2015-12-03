package io.github.jonathanxd.wreport.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by jonathan on 03/12/15.
 */
public class BaseData<OWNER, DATA> {

    private Map<OWNER, DATA> dataStoreMap = new HashMap<>();

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
}
