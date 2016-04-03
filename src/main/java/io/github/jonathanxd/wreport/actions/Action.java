package io.github.jonathanxd.wreport.actions;

/**
 * Created by jonathan on 03/04/16.
 */

import com.github.jonathanxd.iutils.object.Reference;

/**
 * Represents an 'Action'
 *
 * Actions is called when a report is closed!
 *
 * Actions uses WCommand API to call Methods via Reflection.
 *
 * Provided Information:
 *
 * Game {@link org.spongepowered.api.Game}
 *
 * All Services Registered in {@link org.spongepowered.api.service.ServiceManager}
 *
 * Collection of {@link org.spongepowered.api.entity.living.player.User} tagged as
 * 'affectedPlayers'
 *
 * An {@link org.spongepowered.api.entity.living.player.User} tagged as 'causer' (ex: judge of
 * {@link io.github.jonathanxd.wreport.reports.CloseReportData})
 *
 * A {@link io.github.jonathanxd.wreport.reports.Report} tagged as 'report'.
 */
public interface Action {

    String getName();


    Reference<?> getReference();
}
