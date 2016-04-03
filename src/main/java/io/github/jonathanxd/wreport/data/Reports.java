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

import com.github.jonathanxd.iutils.object.Reference;

import org.spongepowered.api.entity.living.player.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import io.github.jonathanxd.wreport.ConfigurationUpdater;
import io.github.jonathanxd.wreport.actions.Action;
import io.github.jonathanxd.wreport.actions.ActionProcessor;
import io.github.jonathanxd.wreport.filters.AllPlayersReportedByFilter;
import io.github.jonathanxd.wreport.filters.PlayerReportsFilter;
import io.github.jonathanxd.wreport.registry.Register;
import io.github.jonathanxd.wreport.reports.CloseReportData;
import io.github.jonathanxd.wreport.reports.IReportManager;
import io.github.jonathanxd.wreport.reports.Report;
import io.github.jonathanxd.wreport.reports.ReportType;
import io.github.jonathanxd.wreport.reports.reasons.Reason;

/**
 * Created by jonathan on 03/12/15.
 */
public final class Reports extends BaseData<Report, BaseData<User, Data<?>>> implements IReportManager {

    private final ConfigurationUpdater configurationUpdater;
    private final Register<Reference<?>, Action> actionRegister;
    private final ActionProcessor actionProcessor;
    List<Report> reportList = new LinkedList<>();

    public Reports(ConfigurationUpdater configurationUpdater, Register<Reference<?>, Action> actionRegister, ActionProcessor actionProcessor) {
        this.configurationUpdater = configurationUpdater;
        this.actionRegister = actionRegister;
        this.actionProcessor = actionProcessor;
    }

    @Override
    public Report report(ReportType reportType, @Nullable User source, @Nullable Collection<User> reportedUsers, Reason reportReason, String description) {
        Report report = new Report(reportType, reportReason, reportedUsers, source, description);

        reportList.add(report);

        BaseData<User, Data<?>> data = report.apply();

        super.setOwnerData(report, data);

        configurationUpdater.save();

        return report;
    }

    @Override
    public void endReport(Report report, User judge, String description, Action action, String actionData) {
        report.setCloseReportData(new CloseReportData(judge, description, action));
        actionProcessor.process(report, judge, report.getReportedPlayers().orElse(Collections.emptyList()), action, actionData);
    }


    @Override
    public Report report(ReportType reportType, Collection<User> reportedPlayers, Reason reportReason, String description) {
        return report(reportType, null, reportedPlayers, reportReason, description);
    }

    @Override
    public Report reportPlayer(User source, Collection<User> reportedPlayers, Reason reportReason, String description) {
        return report(ReportType.Normal.PLAYER_REPORT, source, reportedPlayers, reportReason, description);
    }

    @Override
    public Collection<Report> getPlayerReports(User reportedPlayer) {
        final Collection<Report> playerReports = new ArrayList<>();

        reportList.stream().filter(new PlayerReportsFilter(reportedPlayer)).forEach(playerReports::add);

        return playerReports;
    }

    @Override
    public Collection<Report> getAllPlayersReportedBy(User player) {
        final Collection<Report> reportedPlayersBy = new ArrayList<>();

        reportList.stream().filter(new AllPlayersReportedByFilter(player)).forEach(reportedPlayersBy::add);

        return reportedPlayersBy;
    }

    @Override
    public Collection<Report> getAllReports() {
        return Collections.unmodifiableList(reportList);
    }

    @Override
    public BaseData<Report, BaseData<User, Data<?>>> getReportDatas() {
        return this;
    }

    @Override
    public Optional<BaseData<User, Data<?>>> getSingleReportData(Report report) {
        return this.getData(report);
    }

    @Override
    public void addNewAppliedReport(Report report, BaseData<User, Data<?>> reportBaseData, boolean save) {
        reportList.add(report);

        if (save) configurationUpdater.save();

        super.setOwnerData(report, reportBaseData);
    }

    @Override
    public void exportTo(IReportManager to) {
        this.getAllReports().forEach(r -> to.addNewAppliedReport(r, this.getSingleReportData(r).orElseThrow(() -> new RuntimeException("Cannot transfer!")), false));
    }

    @Override
    public void importFrom(IReportManager from) {
        from.exportTo(this);
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder();

        reportList.forEach(r -> sb.append("Type: ").append(r.getReportType().getName()));

        return sb.toString();
    }
}
