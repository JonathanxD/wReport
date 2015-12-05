package io.github.jonathanxd.wreport.filters;

import io.github.jonathanxd.wreport.reports.Report;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by jonathan on 05/12/15.
 */
public class PlayerReportsFilter implements java.util.function.Predicate<Report> {

    private final Player reportedPlayer;

    public PlayerReportsFilter(Player reportedPlayer) {
        this.reportedPlayer = reportedPlayer;
    }

    @Override
    public boolean test(Report report) {
        if(report.getReportedPlayers().isPresent()){
            if(report.getReportedPlayers().get().contains(reportedPlayer)){
                return true;
            }
        }
        return false;
    }
}
