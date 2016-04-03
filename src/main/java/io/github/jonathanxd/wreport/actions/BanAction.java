package io.github.jonathanxd.wreport.actions;

import com.github.jonathanxd.iutils.object.Reference;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.infos.Info;
import com.github.jonathanxd.wcommands.infos.Information;

import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.ban.BanService;

import java.time.Instant;
import java.util.Collection;

import io.github.jonathanxd.wreport.reports.Report;

/**
 * Created by jonathan on 03/04/16.
 */
public class BanAction implements Action {


    @Command
    public void ban(@Argument(id = "Ban Time") Instant time,
                    @Info Information<Report> reportInformation,
                    @Info(tags = "affectedPlayers") Information<Collection<User>> affectedPlayers,
                    @Info Information<BanService> banServiceInfo) {



    }

    @Override
    public String getName() {
        return "Ban";
    }

    @Override
    public Reference<?> getReference() {
        return Reference.aEnd(BanAction.class);
    }
}
