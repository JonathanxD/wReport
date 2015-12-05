package io.github.jonathanxd.wreport.data;

import com.google.common.reflect.TypeToken;
import io.github.jonathanxd.wreport.reports.IReportManager;
import io.github.jonathanxd.wreport.reports.Report;
import io.github.jonathanxd.wreport.reports.reasons.Reason;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;

import java.util.*;
import java.util.Map.Entry;

/**
 * Created by jonathan on 03/12/15.
 */
public class PlayerReportData implements TypeSerializer<IReportManager> {

    private static int reportId = 0;
    private static final String reportNodeString = "reports";
    private static final String singleReportNodeFormat = "report%s";
    private static final String requesterNode = "requester";
    private static final String reasonNode = "reason";
    private static final String involvedNode = "involved";
    private static final String reportDataNode = "reportData";
    private static final String reportDataTypeNode = "reportDataType";
    private static final String reportDataObjectNode = "data";

    private final Game game;
    private final Logger logger;

    public PlayerReportData(Game game, Logger logger) {
        this.game = game;
        this.logger = logger;
    }


    @Override
    public IReportManager deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {

        IReportManager reportManager = new Reports();

        ConfigurationNode reportsNode = value.getNode(reportNodeString);

        for(ConfigurationNode currentReportNode : reportsNode.getChildrenList()){

            Optional<Player> requesterPlayer = Optional.empty();

            if(!currentReportNode.getNode(requesterNode).isVirtual()){
                ConfigurationNode configRequesterNode = currentReportNode.getNode(requesterNode);
                UUID playerUUID = configRequesterNode.getValue(TypeToken.of(UUID.class));
                requesterPlayer = game.getServer().getPlayer(playerUUID);
            }

            Reason reportReason = currentReportNode.getNode(reasonNode).getValue(TypeToken.of(Reason.class));

            Optional<Collection<Player>> involvedPlayers = Optional.empty();

            if(!currentReportNode.getNode(involvedNode).isVirtual()){
                ConfigurationNode configInvolvedNode = currentReportNode.getNode(involvedNode);
                List<UUID> involvedPlayerUUIDs = configInvolvedNode.getList(TypeToken.of(UUID.class));

                List<Player> involvedPlayerList = new ArrayList<>();

                for(UUID playerUUID : involvedPlayerUUIDs){
                    Optional<Player> playerOptional = game.getServer().getPlayer(playerUUID);
                    if(playerOptional.isPresent()){
                        involvedPlayerList.add(playerOptional.get());
                    }

                }

                involvedPlayers = Optional.of(involvedPlayerList);
            }

            BaseData<Player, Optional<Object>> baseReportData = new BaseData<>();

            if(!currentReportNode.getNode(reportDataNode).isVirtual()){

                BaseData<Player, Optional<Object>> newBaseData = new BaseData<>();

                ConfigurationNode configReportDataNode = currentReportNode.getNode(reportDataNode);

                for(Entry<Object, ? extends ConfigurationNode> playersDataNode : configReportDataNode.getChildrenMap().entrySet()){

                    UUID playerUUID = UUID.fromString(playersDataNode.getKey().toString());
                    Optional<Player> player = game.getServer().getPlayer(playerUUID);

                    if(player.isPresent()){
                        String dataTypeString = playersDataNode.getValue().getNode(reportDataTypeNode).getValue(TypeToken.of(String.class));
                        Class<?> dataType = null;
                        try {
                            dataType = Class.forName(dataTypeString);
                        } catch (ClassNotFoundException e) {
                            logger.error("Cannot deserialize reportData of UUID %s. Type %s not found!", playerUUID.toString(), dataTypeString);
                            break;
                        }
                        Object data = playersDataNode.getValue().getNode(reportDataObjectNode).getValue(TypeToken.of(dataType));

                        newBaseData.setOwnerData(player.get(), Optional.of(data));
                    }
                }
                if(!newBaseData.getDataStoreMap().isEmpty()){
                    baseReportData = newBaseData;
                }
            }
            reportManager.addNewAppliedReport(new Report(reportReason, involvedPlayers, requesterPlayer), baseReportData);

        }

        return null;
    }

    @Override
    public void serialize(TypeToken<?> type, IReportManager reportManager, ConfigurationNode value) throws ObjectMappingException {
        for(Report report : reportManager.getAllReports()){

            final String currentReportNodeString = String.format(singleReportNodeFormat, ++reportId);

            ConfigurationNode currentReportNode = value.getNode(reportNodeString, currentReportNodeString);

            if(report.getReportRequirer().isPresent()){
                Player reportRequester = report.getReportRequirer().get();
                currentReportNode.getNode(requesterNode).setValue(reportRequester.getUniqueId());
            }

            Reason reportReason = report.getReportReason();
            currentReportNode.getNode(reasonNode).setValue(reportReason);

            if(report.getReportedPlayers().isPresent()){
                ConfigurationNode configInvolvedNode = currentReportNode.getNode(involvedNode);
                Collection<Player> involvedPlayersList = report.getReportedPlayers().get();
                List<UUID> uuidList = new ArrayList<>();

                involvedPlayersList.forEach((player) -> uuidList.add(player.getUniqueId()));

                configInvolvedNode.setValue(uuidList);
            }

            Optional<BaseData<Player, Optional<Object>>> baseReportData;
            if((baseReportData = reportManager.getSingleReportData(report)).isPresent()){
                BaseData<Player, Optional<Object>> specificReportData = baseReportData.get();

                specificReportData.entrySet().stream().filter(reportDataEntry -> reportDataEntry.getValue().isPresent()).forEach(reportDataEntry -> {
                    Player playerReportData = reportDataEntry.getKey();
                    Object data = reportDataEntry.getValue().get();

                    ConfigurationNode playerDataNode = currentReportNode.getNode(reportDataNode, playerReportData.getUniqueId());
                    playerDataNode.getNode(reportDataTypeNode).setValue(data.getClass().getCanonicalName());
                    playerDataNode.getNode(reportDataObjectNode).setValue(data);
                });
            }
        }
    }
}
