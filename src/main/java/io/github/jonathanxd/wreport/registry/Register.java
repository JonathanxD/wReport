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

import org.slf4j.Logger;
import org.spongepowered.api.Game;

import java.util.Optional;

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

    String getName();

}
