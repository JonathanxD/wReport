package io.github.jonathanxd.wreport.data;

import io.github.jonathanxd.wreport.filters.AllPlayersReportedByFilter;
import io.github.jonathanxd.wreport.filters.PlayerReportsFilter;
import io.github.jonathanxd.wreport.reports.IReportManager;
import io.github.jonathanxd.wreport.reports.Report;
import io.github.jonathanxd.wreport.reports.reasons.Reason;
import org.spongepowered.api.entity.living.player.Player;

import java.util.*;

/**
 * Created by jonathan on 03/12/15.
 */
public final class Reports extends BaseData<Report, BaseData<Player, Optional<Object>>> implements IReportManager{

    List<Report> reportList = new LinkedList<>();

    @Override
    public Report reportPlayer(Player source, Collection<Player> reportedPlayers, Reason reportReason){
        Report report = new Report(reportReason, reportedPlayers, source);
        reportList.add(report);

        BaseData<Player, Optional<Object>> data = report.apply();

        super.setOwnerData(report, data);
        return report;
    }

    @Override
    public Collection<Report> getPlayerReports(Player reportedPlayer){
        final Collection<Report> playerReports = new ArrayList<>();

        reportList.stream().filter(new PlayerReportsFilter(reportedPlayer)).forEach(playerReports::add);

        return playerReports;
    }

    @Override
    public Collection<Report> getAllPlayersReportedBy(Player player) {
        final Collection<Report> reportedPlayersBy = new ArrayList<>();

        reportList.stream().filter(new AllPlayersReportedByFilter(player)).forEach(reportedPlayersBy::add);

        return reportedPlayersBy;
    }

    @Override
    public Collection<Report> getAllReports() {
        return Collections.unmodifiableList(reportList);
    }

    @Override
    public BaseData<Report, BaseData<Player, Optional<Object>>> getReportDatas() {
        return this;
    }

    @Override
    public Optional<BaseData<Player, Optional<Object>>> getSingleReportData(Report report) {
        return this.getData(report);
    }

    @Override
    public void addNewAppliedReport(Report report, BaseData<Player, Optional<Object>> reportBaseData) {
        reportList.add(report);
        super.setOwnerData(report, reportBaseData);
    }

}
