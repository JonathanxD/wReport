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
package io.github.jonathanxd.wreport.commands;

import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import io.github.jonathanxd.wreport.reports.IReportManager;
import io.github.jonathanxd.wreport.reports.ReportType;
import io.github.jonathanxd.wreport.reports.reasons.Reason;

/**
 * TODO: Test Command
 */
public class PlayerReportCommand extends wCommandBase implements CommandExecutor {

    private final IReportManager reportManager;

    public PlayerReportCommand(Game game, IReportManager reportManager) {
        super(game);
        this.reportManager = reportManager;
    }

    /*
GenericArguments.player(Text.of("player")),
GenericArguments.choices(Text.of("reason"), reasonRegister.mapOfRegisteredReasonsName()),
GenericArguments.remainingJoinedStrings(Text.of("description"))

     */

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        Optional<Player> player = args.getOne(Text.of("player"));
        Optional<Reason> reason = args.getOne(Text.of("reason"));
        Optional<String> description = args.getOne(Text.of("description"));

        if(player.isPresent() && reason.isPresent()) {

            reportManager.report(ReportType.System.SYSTEM_REPORT_PLAYER, Collections.singleton(player.get()), reason.get(), description.orElse("No description provided!"));

            src.sendMessage(Text.of("Player '"+player.get().getName()+"' reported!"));

            return CommandResult.success();
        }



        return CommandResult.empty();
    }

}
