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
