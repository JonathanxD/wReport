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
package io.github.jonathanxd.wreport.config;

import com.google.common.reflect.TypeToken;

import java.util.Optional;

import io.github.jonathanxd.wreport.exception.MissingReasonException;
import io.github.jonathanxd.wreport.exception.MissingRegistryException;
import io.github.jonathanxd.wreport.registry.Register;
import io.github.jonathanxd.wreport.reports.reasons.Reason;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

/**
 * Created by jonathan on 01/04/16.
 */
public class ReasonSerializer implements TypeSerializer<Reason> {

    private static final String reasonNode = "reason";
    private static final String reasonClassNode = "reasonClass";
    private static final String serializedNode = "serialized";
    private final Register<Class<? extends Reason>, Reason.Serializer<?>> serializerRegister;

    public ReasonSerializer(Register<Class<? extends Reason>, Reason.Serializer<?>> serializerRegister) {
        this.serializerRegister = serializerRegister;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Reason deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {

        ConfigurationNode nodeReason = value.getNode(reasonNode);


        String classString = nodeReason.getNode(reasonClassNode).getValue(TypeToken.of(String.class));

        try {
            Class<? extends Reason> classForName = (Class<? extends Reason>) Class.forName(classString);

            Optional<Reason.Serializer<?>> serializerOptional = serializerRegister.get(classForName);

            if(serializerOptional.isPresent()) {
                return serializerOptional.get().deserialize(nodeReason.getNode(serializedNode));
            } else {
                throw new MissingRegistryException("Missing deserialize register for reason '"+classForName.getCanonicalName()+"'");
            }

        } catch (ClassNotFoundException e) {
            throw new MissingReasonException("Reason missing!", e);
        }

    }

    @Override
    public void serialize(TypeToken<?> type, Reason obj, ConfigurationNode value) throws ObjectMappingException {

        ConfigurationNode nodeReason = value.getNode(reasonNode);

        nodeReason.getNode(reasonClassNode).setValue(obj.getClass().getName());

        Optional<Reason.Serializer<?>> serializerOptional = serializerRegister.get(obj.getClass());

        if(serializerOptional.isPresent()) {
            help(obj, serializerOptional.get(), nodeReason.getNode(serializedNode));
        } else {
            throw new MissingRegistryException("Missing deserialize register for reason '"+obj.getClass().getCanonicalName()+"'");
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Reason> void help(Object obj, Reason.Serializer<T> serializer, ConfigurationNode node) {
        serializer.serialize((T) obj, node);
    }
}
