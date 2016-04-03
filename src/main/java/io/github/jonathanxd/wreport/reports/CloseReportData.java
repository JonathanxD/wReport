package io.github.jonathanxd.wreport.reports;

import org.spongepowered.api.entity.living.player.User;

import io.github.jonathanxd.wreport.actions.Action;

/**
 * Created by jonathan on 03/04/16.
 */
public class CloseReportData {

    private final User judge;
    private final String description;
    private final Action action;

    public CloseReportData(User judge, String description, Action action) {
        this.judge = judge;
        this.description = description;
        this.action = action;
    }

    public User getJudge() {
        return judge;
    }

    public Action getAction() {
        return action;
    }

    public String getDescription() {
        return description;
    }
}
