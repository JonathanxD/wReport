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
package io.github.jonathanxd.wreport.filters;

import io.github.jonathanxd.wreport.reports.Report;

import org.spongepowered.api.entity.living.player.User;

import java.util.function.Predicate;

/**
 * Created by jonathan on 05/12/15.
 */
public class AllPlayersReportedByFilter implements Predicate<Report> {

    private final User reportRequester;

    public AllPlayersReportedByFilter(User reportRequirer) {
        this.reportRequester = reportRequirer;
    }

    @Override
    public boolean test(Report report) {
        if(report.getReportApplicant().isPresent()){
            User reportReportRequirer = report.getReportApplicant().get();
            if(reportReportRequirer.getUniqueId().equals(reportRequester.getUniqueId())){
                return true;
            }
        }
        return false;
    }
}
