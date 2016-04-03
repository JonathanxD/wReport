package io.github.jonathanxd.wreport.registry.registers;

import org.spongepowered.api.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.github.jonathanxd.wreport.registry.Register;

/**
 * Created by jonathan on 03/04/16.
 */
public abstract class AbstractMapRegister<K, T> implements Register<K, T> {
    Map<K, T> map = new HashMap<>();

    @Override
    public boolean register(Object plugin, Game game, K key, T obj) {

        if (map.containsKey(key))
            throw new IllegalArgumentException("Key already registered! Key: '" + key + "' for object: '" + obj + "'");

        map.put(key, obj);

        return true;
    }

    @Override
    public Optional<T> get(K key) {
        return Optional.ofNullable(map.get(key));
    }

    @Override
    public String getName() {
        return this.getClass().getCanonicalName();
    }
}
