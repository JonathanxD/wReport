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
package io.github.jonathanxd.wreport.reports;

import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.channel.MessageReceiver;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

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

    /**
     * Report a Player
     *
     * @param source          Applicant
     * @param reportedPlayers Reported Player
     * @param reportReason    Reason
     * @param description     Description of report
     * @return Created Report
     */
    Report reportPlayer(User source, Collection<User> reportedPlayers, Reason reportReason, String description);

    /**
     * Report
     *
     * @param reportType      Type of report
     * @param reportedPlayers Reported players
     * @param reportReason    Report reason
     * @param description     Description of Report
     * @return Created Report
     */
    Report report(ReportType reportType, Collection<User> reportedPlayers, Reason reportReason, String description);

    /**
     * Report
     *
     * @param reportType      Type of report
     * @param source          Applicant
     * @param reportedPlayers Reported Players
     * @param reportReason    Report Reason
     * @param description     Description of Report
     * @return Created Report
     */
    Report report(ReportType reportType, @Nullable User source, @Nullable Collection<User> reportedPlayers, Reason reportReason, String description);

    /**
     * Close a Report
     *
     * @param report                 Report
     * @param judge                  Judge (User that closed the report)
     * @param description            Close description
     * @param action                 Action
     * @param actionData             Action Data
     * @param affectedUsersPredicate Affected Players Predicate
     */
    boolean endReport(Report report, User judge, String description, Action action, String actionData, Predicate<User> affectedUsersPredicate, MessageReceiver messageReceiver);

    /**
     * Get all reports of a reported user (reported)
     *
     * @param reportedPlayer Reported User
     * @return Collection of Reports
     */
    Collection<Report> getPlayerReports(User reportedPlayer);

    /**
     * Get all reports created by a user (applicant)
     *
     * @param player Applicant User
     * @return Collection of Reports
     */
    Collection<Report> getAllReportedBy(User player);

    /**
     * Get all reports
     *
     * @return Collection of All Reports
     */
    Collection<Report> getAllReports();

    /**
     * Report Data
     *
     * @return Report Data
     */
    BaseData<Report, BaseData<User, Data<?>>> getReportDatas();

    /**
     * Get report data for specific Report
     *
     * @param report Report
     * @return Report Data
     */
    Optional<BaseData<User, Data<?>>> getSingleReportData(Report report);

    /**
     * Add applied report.
     *
     * @param report         Report
     * @param reportBaseData Report Data
     * @param save           Save report
     * @return Created report, if {@code report} id don't exists in list {@link #get(long)}, add the
     * instance, otherwise created a new instance with a free id {@link #getFreeId()}
     */
    Report addNewAppliedReport(Report report, BaseData<User, Data<?>> reportBaseData, boolean save);

    /**
     * Get id that haven't any report assigned.
     *
     * @return Free Id
     */
    long getFreeId();

    /**
     * Get report for id
     *
     * @param id Report id
     * @return Report
     */
    Optional<Report> get(long id);
}
