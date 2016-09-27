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
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import io.github.jonathanxd.wreport.ConfigurationUpdater;
import io.github.jonathanxd.wreport.actions.Action;
import io.github.jonathanxd.wreport.actions.ActionProcessor;
import io.github.jonathanxd.wreport.config.tokens.GlobalTokens;
import io.github.jonathanxd.wreport.data.BaseData;
import io.github.jonathanxd.wreport.data.Data;
import io.github.jonathanxd.wreport.data.Reports;
import io.github.jonathanxd.wreport.registry.Register;
import io.github.jonathanxd.wreport.registry.registers.SerializersRegister;
import io.github.jonathanxd.wreport.reports.IReportManager;
import io.github.jonathanxd.wreport.reports.Report;
import io.github.jonathanxd.wreport.utils.func.ThrowingConsumer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

/**
 * Created by jonathan on 03/12/15.
 */
@SuppressWarnings("Duplicates")
public class PlayerReportSerializer implements TypeSerializer<IReportManager> {

    private static final String REPORT_NODE_STRING = "reports";
    private static final String SINGLE_REPORT_NODE_FORMAT = "report%s";

    private static final String DATA_NODE = "data";
    private static final String REPORT_NODE = "report";

    private final Game game;
    private final Logger logger;
    private final SerializersRegister serializersRegister;
    private final UserStorageService userStorageService;
    private final ConfigurationUpdater configurationUpdater;
    private final Register<TypeInfo<? extends Action>, Action> actionRegister;
    private final ActionProcessor actionProcessor;

    public PlayerReportSerializer(Game game, Logger logger, SerializersRegister serializersRegister, UserStorageService userStorageService, ConfigurationUpdater configurationUpdater, Register<TypeInfo<? extends Action>, Action> actionRegister, ActionProcessor actionProcessor) {
        this.game = game;
        this.logger = logger;
        this.serializersRegister = serializersRegister;
        this.userStorageService = userStorageService;
        this.configurationUpdater = configurationUpdater;
        this.actionRegister = actionRegister;
        this.actionProcessor = actionProcessor;
    }


    @SuppressWarnings("unchecked")
    @Override
    public IReportManager deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {

        IReportManager reportManager = new Reports(configurationUpdater, actionRegister, actionProcessor);

        ConfigurationNode reportsNode = value.getNode(REPORT_NODE_STRING);


        for (ConfigurationNode currentReportNode : reportsNode.getChildrenMap().values()) {


            Report report = currentReportNode.getNode(REPORT_NODE).getValue(TypeToken.of(Report.class));

            BaseData<User, Data<?>> baseData = currentReportNode.getNode(DATA_NODE).getValue(GlobalTokens.BASE_USER_DATA_TOKEN, new BaseData<User, Data<?>>());

            reportManager.addNewAppliedReport(report, baseData, false);

        }

        return reportManager;
    }

    @Override
    public void serialize(TypeToken<?> type, IReportManager reportManager, ConfigurationNode value) throws ObjectMappingException {

        int reportId = 0;

        for (Report report : reportManager.getAllReports()) {

            final String currentReportNodeString = String.format(SINGLE_REPORT_NODE_FORMAT, ++reportId);

            ConfigurationNode currentReportNode = value.getNode(REPORT_NODE_STRING, currentReportNodeString);

            currentReportNode.getNode(REPORT_NODE).setValue(TypeToken.of(Report.class), report);

            reportManager.getSingleReportData(report).ifPresent((ThrowingConsumer<BaseData<User, Data<?>>>) baseData -> currentReportNode.getNode(DATA_NODE).setValue(GlobalTokens.BASE_USER_DATA_TOKEN, baseData));
        }
    }
}
