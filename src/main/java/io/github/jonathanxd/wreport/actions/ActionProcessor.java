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
package io.github.jonathanxd.wreport.actions;

import com.github.jonathanxd.iutils.object.Reference;
import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.commandstring.CommonCommandStringParser;
import com.github.jonathanxd.wcommands.infos.InfoId;
import com.github.jonathanxd.wcommands.infos.Information;
import com.github.jonathanxd.wcommands.infos.InformationRegister;
import com.github.jonathanxd.wcommands.infos.requirements.Requirements;
import com.github.jonathanxd.wcommands.result.Results;

import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.ServiceManager;
import org.spongepowered.api.text.channel.MessageReceiver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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

        this.informationBuilder.with(Game.class, "game", game, "Game instance!", Reference.aEnd(Game.class));
        this.informationBuilder.withProvider((requestId, requestingType) -> {
            if(serviceManager.isRegistered(requestingType.getAClass())) {
                return Optional.of(new Information<>(requestId, serviceManager.provideUnchecked(requestingType.getAClass()), requestingType));
            }

            return Optional.empty();
        });
    }

    public Optional<ActionData> process(Report report, MessageReceiver receiver, User causer, Collection<User> affectedPlayers, Action action, String actionData) {

        InformationRegister informationRegister = informationBuilder.build();


        informationRegister.register(new InfoId("messager", MessageReceiver.class), receiver, Reference.aEnd(MessageReceiver.class));
        informationRegister.register(new InfoId("report", Report.class), report, Reference.aEnd(Report.class));

        if(causer != null) {
            informationRegister.register(new InfoId("causer", User.class), causer, Reference.aEnd(User.class));
        }

        if(affectedPlayers != null) {
            informationRegister.register(new InfoId("affectedPlayers", AffectedPlayers.class), affectedPlayers, Reference.a(Collection.class).of(User.class).build());
        }

        informationRegister.register(new InfoId("action", Action.class), action, Reference.aEnd(Action.class));

        List<String> actionDataArgs;

        List<String> args = new ArrayList<>();

        args.add(action.getName().toLowerCase());

        if(actionData != null) {
            actionDataArgs = commonCommandStringParser.parseSingle(actionData);
            args.addAll(actionDataArgs);
        } else {
            actionDataArgs = Collections.emptyList();
        }

        Results results = wCommandCommon.processAndInvoke(args, requirements, informationRegister);

        if(results.find(Action.State.OK).isPresent()) {
            return Optional.of(new ActionData(action.getReference(), causer, affectedPlayers, actionDataArgs, report));
        }

        return Optional.empty();
    }

    public static class AffectedPlayers {}

}
