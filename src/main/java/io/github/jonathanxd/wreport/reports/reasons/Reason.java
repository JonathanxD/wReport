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
package io.github.jonathanxd.wreport.reports.reasons;

import com.google.common.reflect.TypeToken;

import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

import java.util.Optional;

import io.github.jonathanxd.wreport.data.Data;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

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

    class DefaultSerializer implements Serializer<Reason> {

        final static String REASON_MESSAGE_NODE = "reasonMessage";

        private final Class<? extends Reason> aClass;

        public DefaultSerializer(Class<? extends Reason> aClass) {
            this.aClass = aClass;
        }

        @Override
        public void serialize(Reason reason, ConfigurationNode node) {
            try {
                node.getNode(REASON_MESSAGE_NODE).setValue(TypeToken.of(Text.class), reason.reasonMessage());
            } catch (ObjectMappingException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Reason deserialize(ConfigurationNode node) {
            try {
                return aClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Serializer missing for Reason '"+aClass.getCanonicalName()+"'", e);
            }
        }
    }
}
