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
package io.github.jonathanxd.wreport.registry.registers;

import com.github.jonathanxd.iutils.object.TypeInfo;
import com.github.jonathanxd.wcommands.WCommandCommon;

import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import io.github.jonathanxd.wreport.actions.Action;
import io.github.jonathanxd.wreport.commands.AdminCloseReportCommand;
import io.github.jonathanxd.wreport.commands.AdminReportCommand;
import io.github.jonathanxd.wreport.commands.PlayerReportCommand;
import io.github.jonathanxd.wreport.registry.IReasonRegister;
import io.github.jonathanxd.wreport.registry.DefaultRegister;
import io.github.jonathanxd.wreport.registry.Register;
import io.github.jonathanxd.wreport.reports.IReportManager;
import io.github.jonathanxd.wreport.statics.wReportInfos;
import io.github.jonathanxd.wreport.wReport;

public class CommandRegister implements DefaultRegister, wReportInfos {

    private final IReportManager reportManager;
    private final WCommandCommon wCommandCommon;
    private final Register<TypeInfo<? extends Action>, Action> actionRegister;

    public CommandRegister(IReportManager reportManager, WCommandCommon wCommandCommon, Register<TypeInfo<? extends Action>, Action> actionRegister) {
        this.reportManager = reportManager;
        this.wCommandCommon = wCommandCommon;
        this.actionRegister = actionRegister;
    }

    @Override
    public boolean register(Object plugin, Game game) {
        wReport wReportPlugin = wReport.wReportPlugin();
        IReasonRegister reasonRegister = wReportPlugin.reasonRegister();

        CommandSpec show = CommandSpec.builder()
                .description(Text.builder("Show all or player specific report(s)!").color(TextColors.GREEN).build())
                .permission(NAME.toLowerCase() + ".admin.reports.show")
                .arguments(
                        GenericArguments.optional(GenericArguments.player(Text.of("player")))
                )
                .executor(new AdminReportCommand(game, reportManager))
                .build();

        CommandSpec close = CommandSpec.builder()
                .description(Text.of(TextColors.GREEN, "Close report by id"))
                .permission(NAME.toLowerCase() + ".admin.reports.close")
                .arguments(
                        GenericArguments.longNum(Text.of("id")),
                        GenericArguments.choices(Text.of("action"), actionRegister.applyValueToKey(Action::getName), true),
                        GenericArguments.remainingJoinedStrings(Text.of("data"))
                )
                .executor(new AdminCloseReportCommand(game, reportManager, wCommandCommon, actionRegister))
                .build();

        CommandSpec adminReportCommand = CommandSpec.builder()
                .description(Text.builder("Show all or player specific report(s)!").color(TextColors.GREEN).build())
                .permission(NAME.toLowerCase() + ".admin.reports")
                .child(show, "show")
                .child(close, "close")
                .build();



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
