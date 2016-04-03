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
package io.github.jonathanxd.wreport.serializer;

import ninja.leaping.configurate.ConfigurationNode;

/**
 * Created by jonathan on 02/04/16.
 */
public interface Serializer<T> {

    void serialize(T object, ConfigurationNode node);

    T deserialize(ConfigurationNode node);

    @SuppressWarnings("unchecked")
    static <T> void helpSerialize(Object o, Serializer<T> serializer, ConfigurationNode node) {
        serializer.serialize((T) o, node);
    }

    @SuppressWarnings("unchecked")
    static <T> T helpDeserialize(Serializer<?> serializer, ConfigurationNode node) {
        return (T) serializer.deserialize(node);
    }
}
