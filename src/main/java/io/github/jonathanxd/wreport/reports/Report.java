package io.github.jonathanxd.wreport.reports;

import io.github.jonathanxd.wreport.data.BaseData;
import io.github.jonathanxd.wreport.reports.reasons.Reason;
import org.spongepowered.api.entity.living.player.Player;

import java.util.*;

/**
 * Created by jonathan on 03/12/15.
 */
public class Report {

    private final Reason reportReason;
    private final Optional<Collection<Player>> reportedPlayers;
    private final Optional<Player> reportRequirer;

    public Report(Reason reportReason, Collection<Player> reportedPlayers, Player reportRequirer) {
        this.reportReason = reportReason;
        this.reportedPlayers = Optional.ofNullable(reportedPlayers);
        this.reportRequirer = Optional.ofNullable(reportRequirer);
    }

    public Reason getReportReason() {
        return reportReason;
    }

    public Optional<Player> getReportRequirer() {
        return reportRequirer;
    }

    public Optional<Collection<Player>> getReportedPlayers() {
        return reportedPlayers;
    }

    public boolean isReporter(Player subject){
        if(!reportRequirer.isPresent()) return false;
        return subject.getUniqueId().equals(reportRequirer.get().getUniqueId());
    }

    /**
     *
     * @return Report reason apply data for Players or Bug
     */
    public BaseData<Player, Optional<Object>> apply(){

        BaseData<Player, Optional<Object>> baseData = new BaseData<>();

        if(reportRequirer.isPresent()){
            Player reporter = reportRequirer.get();
            Optional<Object> reasonResult = reportReason.apply(Optional.of(reporter));
            baseData.setOwnerData(reporter, reasonResult);
        }

        if(reportedPlayers.isPresent()){
            for(Player involved : reportedPlayers.get()){

                Optional<Object> reasonResult = reportReason.apply(Optional.of(involved));
                baseData.setOwnerData(involved, reasonResult);
            }
        }

        return baseData;
    }
}
