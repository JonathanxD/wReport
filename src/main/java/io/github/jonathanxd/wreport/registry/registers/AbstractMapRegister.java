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
package io.github.jonathanxd.wreport.registry.registers;

import org.spongepowered.api.Game;

import java.util.Collection;
import java.util.Collections;
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
    public Map<K, T> getMap() {
        return Collections.unmodifiableMap(map);
    }

    @Override
    public Collection<T> getAll() {
        return Collections.unmodifiableCollection(map.values());
    }

    @Override
    public String getName() {
        return this.getClass().getCanonicalName();
    }
}
