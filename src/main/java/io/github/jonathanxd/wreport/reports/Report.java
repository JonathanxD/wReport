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

import com.google.common.base.Objects;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;

import java.util.Collection;
import java.util.Optional;

import io.github.jonathanxd.wreport.actions.Action;
import io.github.jonathanxd.wreport.data.BaseData;
import io.github.jonathanxd.wreport.data.Data;
import io.github.jonathanxd.wreport.reports.reasons.Reason;

/**
 * Created by jonathan on 03/12/15.
 */
public class Report {

    private final ReportType reportType;
    private final Reason reportReason;
    private final Collection<User> reportedPlayers;
    private final User reportApplicant;
    private final String description;

    private CloseReportData closeReportData = null;

    public Report(ReportType reportType, Reason reportReason, Collection<User> reportedPlayers, User reportApplicant, String description) {
        this.reportType = reportType;
        this.reportReason = reportReason;
        this.reportedPlayers = reportedPlayers;
        this.reportApplicant = reportApplicant;
        this.description = description;
    }

    public void setCloseReportData(CloseReportData closeReportData) {
        java.util.Objects.requireNonNull(this.closeReportData, "Already closed!");
        this.closeReportData = closeReportData;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public Reason getReportReason() {
        return reportReason;
    }

    public String getDescription() {
        return description;
    }

    public Optional<User> getReportApplicant() {
        return Optional.ofNullable(reportApplicant);
    }

    public Optional<Collection<User>> getReportedPlayers() {
        return Optional.ofNullable(reportedPlayers);
    }

    public boolean isReporter(User subject) {
        if (!getReportApplicant().isPresent()) return false;
        return subject.getUniqueId().equals(reportApplicant.getUniqueId());
    }

    public boolean isReporter(Player subject) {
        return isReporter((User) subject);
    }

    /**
     * @return Report reason apply data for Players or Bug
     */
    public BaseData<User, Data<?>> apply() {

        BaseData<User, Data<?>> baseData = new BaseData<>();

        if (getReportApplicant().isPresent()) {
            User reporter = reportApplicant;
            Optional<Data<?>> reasonResult = reportReason.apply(reporter);


            if (reasonResult.isPresent()) {
                baseData.setOwnerData(reporter, reasonResult.get());
            }
        }

        if (getReportedPlayers().isPresent()) {
            for (User involved : reportedPlayers) {

                Optional<Data<?>> reasonResult = reportReason.apply(involved);

                if (reasonResult.isPresent()) {
                    baseData.setOwnerData(involved, reasonResult.get());
                }

            }
        }

        return baseData;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        if (hash < 0) {
            hash = -(hash);
            hash *= 3;
        } else {
            hash *= 2;
        }

        return hash;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(Report.class)
                .add("type", reportType.getName())
                .add("reason", reportReason)
                .add("reportedPlayers", reportedPlayers)
                .add("requirer", reportApplicant)
                .add("description", description)
                .toString();
    }
}
