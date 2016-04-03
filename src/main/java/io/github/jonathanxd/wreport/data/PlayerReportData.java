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
package io.github.jonathanxd.wreport.data;

import com.google.common.reflect.TypeToken;

import com.github.jonathanxd.iutils.object.Reference;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import io.github.jonathanxd.wreport.ConfigurationUpdater;
import io.github.jonathanxd.wreport.registry.registers.SerializersRegister;
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
public class PlayerReportData implements TypeSerializer<IReportManager> {

    private static final String reportNodeString = "reports";
    private static final String singleReportNodeFormat = "report%s";
    private static final String requesterNode = "requester";
    private static final String typeNode = "type";
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

    public PlayerReportData(Game game, Logger logger, SerializersRegister serializersRegister, UserStorageService userStorageService, ConfigurationUpdater configurationUpdater) {
        this.game = game;
        this.logger = logger;
        this.serializersRegister = serializersRegister;
        this.userStorageService = userStorageService;
        this.configurationUpdater = configurationUpdater;
    }


    @Override
    public IReportManager deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {

        IReportManager reportManager = new Reports(configurationUpdater, actionRegister, actionProcessor);

        ConfigurationNode reportsNode = value.getNode(reportNodeString);


        for (ConfigurationNode currentReportNode : reportsNode.getChildrenMap().values()) {

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


            Report report = new Report(reportType, reportReason, involvedPlayers.orElse(null), requesterPlayer.orElse(null), description);

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
