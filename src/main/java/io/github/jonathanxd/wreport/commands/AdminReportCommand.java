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
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import io.github.jonathanxd.wreport.actions.ActionData;
import io.github.jonathanxd.wreport.data.BaseData;
import io.github.jonathanxd.wreport.data.Data;
import io.github.jonathanxd.wreport.reports.CloseReportData;
import io.github.jonathanxd.wreport.reports.IReportManager;
import io.github.jonathanxd.wreport.reports.Report;
import io.github.jonathanxd.wreport.reports.ReportType;
import io.github.jonathanxd.wreport.reports.reasons.Reason;
import io.github.jonathanxd.wreport.reports.reasons.Severity;

public class AdminReportCommand extends wCommandBase implements CommandExecutor {

    private final IReportManager manager;

    public AdminReportCommand(Game game, IReportManager manager) {
        super(game);
        this.manager = manager;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        boolean isPlayer = src instanceof Player;
        boolean argumentPresent = args.hasAny("player");

        Player player = args.<Player>getOne("player").orElse(null);

        if (!isPlayer) {
            /*src.sendMessage(Text.builder("Error, only players can use this command!").style(TextStyles.BOLD)
                    .color(TextColors.RED).build());*/

            showReported(src, player);

            return CommandResult.empty();
        } else {
            if (argumentPresent) {

                showReported(src, player);

                //player.openInventory();
                // TODO Admins see all player reports in a GUI
                return CommandResult.success();
            }
        }

        return CommandResult.empty();

    }


    private void showReported(CommandSource src, Player player) {

        Collection<Report> reports = player != null ? manager.getPlayerReports(player) : manager.getAllReports();

        src.sendMessage(Text.of(TextColors.RED, "==========", TextColors.GREEN, "Reports", TextColors.RED, "=========="));

        for (Report report : reports) {

            src.sendMessage(Text.of(TextColors.AQUA, "--------------------"));

            Reason reportReason = report.getReportReason();

            Optional<User> reportRequirer = report.getReportApplicant();

            String reporter = reportRequirer.isPresent() ? reportRequirer.get().getName() : "None";

            src.sendMessage(Text.of(TextColors.GREEN, "Id: ", TextColors.AQUA, report.getId()));

            src.sendMessage(Text.of(TextColors.GREEN, "Reason:", TextColors.AQUA, " ", reportReason.reasonMessage(),
                    TextColors.GREEN, " Severity:", TextColors.RED, " ", reportReason.severity().orElse(Severity.MEDIUM),
                    TextColors.GREEN, " Reporter:", TextColors.RED, " ", reporter));

            src.sendMessage(Text.of(TextColors.GREEN, "Description:", TextColors.AQUA, " ", report.getDescription()));

            ReportType reportType = report.getReportType();
            src.sendMessage(Text.of(TextColors.GREEN, "ReportType:", TextColors.AQUA, " ", reportType.getName()));


            Optional<Collection<User>> reportedPlayers = report.getReportedPlayers();

            if (reportedPlayers.isPresent()) {
                Text.Builder textBuilder = Text.builder();

                Iterator<User> playerIterator = reportedPlayers.get().iterator();

                while (playerIterator.hasNext()) {
                    User reported = playerIterator.next();
                    textBuilder.append(Text.of(TextColors.AQUA, reported.getName()));

                    if (playerIterator.hasNext())
                        textBuilder.append(Text.of(TextColors.GREEN, ", "));
                }

                src.sendMessage(Text.of(TextColors.GREEN, "Reported Players: ", textBuilder.build()));

            }

            Optional<BaseData<User, Data<?>>> singleReportData = manager.getSingleReportData(report);

            if (singleReportData.isPresent()) {

                BaseData<User, Data<?>> baseData = singleReportData.get();

                for (Map.Entry<User, Data<?>> entry : baseData.getDataStoreMap().entrySet()) {

                    src.sendMessage(Text.builder("-> ").color(TextColors.AQUA).append(Text.of(TextColors.GREEN, entry.getKey().getName())).build());

                    if (entry.getValue() != null) {
                        Data<?> value = entry.getValue();
                        if (value.getData() instanceof Collection) {


                            @SuppressWarnings("unchecked") Collection<String> completeHistory = (Collection<String>) value.getData();


                            src.sendMessage(Text.of(TextColors.GREEN, "   ChatHistory "));


                            src.sendMessage(Text.of(TextColors.GRAY, "   ---------------------"));

                            completeHistory.forEach(e -> src.sendMessage(Text.of("   ", TextColors.RED, e)));

                            src.sendMessage(Text.of(TextColors.GRAY, "   ---------------------"));


                        } else {
                            src.sendMessage(Text.of(value.toString()));
                        }
                    }


                }


            }

            if(report.getCloseReportData().isPresent()) {
                CloseReportData closeReportData = report.getCloseReportData().get();

                src.sendMessage(Text.of(TextColors.RED, "  Closed "));

                ActionData actionData = closeReportData.getAction();

                String judge = closeReportData.getJudge().isPresent() ? closeReportData.getJudge().get().getName() : "Console";

                src.sendMessage(Text.of(TextColors.GREEN, "   Judge:", " ", TextColors.AQUA, judge));

                src.sendMessage(Text.of(TextColors.GREEN, "   Description:", " ", TextColors.AQUA, closeReportData.getDescription()));


                String causer = actionData.getCauser().isPresent() ? actionData.getCauser().get().getName() : "Console";

                src.sendMessage(Text.of(TextColors.GREEN, "   Action:", " ", TextColors.AQUA, actionData.getReference().toString()));

                src.sendMessage(Text.of(TextColors.GREEN, "   Arguments:", " ", TextColors.AQUA, actionData.getActionArguments().toString()));

                src.sendMessage(Text.of(TextColors.GREEN, "   Causer:", " ", TextColors.AQUA, causer));

                src.sendMessage(Text.of(TextColors.GREEN, "   Affected Users:", " ", TextColors.AQUA, actionData.getAffectedUsers().stream().map(User::getName).collect(Collectors.toList()).toString()));


            }

            src.sendMessage(Text.of(TextColors.AQUA, "--------------------"));

        }

        src.sendMessage(Text.of(TextColors.RED, "==========", TextColors.GREEN, "Reports", TextColors.RED, "=========="));
    }

}
