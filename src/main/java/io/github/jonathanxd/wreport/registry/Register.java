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

import com.github.jonathanxd.iutils.function.collector.BiCollectors;
import com.github.jonathanxd.iutils.function.stream.BiStream;
import com.github.jonathanxd.iutils.function.stream.MapStream;
import com.github.jonathanxd.iutils.object.Node;

import org.slf4j.Logger;
import org.spongepowered.api.Game;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.github.jonathanxd.wreport.exception.MissingRegistryException;

/**
 * Created by jonathan on 01/04/16.
 */
public interface Register<K, T> {

    String SUCCESS_MESSAGE = "Success registered %s!";
    String ERROR_MESSAGE = "Cannot register %s!";

    default void tryRegister(Object plugin, Game game, Logger logging, K key, T obj){
        if(register(plugin, game, key, obj)){
            logging.info(String.format(SUCCESS_MESSAGE, getName()));
        }else{
            logging.error(String.format(ERROR_MESSAGE, getName()));
        }
    }

    boolean register(Object plugin, Game game, K key, T obj);

    Optional<T> get(K key);

    default T getUnchecked(K key) throws MissingRegistryException {
        return get(key).orElseThrow(() -> new MissingRegistryException("Missing register for key: '"+key+"'"));
    }

    Collection<T> getAll();

    Map<K, T> getMap();

    default <E> Map<K, E> getAllAs(Function<T, E> valueFunction) {
        return getMap().isEmpty() ? Collections.emptyMap() : MapStream.of(getMap()).map((k, t) -> new Node<>(k, valueFunction.apply(t))).collect(BiCollectors.toMap());
    }

    default <E> Map<E, T> applyValueToKey(Function<T, E> valueFunction) {
        return getMap().isEmpty() ? Collections.emptyMap() : MapStream.of(getMap()).map((k, t) -> new Node<>(valueFunction.apply(t), t)).collect(BiCollectors.toMap());
    }

    String getName();

}
