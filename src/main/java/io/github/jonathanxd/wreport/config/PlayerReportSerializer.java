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

import com.github.jonathanxd.iutils.object.Reference;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import io.github.jonathanxd.wreport.ConfigurationUpdater;
import io.github.jonathanxd.wreport.actions.Action;
import io.github.jonathanxd.wreport.actions.ActionData;
import io.github.jonathanxd.wreport.actions.ActionProcessor;
import io.github.jonathanxd.wreport.data.BaseData;
import io.github.jonathanxd.wreport.data.Data;
import io.github.jonathanxd.wreport.data.Reports;
import io.github.jonathanxd.wreport.registry.Register;
import io.github.jonathanxd.wreport.registry.registers.SerializersRegister;
import io.github.jonathanxd.wreport.reports.CloseReportData;
import io.github.jonathanxd.wreport.reports.IReportManager;
import io.github.jonathanxd.wreport.reports.Report;
import io.github.jonathanxd.wreport.reports.ReportType;
import io.github.jonathanxd.wreport.reports.reasons.Reason;
import io.github.jonathanxd.wreport.serializer.Serializer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

/**
 * Created by jonathan on 03/12/15.
 */
public class PlayerReportSerializer implements TypeSerializer<IReportManager> {

    private static final String reportNodeString = "reports";
    private static final String singleReportNodeFormat = "report%s";
    private static final String requesterNode = "requester";
    private static final String typeNode = "type";
    private static final String idNode = "id";

    private static final String closedNode = "closed";
    private static final String causerNode = "causer";

    private static final String actionDataNode = "actionData";
    private static final String affectedPlayersNode = "affectedPlayers";
    private static final String argumentsNode = "arguments";

    private static final String reasonNode = "reason";
    private static final String involvedNode = "involved";
    private static final String reportDataNode = "reportData";
    private static final String descriptionNode = "description";
    private static final String reportDataTypeNode = "reportDataType";
    private static final String reportDataObjectNode = "data";
    private final Game game;
    private final Logger logger;
    private final SerializersRegister serializersRegister;
    private final UserStorageService userStorageService;
    private final ConfigurationUpdater configurationUpdater;
    private final Register<Reference<? extends Action>, Action> actionRegister;
    private final ActionProcessor actionProcessor;

