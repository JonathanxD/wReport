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

import org.spongepowered.api.entity.living.player.User;

import io.github.jonathanxd.wreport.actions.ActionData;
import io.github.jonathanxd.wreport.reports.CloseReportData;
import io.github.jonathanxd.wreport.utils.func.ThrowingConsumer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

/**
 * Created by jonathan on 03/04/16.
 */
public class CloseReportSerializer implements TypeSerializer<CloseReportData> {

    private static final String CAUSER_NODE = "causer";
    private static final String DESCRIPTION_NODE = "description";
    private static final String ACTION_DATA_NODE = "actionData";

    @Override
    public CloseReportData deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {

        User judge = value.getNode(CAUSER_NODE).getValue(TypeToken.of(User.class), (User) null);

        String description = value.getNode(DESCRIPTION_NODE).getValue(TypeToken.of(String.class), (String) null);

        ActionData actionData = value.getNode(ACTION_DATA_NODE).getValue(TypeToken.of(ActionData.class));

        return new CloseReportData(judge, description, actionData);
    }

    @Override
    public void serialize(TypeToken<?> type, CloseReportData obj, ConfigurationNode value) throws ObjectMappingException {

        obj.getJudge().ifPresent((ThrowingConsumer<User>) c -> value.getNode(CAUSER_NODE).setValue(TypeToken.of(User.class), c));

        value.getNode(DESCRIPTION_NODE).setValue(TypeToken.of(String.class), obj.getDescription());

        value.getNode(ACTION_DATA_NODE).setValue(TypeToken.of(ActionData.class), obj.getAction());
    }
}
