package io.github.jonathanxd.wreport.filters;

import io.github.jonathanxd.wreport.reports.Report;
import org.spongepowered.api.entity.living.player.Player;

import java.util.function.Predicate;

/**
 * Created by jonathan on 05/12/15.
 */
public class AllPlayersReportedByFilter implements Predicate<Report> {

    private final Player reportRequester;

    public AllPlayersReportedByFilter(Player reportRequirer) {
        this.reportRequester = reportRequirer;
    }

    @Override
    public boolean test(Report report) {
        if(report.getReportRequirer().isPresent()){
            Player reportReportRequirer = report.getReportRequirer().get();
            if(reportReportRequirer.getUniqueId().equals(reportRequester.getUniqueId())){
                return true;
            }
        }
        return false;
    }
}
