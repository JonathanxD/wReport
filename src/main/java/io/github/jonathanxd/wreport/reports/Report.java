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

import com.google.common.base.Objects;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import io.github.jonathanxd.wreport.data.BaseData;
import io.github.jonathanxd.wreport.data.Data;
import io.github.jonathanxd.wreport.reports.reasons.Reason;

/**
 * Created by jonathan on 03/12/15.
 */
public class Report {

    private final long id;
    private final Instant creationDate;
    private final ReportType reportType;
    private final Reason reportReason;
    private final Collection<User> reportedPlayers;
    private final User reportApplicant;
    private final String description;

    private CloseReportData closeReportData = null;

    public Report(long id, Instant creationDate, ReportType reportType, Reason reportReason, Collection<User> reportedPlayers, User reportApplicant, String description) {
        this.id = id;
        this.creationDate = creationDate;
        this.reportType = reportType;
        this.reportReason = reportReason;
        this.reportedPlayers = reportedPlayers;
        this.reportApplicant = reportApplicant;
        this.description = description;
    }


    public Instant getCreationDate() {
        return creationDate;
    }

    public Optional<CloseReportData> getCloseReportData() {
        return Optional.ofNullable(closeReportData);
    }

    public void setCloseReportData(CloseReportData closeReportData) {
        if (this.closeReportData != null) {
            throw new IllegalStateException("Already closed!");
        }
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
                .add("id", getId())
                .add("type", reportType.getName())
                .add("reason", reportReason)
                .add("reportedPlayers", reportedPlayers)
                .add("requirer", reportApplicant)
                .add("description", description)
                .toString();
    }

    public long getId() {
        return id;
    }
}
