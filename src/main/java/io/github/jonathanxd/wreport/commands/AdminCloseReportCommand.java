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

import com.github.jonathanxd.iutils.object.Node;
import com.github.jonathanxd.iutils.object.Reference;
import com.github.jonathanxd.wcommands.WCommandCommon;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.ext.help.HelperAPI;
import com.github.jonathanxd.wcommands.ext.help.printer.CommonPrinter;
import com.github.jonathanxd.wcommands.infos.InformationRegister;

import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Optional;

import io.github.jonathanxd.wreport.actions.Action;
import io.github.jonathanxd.wreport.registry.Register;
import io.github.jonathanxd.wreport.reports.IReportManager;
import io.github.jonathanxd.wreport.reports.Report;

public class AdminCloseReportCommand extends wCommandBase implements CommandExecutor {

    private final IReportManager manager;
    private final WCommandCommon commandCommon;
    private final Register<Reference<? extends Action>, Action> actionRegister;

    public AdminCloseReportCommand(Game game, IReportManager manager, WCommandCommon commandCommon, Register<Reference<? extends Action>, Action> actionRegister) {
        super(game);
        this.manager = manager;
        this.commandCommon = commandCommon;
        this.actionRegister = actionRegister;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        User user = src instanceof User ? (User) src : null;

        long id = args.<Long>getOne(Text.of("id")).orElse(0L);
        Action action = args.<Action>getOne(Text.of("action")).orElseThrow(() -> new RuntimeException("Action required"));
        String data = args.<String>getOne(Text.of("data")).orElse(null);

        Node<String, String> nd = desc(data);

        String description = nd.getKey();
        String actionData = nd.getValue();



        Optional<Report> reportOptional = manager.get(id);

        if(description == null) {
            throw new CommandException(Text.of(TextColors.RED, "Description missing! Usage: /wradmin close [reportid] [action] \"Description\" \"Action arguments\""));
        }

        if(!reportOptional.isPresent()) {
            src.sendMessage(Text.of(TextColors.RED, "Cannot find report id '"+id+"'"));
        } else {

            action = actionRegister.getUnchecked(action.getReference());

            Optional<CommandSpec> command = commandCommon.getCommand(action.getName().toLowerCase());

            if(!command.isPresent())
                throw new CommandException(Text.of(TextColors.GREEN, "Cannot find action '", TextColors.RED, action.getName(), TextColors.GREEN, "'"));


            boolean end = false;
            Report report = reportOptional.get();
            try{
                end = manager.endReport(report, user, description, action, actionData, (aUser) -> true, src);
            }catch (Exception e) {
                end = false;

                throw new RuntimeException(e);
            }
            if(!end) {
                HelperAPI.help(command.get(), new InformationRegister(), new CPrint(src, false));
            }else {
                src.sendMessage(Text.of(TextColors.GREEN, "Report closed!"));
            }


            return CommandResult.success();
        }


        return CommandResult.empty();

    }

    public static class CPrint extends CommonPrinter {

        public CPrint(MessageReceiver receiver, boolean printLabels) {
            super(new PrintStream(new MessageReceiverOutputStream(receiver, false)),
                    new PrintStream(new MessageReceiverOutputStream(receiver, true)),
                    printLabels);
        }


    }

    public static class MessageReceiverOutputStream extends OutputStream {

        private final MessageReceiver messageReceiver;
        private final TextColor textColor;
        StringBuffer sb = new StringBuffer();

        public MessageReceiverOutputStream(MessageReceiver messageReceiver, boolean isError) {
            this.messageReceiver = messageReceiver;
            this.textColor = isError ? TextColors.RED : TextColors.GREEN;
        }

        @Override
        public void write(int b) throws IOException {
            if(b == '\n') {
                messageReceiver.sendMessage(Text.of(textColor, sb.toString()));
                sb.setLength(0);
            }else {
                sb.append((char) b);
            }

        }
    }


    private static Node<String, String> desc(String data) throws CommandException {
        if(data == null || !data.startsWith("\"")) {
            throw new CommandException(Text.of(TextColors.RED, "Description missing! Usage: /wradmin close [reportid] [action] \"Description\" \"Action arguments\""));
        } else {
            StringBuilder desc = new StringBuilder();
            char[] cr = data.toCharArray();
            int end = 0;

            for(int x = 0; x < cr.length; ++x) {
                char c = cr[x];
                if(x != 0 && c == '"') {
                    end = x;
                    break;
                }
                if(x == 0)
                    continue;

                desc.append(c);

            }

            return new Node<>(desc.toString(), data.substring(end+1).trim());
        }
    }
}
