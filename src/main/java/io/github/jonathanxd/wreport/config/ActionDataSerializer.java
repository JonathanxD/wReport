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

import com.github.jonathanxd.iutils.object.TypeInfo;

import org.spongepowered.api.entity.living.player.User;

import java.util.Collection;

import io.github.jonathanxd.wreport.actions.Action;
import io.github.jonathanxd.wreport.actions.ActionData;
import io.github.jonathanxd.wreport.config.tokens.GlobalTokens;
import io.github.jonathanxd.wreport.utils.func.ThrowingConsumer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

/**
 * Created by jonathan on 03/04/16.
 */
public class ActionDataSerializer implements TypeSerializer<ActionData> {
    private static final String CAUSER_NODE = "causer";
    private static final String AFFECTED_USERS_NODE = "affectedUsers";
    private static final String TYPE_NODE = "typeNode";
    private static final String ARGUMENTS_NODE = "arguments";

    @SuppressWarnings("unchecked")
    @Override
    public ActionData deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        try{
            User causer = value.getNode(CAUSER_NODE).getValue(TypeToken.of(User.class), (User) null);

            Collection<User> affectedUsers = value.getNode(AFFECTED_USERS_NODE).getValue(GlobalTokens.USERS_TOKEN);

            TypeInfo<? extends Action> reference = (TypeInfo<? extends Action>) TypeInfo.fromFullString(value.getNode(TYPE_NODE).getValue(TypeToken.of(String.class))).get(0);

            Collection<String> arguments = value.getNode(ARGUMENTS_NODE).getValue(GlobalTokens.STRING_COLL_TOKEN);

            return new ActionData(reference, causer, affectedUsers, arguments, null);

        }catch (Throwable t) {
            throw new RuntimeException(t);
        }

    }

    @Override
    public void serialize(TypeToken<?> type, ActionData obj, ConfigurationNode value) throws ObjectMappingException {

        obj.getCauser().ifPresent((ThrowingConsumer<User>) c -> value.getNode(CAUSER_NODE).setValue(TypeToken.of(User.class), c));

        value.getNode(AFFECTED_USERS_NODE).setValue(GlobalTokens.USERS_TOKEN, obj.getAffectedUsers());

        value.getNode(TYPE_NODE).setValue(TypeToken.of(String.class), obj.getReference().toFullString());

        value.getNode(ARGUMENTS_NODE).setValue(GlobalTokens.STRING_COLL_TOKEN, obj.getActionArguments());

    }
}
