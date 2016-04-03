package io.github.jonathanxd.wreport.actions;

import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.commandstring.CommonCommandStringParser;
import com.github.jonathanxd.wcommands.infos.InfoId;
import com.github.jonathanxd.wcommands.infos.Information;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.requirements.Requirements;

import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.ServiceManager;

import java.util.Collection;
import java.util.Optional;

import io.github.jonathanxd.wreport.reports.Report;

/**
 * Created by jonathan on 03/04/16.
 */
public class ActionProcessor {

    private final Game game;
    private final ServiceManager serviceManager;
    private final WCommandCommon wCommandCommon;
    private final InformationRegister.InformationBuilder informationBuilder;
    private final Requirements requirements;
    private final CommonCommandStringParser commonCommandStringParser = new CommonCommandStringParser();

    public ActionProcessor(Game game, WCommandCommon wCommandCommon) {
        this.game = game;
        this.serviceManager = game.getServiceManager();
        this.wCommandCommon = wCommandCommon;
        this.informationBuilder = InformationRegister.builder(wCommandCommon);
        this.requirements = new Requirements();

        this.informationBuilder.with(Game.class, "game", game, "Game instance!");
        this.informationBuilder.withProvider((requestId, requestingType) -> {
            if(serviceManager.isRegistered(requestingType)) {
                return Optional.of(new Information<>(requestId, serviceManager.provideUnchecked(requestingType)));
            }

            return Optional.empty();
        });
    }

    public void process(Report report, User causer, Collection<User> affectedPlayers, Action action, String actionData) {

        InformationRegister informationRegister = informationBuilder.build();

        informationRegister.register(new InfoId("report", Report.class), report);
        informationRegister.register(new InfoId("causer", User.class), causer);
        informationRegister.register(new InfoId("affectedPlayers", AffectedPlayers.class), affectedPlayers);
        informationRegister.register(new InfoId("action", Action.class), action);

        wCommandCommon.processAndInvoke(commonCommandStringParser.parseSingle(actionData), requirements, informationBuilder.build());
    }

    public static class AffectedPlayers {}

}
