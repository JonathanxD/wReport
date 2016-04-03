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
package io.github.jonathanxd.wreport.config;

import com.google.common.reflect.TypeToken;

import java.util.UUID;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

/**
 * Created by jonathan on 01/04/16.
 */
public class UUIDSerializer implements TypeSerializer<UUID> {

    @Override
    public UUID deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        return UUID.fromString(value.getValue(TypeToken.of(String.class)));
    }

    @Override
    public void serialize(TypeToken<?> type, UUID obj, ConfigurationNode value) throws ObjectMappingException {
        value.setValue(obj.toString());
    }
}
