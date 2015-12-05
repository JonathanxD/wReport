package io.github.jonathanxd.wreport.reports;

import io.github.jonathanxd.wreport.data.BaseData;
import io.github.jonathanxd.wreport.reports.reasons.Reason;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by jonathan on 05/12/15.
 */
public interface IReportManager {

    Report reportPlayer(Player source, Collection<Player> reportedPlayers, Reason reportReason);
    Collection<Report> getPlayerReports(Player reportedPlayer);
    Collection<Report> getAllPlayersReportedBy(Player player);
    Collection<Report> getAllReports();
    BaseData<Report, BaseData<Player, Optional<Object>>> getReportDatas();
    Optional<BaseData<Player, Optional<Object>>> getSingleReportData(Report report);

    void addNewAppliedReport(Report report, BaseData<Player, Optional<Object>> reportBaseData);
}
