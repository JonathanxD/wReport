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
package io.github.jonathanxd.wreport.reports;

import org.spongepowered.api.entity.living.player.User;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.Nullable;

import io.github.jonathanxd.wreport.actions.Action;
import io.github.jonathanxd.wreport.data.BaseData;
import io.github.jonathanxd.wreport.data.Data;
import io.github.jonathanxd.wreport.reports.reasons.Reason;
import io.github.jonathanxd.wreport.utils.Transferable;

/**
 * Created by jonathan on 05/12/15.
 */
public interface IReportManager extends Transferable<IReportManager> {

    Report reportPlayer(User source, Collection<User> reportedPlayers, Reason reportReason, String description);

    Report report(ReportType reportType, Collection<User> reportedPlayers, Reason reportReason, String description);

    Report report(ReportType reportType, @Nullable User source, @Nullable Collection<User> reportedPlayers, Reason reportReason, String description);

    void endReport(Report report, User judge, String description, Action action, String actionData);

    Collection<Report> getPlayerReports(User reportedPlayer);

    Collection<Report> getAllPlayersReportedBy(User player);

    Collection<Report> getAllReports();

    BaseData<Report, BaseData<User, Data<?>>> getReportDatas();

    Optional<BaseData<User, Data<?>>> getSingleReportData(Report report);

    void addNewAppliedReport(Report report, BaseData<User, Data<?>> reportBaseData, boolean save);
}
