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
package io.github.jonathanxd.wreport.registry.registers;

import org.spongepowered.api.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.github.jonathanxd.wreport.exception.MissingRegistryException;
import io.github.jonathanxd.wreport.registry.Register;
import io.github.jonathanxd.wreport.reports.reasons.Reason;

/**
 * Created by jonathan on 01/04/16.
 */
public class ReasonSerializerRegister implements Register<Class<? extends Reason>, Reason.Serializer<?>> {

    Map<Class<? extends Reason>, Reason.Serializer<?>> map = new HashMap<>();


    @Override
    public boolean register(Object plugin, Game game, Class<? extends Reason> key, Reason.Serializer<?> obj) {

        map.put(key, obj);

        return true;
    }

    @Override
    public Optional<Reason.Serializer<?>> get(Class<? extends Reason> key) {
        return Optional.ofNullable(map.get(key));
    }

    @Override
    public Reason.Serializer<?> getUnchecked(Class<? extends Reason> key) throws MissingRegistryException {
        return get(key).orElseThrow(() -> new MissingRegistryException("Missing register for key: "+key));
    }

    @Override
    public String getName() {
        return "ReasonSerializerRegister";
    }
}
