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

import org.slf4j.Logger;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.github.jonathanxd.wreport.data.BaseData;
import io.github.jonathanxd.wreport.data.Data;
import io.github.jonathanxd.wreport.registry.registers.SerializersRegister;
import io.github.jonathanxd.wreport.serializer.Serializer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

/**
 * Created by jonathan on 03/04/16.
 */
public class BaseUserDataSerializer implements TypeSerializer<BaseData<User, Data<?>>> {

    private static final String REPORT_DATA_NODE = "reportData";
    private static final String reportDataTypeNode = "reportDataType";
    private static final String reportDataObjectNode = "data";

    private final SerializersRegister serializersRegister;
    private final UserStorageService userStorageService;
    private final Logger logger;

    public BaseUserDataSerializer(SerializersRegister serializersRegister, UserStorageService userStorageService, Logger logger) {
        this.serializersRegister = serializersRegister;
        this.userStorageService = userStorageService;
        this.logger = logger;
    }


    @Override
    public BaseData<User, Data<?>> deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        BaseData<User, Data<?>> baseReportData = new BaseData<>();

        if (!value.isVirtual()) {

            BaseData<User, Data<?>> newBaseData = new BaseData<>();

            for (Map.Entry<Object, ? extends ConfigurationNode> playersDataNode : value.getChildrenMap().entrySet()) {

                UUID playerUUID = UUID.fromString(playersDataNode.getKey().toString());

                Optional<User> user = userStorageService.get(playerUUID);

                if (user.isPresent()) {

                    String dataTypeString = null;

                    try {
                        dataTypeString = playersDataNode.getValue().getNode(reportDataTypeNode).getValue(TypeToken.of(String.class));

                        TypeInfo<?> reference = TypeInfo.fromFullString(dataTypeString).get(0);

                        Data<?> data = new Data<>(reference, Serializer.helpDeserialize(serializersRegister.getUnchecked(reference), playersDataNode.getValue().getNode(reportDataObjectNode)));

                        newBaseData.setOwnerData(user.get(), data);
                    } catch (Throwable e) {
                        logger.error(String.format("Cannot deserialize reportData of UUID %s. Type %s not found!", playerUUID.toString(), dataTypeString));
                    }
                }
            }

            if (!newBaseData.getDataStoreMap().isEmpty()) {
                baseReportData = newBaseData;
            }
        }

        return baseReportData;
    }

    @Override
    public void serialize(TypeToken<?> type, BaseData<User, Data<?>> obj, ConfigurationNode value) throws ObjectMappingException {

        obj.entrySet().stream().filter(reportDataEntry -> reportDataEntry.getValue() != null).forEach(reportDataEntry -> {

            User userReportData = reportDataEntry.getKey();
            Data<?> data = reportDataEntry.getValue();

            ConfigurationNode playerDataNode = value.getNode(userReportData.getUniqueId());

            playerDataNode.getNode(reportDataTypeNode).setValue(data.getDataId().toFullString());

            Serializer.helpSerialize(data, serializersRegister.getUnchecked(data.getDataId()), playerDataNode.getNode(reportDataObjectNode));
        });
    }
}