    public PlayerReportSerializer(Game game, Logger logger, SerializersRegister serializersRegister, UserStorageService userStorageService, ConfigurationUpdater configurationUpdater, Register<Reference<? extends Action>, Action> actionRegister, ActionProcessor actionProcessor) {
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

        ConfigurationNode reportsNode = value.getNode(reportNodeString);


        for (ConfigurationNode currentReportNode : reportsNode.getChildrenMap().values()) {


            long id = currentReportNode.getNode(idNode).getLong();

            Optional<User> requesterPlayer = Optional.empty();

            if (!currentReportNode.getNode(requesterNode).isVirtual()) {
                ConfigurationNode configRequesterNode = currentReportNode.getNode(requesterNode);
                UUID playerUUID = configRequesterNode.getValue(TypeToken.of(UUID.class));
                requesterPlayer = userStorageService.get(playerUUID);
            }

            ReportType reportType = currentReportNode.getNode(typeNode).getValue(TypeToken.of(ReportType.class));

            Reason reportReason = currentReportNode.getNode(reasonNode).getValue(TypeToken.of(Reason.class));

            String description = currentReportNode.getNode(descriptionNode).getValue(TypeToken.of(String.class));

            Optional<Collection<User>> involvedPlayers = Optional.empty();

            if (!currentReportNode.getNode(involvedNode).isVirtual()) {
                ConfigurationNode configInvolvedNode = currentReportNode.getNode(involvedNode);

                List<UUID> involvedPlayerUUIDs = configInvolvedNode.getValue(new TypeToken<List<UUID>>() {});

                List<User> involvedPlayerList = new ArrayList<>();

                for (UUID playerUUID : involvedPlayerUUIDs) {
                    Optional<User> playerOptional = userStorageService.get(playerUUID);
                    if (playerOptional.isPresent()) {
                        involvedPlayerList.add(playerOptional.get());
                    } else {
                        logger.error("Cannot find player from UUID: "+playerUUID);
                    }

                }

                System.out.println("Involved UUIDS: "+involvedPlayerUUIDs);

                involvedPlayers = Optional.of(involvedPlayerList);
            }


            BaseData<User, Data<?>> baseReportData = new BaseData<>();

            if (!currentReportNode.getNode(reportDataNode).isVirtual()) {

                BaseData<User, Data<?>> newBaseData = new BaseData<>();

                ConfigurationNode configReportDataNode = currentReportNode.getNode(reportDataNode);

                for (Entry<Object, ? extends ConfigurationNode> playersDataNode : configReportDataNode.getChildrenMap().entrySet()) {

                    UUID playerUUID = UUID.fromString(playersDataNode.getKey().toString());

                    Optional<User> user = userStorageService.get(playerUUID);

                    if (user.isPresent()) {

                        String dataTypeString = playersDataNode.getValue().getNode(reportDataTypeNode).getValue(TypeToken.of(String.class));

                        try {

                            Reference<?> reference = Reference.fromFullString(dataTypeString).get(0);

                            Data<?> data = new Data<>(reference, Serializer.helpDeserialize(serializersRegister.getUnchecked(reference), playersDataNode.getValue().getNode(reportDataObjectNode)));

                            newBaseData.setOwnerData(user.get(), data);
                        } catch (Throwable e) {
                            logger.error(String.format("Cannot deserialize reportData of UUID %s. Type %s not found!", playerUUID.toString(), dataTypeString));
                            break;
                        }


                    }
                }
                if (!newBaseData.getDataStoreMap().isEmpty()) {
                    baseReportData = newBaseData;
                }
            }


            Report report = new Report(id, reportType, reportReason, involvedPlayers.orElse(null), requesterPlayer.orElse(null), description);

            // DESERIALIZE CLOSED REPORT DATA

            Optional<CloseReportData> closeReportData = Optional.empty();

            ConfigurationNode closedReportNode = currentReportNode.getNode(closedNode);

            if(!closedReportNode.isVirtual()) {

                User causer;
                String closedDescription;
                ActionData actionData;


                ConfigurationNode causerClosedNode = closedReportNode.getNode(causerNode);


                causer = causerClosedNode.getValue(TypeToken.of(User.class), (User) null);

                closedDescription = closedReportNode.getNode(descriptionNode).getValue(TypeToken.of(String.class), (String) null);


                ConfigurationNode actionDataNd = closedReportNode.getNode(actionDataNode);

                User actionCauser = actionDataNd.getNode(causerNode).getValue(TypeToken.of(User.class), (User) null);

                Collection<User> affectedPlayers = actionDataNd.getNode(affectedPlayersNode).getValue(new TypeToken<Collection<User>>() {}, Collections.emptyList());

                Collection<String> actionArguments = actionDataNd.getNode(argumentsNode).getValue(new TypeToken<Collection<String>>() {}, Collections.emptyList());

                Reference<? extends Action> reference = null;

                try {

                    ConfigurationNode typeActionNode = actionDataNd.getNode(typeNode);

                    if(!typeActionNode.isVirtual()) {
                        reference = (Reference<? extends Action>) Reference.fromFullString(typeActionNode.getValue(TypeToken.of(String.class))).get(0);
                    }
                } catch (ClassNotFoundException e) {
                    throw new ObjectMappingException("Cannot translate reference!", e);
                }

                actionData = new ActionData(reference, actionCauser, affectedPlayers, actionArguments, report);

                closeReportData = Optional.of(new CloseReportData(causer, closedDescription, actionData));
            }

            // /DESERIALIZE CLOSED REPORT DATA

            if(closeReportData.isPresent()) {
                report.setCloseReportData(closeReportData.get());
            }

            logger.info("Loaded report: "+report);

            reportManager.addNewAppliedReport(report, baseReportData, false);



        }

        return reportManager;
    }

