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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.github.jonathanxd.wreport.data.Data;
import io.github.jonathanxd.wreport.utils.list.MaxLinkedList;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class BaseHistoryData<T, E> implements IHistoryData<T, E> {

    Map<T, List<E>> historyDataMap = new HashMap<>();

    @Override
    public Map<T, List<E>> getHistory() {
        Map<T, List<E>> historyDataMapClone = Collections.unmodifiableMap(historyDataMap);

        return historyDataMapClone;
    }

    @Override
    public void add(T owner, E element) {
        add(owner, Arrays.asList(element));
    }

    @Override
    public void add(T owner, List<E> elementList) {
        List<E> mapElementList;

        if (!historyDataMap.containsKey(owner)) {
            mapElementList = new HistoryList<>();
        } else {
            mapElementList = historyDataMap.get(owner);
        }

        mapElementList.addAll(elementList);

        historyDataMap.put(owner, mapElementList);
    }

    /**
     * Remove element from the history based on index
     *
     * @param owner        Owner of the History
     * @param elementIndex Index to remove from history
     * @return A optional that indicate element removed or empty if element not present
     */
    @Override
    public Optional<E> remove(T owner, int elementIndex) {
        Optional<Collection<E>> removeReturn = remove(owner, Arrays.asList(elementIndex));

        if (!removeReturn.isPresent()) {
            return Optional.empty();
        }

        Collection<E> removeReturnList = removeReturn.get();

        return removeReturnList.stream().findFirst();
    }

    /**
     * Remove elements from the history based on index list
     *
     * @param owner            Owner of the History
     * @param elementIndexList Indexes to remove from history
     * @return A list containing all removed elements
     * @throws IndexOutOfBoundsException if informed range out of bound
     */
    @Override
    public Optional<Collection<E>> remove(T owner, Collection<Integer> elementIndexList) {

        if (!historyDataMap.containsKey(owner)) {
            return Optional.empty();
        }

        List<E> removedElementsList = new ArrayList<>();
        List<E> mapElementList = historyDataMap.get(owner);

        for (int currentElementIndex : elementIndexList) {
            if (currentElementIndex > -1 && currentElementIndex < elementIndexList.size()) {
                E removedElement = mapElementList.remove(currentElementIndex);

                if (removedElement != null) {
                    removedElementsList.add(removedElement);
                }

            } else {
                throw new IndexOutOfBoundsException(String.format("Index: %d. List size: %d", currentElementIndex, elementIndexList.size()));
            }
        }

        return Optional.of(removedElementsList);
    }

    @Override
    public Optional<Collection<E>> get(T owner) {
        if (!historyDataMap.containsKey(owner)) {
            return Optional.empty();
        }

        List<E> elementList = historyDataMap.get(owner);

        return Optional.of(elementList);
    }

    @Override
    public boolean historyExists(T owner) {
        return historyDataMap.containsKey(owner);
    }

    @Override
    public Optional<Integer> historySize(T owner) {
        if (!historyExists(owner)) {
            return Optional.empty();
        }

        return Optional.of(historyDataMap.get(owner).size());
    }

    @Override
    public Optional<Collection<E>> removeFromHistory(T owner) {
        if (historyExists(owner)) {
            Optional.of(historyDataMap.remove(owner));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Collection<E>> get(T owner, int startIndex, int endIndex) {
        if (!historyExists(owner)) {
            Optional.empty();
        }

        List<E> historyDataMapList = historyDataMap.get(owner);
        List<E> selectedElements = new ArrayList<>();

        for (int index = startIndex; index < endIndex + 1; ++index) {
            E element = historyDataMapList.get(index);
            selectedElements.add(element);
        }

        return Optional.of(selectedElements);
    }

    public static class HistoryList<E> extends MaxLinkedList<E> {

        public HistoryList() {
            super(IHistory.recommendedMaxSize);
        }

        public static class Serializer implements io.github.jonathanxd.wreport.serializer.Serializer<HistoryList<?>> {

            @Override
            public void serialize(Data<HistoryList<?>> object, ConfigurationNode node) {
                try {
                    node.setValue(new TypeToken<List<Object>>() {}, (List<Object>) object.getData());
                } catch (ObjectMappingException e) {
                    e.printStackTrace();
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public HistoryList<?> deserialize(ConfigurationNode node) {

                HistoryList historyList = new HistoryList();

                try {
                    List<Object> value = node.getValue(new TypeToken<List<Object>>() {
                    });
                    historyList.addAll(value);
                } catch (ObjectMappingException e) {
                    e.printStackTrace();
                }

                return historyList;
            }
        }
    }
}
