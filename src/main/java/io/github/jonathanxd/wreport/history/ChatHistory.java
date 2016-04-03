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
package io.github.jonathanxd.wreport.history;

import com.google.common.reflect.TypeToken;

import java.util.List;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class ChatHistory extends PlayerHistory<String> {

    public static class Serializer implements io.github.jonathanxd.wreport.serializer.Serializer<BaseHistoryData.HistoryList<String>> {

        @Override
        public void serialize(BaseHistoryData.HistoryList<String> object, ConfigurationNode node) {
            try {
                node.setValue(TypeToken.of(List.class), object);
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public BaseHistoryData.HistoryList<String> deserialize(ConfigurationNode node) {

            BaseHistoryData.HistoryList historyList = new BaseHistoryData.HistoryList();

            try {
                List<String> value = node.getValue(new TypeToken<List<String>>() {});

                historyList.addAll(value);
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }

            return historyList;
        }
    }
}