    @Override
    public void serialize(TypeToken<?> type, IReportManager reportManager, ConfigurationNode value) throws ObjectMappingException {

        int reportId = 0;

        for (Report report : reportManager.getAllReports()) {

            final String currentReportNodeString = String.format(singleReportNodeFormat, ++reportId);

            ConfigurationNode currentReportNode = value.getNode(reportNodeString, currentReportNodeString);

            currentReportNode.getNode(idNode).setValue(report.getId());

            if (report.getReportApplicant().isPresent()) {
                User reportRequester = report.getReportApplicant().get();
                currentReportNode.getNode(requesterNode).setValue(TypeToken.of(UUID.class), reportRequester.getUniqueId());
            }

            ReportType reportType = report.getReportType();
            Reason reportReason = report.getReportReason();
            String description = report.getDescription();

            currentReportNode.getNode(typeNode).setValue(TypeToken.of(ReportType.class), reportType);
            currentReportNode.getNode(reasonNode).setValue(TypeToken.of(Reason.class), reportReason);
            currentReportNode.getNode(descriptionNode).setValue(description);

            if (report.getReportedPlayers().isPresent()) {
                ConfigurationNode configInvolvedNode = currentReportNode.getNode(involvedNode);
                Collection<User> involvedPlayersList = report.getReportedPlayers().get();
                List<UUID> uuidList = new ArrayList<>();

                involvedPlayersList.forEach((player) -> uuidList.add(player.getUniqueId()));

                configInvolvedNode.setValue(new TypeToken<List<UUID>>() {
                }, uuidList);
            }

            // SERIALIZE CLOSED REPORT DATA

            Optional<CloseReportData> closeReportDataOpt = report.getCloseReportData();

            if(closeReportDataOpt.isPresent()) {

                CloseReportData closeReportData = closeReportDataOpt.get();

                ConfigurationNode closeNode = currentReportNode.getNode(closedNode);

                if(closeReportData.getJudge().isPresent()) {
                    closeNode.getNode(causerNode).setValue(TypeToken.of(User.class), closeReportData.getJudge().get());
                }

                if(closeReportData.getDescription() != null) {
                    closeNode.getNode(descriptionNode).setValue(TypeToken.of(String.class), closeReportData.getDescription());
                }

                ConfigurationNode actionDataNd = closeNode.getNode(actionDataNode);

                ActionData actionData = closeReportData.getAction();

                if(actionData.getCauser().isPresent()) {
                    actionDataNd.getNode(causerNode).setValue(TypeToken.of(User.class), actionData.getCauser().get());
                }

                if(actionData.getAffectedUsers() != null) {
                    actionDataNd.getNode(affectedPlayersNode).setValue(new TypeToken<Collection<User>>() {}, actionData.getAffectedUsers());
                }

                if(actionData.getReference() != null) {
                    actionDataNd.getNode(typeNode).setValue(TypeToken.of(String.class), actionData.getReference().toFullString());
                }

                if(actionData.getActionArguments() != null) {
                    actionDataNd.getNode(argumentsNode).setValue(new TypeToken<Collection<String>>() {}, actionData.getActionArguments());
                }

            }

            // /SERIALIZE CLOSED REPORT DATA

            Optional<BaseData<User, Data<?>>> baseReportData;
            if ((baseReportData = reportManager.getSingleReportData(report)).isPresent()) {
                BaseData<User, Data<?>> specificReportData = baseReportData.get();

                specificReportData.entrySet().stream().filter(reportDataEntry -> reportDataEntry.getValue() != null).forEach(reportDataEntry -> {
                    User userReportData = reportDataEntry.getKey();
                    Data<?> data = reportDataEntry.getValue();

                    ConfigurationNode playerDataNode = currentReportNode.getNode(reportDataNode, userReportData.getUniqueId());
                    playerDataNode.getNode(reportDataTypeNode).setValue(data.getReference().toFullString());


                    Serializer.helpSerialize(data, serializersRegister.getUnchecked(data.getReference()), playerDataNode.getNode(reportDataObjectNode));

                });
            }
        }
    }
}
