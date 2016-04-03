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
package io.github.jonathanxd.wreport.data;

import com.github.jonathanxd.iutils.object.Reference;

import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.channel.MessageReceiver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import io.github.jonathanxd.wreport.ConfigurationUpdater;
import io.github.jonathanxd.wreport.actions.Action;
import io.github.jonathanxd.wreport.actions.ActionData;
import io.github.jonathanxd.wreport.actions.ActionProcessor;
import io.github.jonathanxd.wreport.filters.AllPlayersReportedByFilter;
import io.github.jonathanxd.wreport.filters.PlayerReportsFilter;
import io.github.jonathanxd.wreport.list.ReportList;
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
    private final Register<Reference<? extends Action>, Action> actionRegister;
    private final ActionProcessor actionProcessor;
    List<Report> reportList = new ReportList();

    public Reports(ConfigurationUpdater configurationUpdater, Register<Reference<? extends Action>, Action> actionRegister, ActionProcessor actionProcessor) {
        this.configurationUpdater = configurationUpdater;
        this.actionRegister = actionRegister;
        this.actionProcessor = actionProcessor;
    }

    @Override
    public Report report(ReportType reportType, @Nullable User source, @Nullable Collection<User> reportedUsers, Reason reportReason, String description) {
        Report report = new Report(getFreeId(), reportType, reportReason, reportedUsers, source, description);

        reportList.add(report);

        BaseData<User, Data<?>> data = report.apply();

        super.setOwnerData(report, data);

        configurationUpdater.save();

        return report;
    }

    @Override
    public boolean endReport(Report report, User judge, String description, Action action, String actionData, Predicate<User> affectedUserPredicate, MessageReceiver messageReceiver) {
        Optional<ActionData> actionDataOptional = actionProcessor.process(report, messageReceiver, judge, report.getReportedPlayers().orElse(Collections.emptyList()).stream().filter(affectedUserPredicate).collect(Collectors.toList()), action, actionData);

        if(actionDataOptional.isPresent()) {
            report.setCloseReportData(new CloseReportData(judge, description, actionDataOptional.get()));
            return true;
        }

        return false;
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
    public Collection<Report> getAllReportedBy(User player) {
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
    public Report addNewAppliedReport(Report report, BaseData<User, Data<?>> reportBaseData, boolean save) {

        if(get(report.getId()).isPresent()) {
            report = recreate(getFreeId(), report);
        }

        reportList.add(report);

        if (save) configurationUpdater.save();

        super.setOwnerData(report, reportBaseData);

        return report;
    }

    private Report recreate(long id, Report report) {
        return new Report(id, report.getReportType(), report.getReportReason(), report.getReportedPlayers().orElse(null), report.getReportApplicant().orElse(null), report.getDescription());
    }

    @Override
    public long getFreeId() {

        long id = 0;

        while(get(id).isPresent()) {
            ++id;
        }

        return id;
    }

    @Override
    public Optional<Report> get(long id) {
        return reportList.stream().filter(c -> c.getId() == id).findAny();
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
