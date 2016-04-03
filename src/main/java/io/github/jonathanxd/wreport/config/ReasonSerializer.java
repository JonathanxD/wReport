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
