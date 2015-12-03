package io.github.jonathanxd.wreport.data;

import io.github.jonathanxd.wreport.reports.Report;
import io.github.jonathanxd.wreport.reports.reasons.Reason;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jonathan on 03/12/15.
 */
public final class Reports {
    List<Report> reportList = new LinkedList<>();

    public void reportPlayer(Player source, Collection<Player> reportedPlayers, Reason reportReason){
        Report report = new Report(reportReason, reportedPlayers, source);
        reportList.add(report);
    }


}
