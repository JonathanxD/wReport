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
package io.github.jonathanxd.wreport.reports.reasons;

import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

import java.util.Optional;

import io.github.jonathanxd.wreport.data.Data;
import ninja.leaping.configurate.ConfigurationNode;

public interface Reason {

    default Optional<Severity> severity() {
        return Optional.empty();
    }

    Text reasonMessage();

    default Optional<Data<?>> apply(User subject) {
        return Optional.empty();
    }

    interface Serializer<T extends Reason> {
        void serialize(T reason, ConfigurationNode node);

        T deserialize(ConfigurationNode node);
    }
}
