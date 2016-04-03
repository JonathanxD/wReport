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
package io.github.jonathanxd.wreport.registry.registers;

import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import io.github.jonathanxd.wreport.commands.AdminReportCommand;
import io.github.jonathanxd.wreport.commands.PlayerReportCommand;
import io.github.jonathanxd.wreport.registry.IReasonRegister;
import io.github.jonathanxd.wreport.registry.DefaultRegister;
import io.github.jonathanxd.wreport.reports.IReportManager;
import io.github.jonathanxd.wreport.statics.wReportInfos;
import io.github.jonathanxd.wreport.wReport;

public class CommandRegister implements DefaultRegister, wReportInfos {

    private final IReportManager reportManager;

    public CommandRegister(IReportManager reportManager) {
        this.reportManager = reportManager;
    }

    @Override
    public boolean register(Object plugin, Game game) {
        wReport wReportPlugin = wReport.wReportPlugin();
        IReasonRegister reasonRegister = wReportPlugin.reasonRegister();

        CommandSpec adminReportCommand = CommandSpec.builder()
                .description(Text.builder("Open a GUI to show all or specific player report(s)!").color(TextColors.GREEN).build())
                .permission(NAME.toLowerCase() + ".admin.reports")
                .arguments(
                        GenericArguments.optional(GenericArguments.player(Text.of("player")))
                )
                .executor(new AdminReportCommand(game, reportManager)).build();
        game.getCommandManager().register(plugin, adminReportCommand, "wradmin", "wreportadmin", "reportadmin", "wradm");

        CommandSpec playerReportCommand = CommandSpec.builder()
                .description(Text.builder("Report an specific player!").color(TextColors.GREEN).build())
                .permission(NAME.toLowerCase() + ".player.report")
                .arguments(
                        GenericArguments.player(Text.of("player")),
                        GenericArguments.choices(Text.of("reason"), reasonRegister.mapOfRegisteredReasonsName(), true),
                        GenericArguments.remainingJoinedStrings(Text.of("description"))
                )
                .executor(new PlayerReportCommand(game, reportManager)).build();
        game.getCommandManager().register(plugin, playerReportCommand, "report", "wreport", "reportplayer", "playerreport");
        return true;
    }

    @Override
    public String getName() {
        return "CommandRegister";
    }
}
