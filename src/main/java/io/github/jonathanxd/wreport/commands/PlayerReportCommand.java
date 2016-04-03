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
