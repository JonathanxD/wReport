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
package io.github.jonathanxd.wreport.commands.wext;

import com.github.jonathanxd.wcommands.WCommand;
import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.command.CommandSpec;
import com.github.jonathanxd.wcommands.ext.help.printer.CommonPrinter;
import com.github.jonathanxd.wcommands.text.Text;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Created by jonathan on 26/03/16.
 *
 * Part of SpongeWCommands
 */
public final class CommandFinder {

    private final ArgumentSuggestionManager manager;

    public CommandFinder(ArgumentSuggestionManager manager) {
        this.manager = manager;
    }

    private static String simpleToString(CommandSpec root) {
        return root.getName().getPlainString();
    }

    private static StringJoiner simpleToString(StringJoiner joiner, CommandSpec root) {

        joiner.add(simpleToString(root));

        return joiner;
    }

    private static CommandSpec full(String name, CommandSpec root) {
        return root.getSubCommands().getCommandOf(Text.ofIgnoreCase(name)).orElse(null);
    }

    // command axz yhz abf
    // command full(axz) full(yhz) partial(abf)
    // if ( x + 1 <  MAX ) -> full
    // if ( x + 1 >= MAX ) -> partial

    private static List<CommandSpec> partial(String name, CommandSpec root) {
        List<CommandSpec> finds = new ArrayList<>();

        for (CommandSpec spec : root.getSubCommands()) {
            if (testName(name, spec) != null) {
                finds.add(spec);
            }
        }

        return finds;
    }

    private static List<String> partialV2(String name, CommandSpec root) {
        List<String> finds = new ArrayList<>();

        for (CommandSpec spec : root.getSubCommands()) {
            String result;
            if ((result = testName(name, spec)) != null) {
                finds.add(result);
            }
        }

        return finds;
    }

    private static String testName(String name, CommandSpec spec) {

        for (Text txt : spec.allTexts()) {
            if (txt.matches(name)) {
                return txt.getPlainString();
            } else {
                String stringName = txt.getPlainString();
                String name2 = name;


                if (txt.ignoreCase()) {
                    stringName = stringName.toLowerCase();
                    name2 = name2.toLowerCase();
                }

                if (!txt.isRegex() && stringName.startsWith(name2)) {
                    return stringName;
                }
            }
        }
        return null;

    }


    private String argsToString(CommandSpec root) {

        StringBuilder sb = new StringBuilder();

        sb.append('[');
        Iterator<ArgumentSpec<?, ?>> iter = root.getArguments().iterator();

        if (!iter.hasNext())
            return null;

        while (iter.hasNext()) {
            ArgumentSpec<?, ?> argument = iter.next();

            sb.append(CommonPrinter.argument(argument));

            if (iter.hasNext())
                sb.append('|');
        }
        sb.append(']');

        return sb.toString();
    }

    public List<String> suggestionsV2(String[] names, CommandSpec root) throws IOException {

        List<String> suggestions = new ArrayList<>();

        CommandSpec current = root;

        for (int x = 0; x < names.length; ++x) {

            String name = names[x];

            CommandSpec full = full(name, current);

            if (full != null) {
                current = full;
            } else {
                List<String> partial = partialV2(name, current);

                if (partial.isEmpty()) {
                    List<String> next = argumentSuggestions(current, ArrayUtils.subarray(names, x, names.length));

                    if (next.isEmpty()) {
                        return Collections.emptyList();
                    } else {
                        return next;
                    }
                } else {
                    for (String string : partial) {
                        suggestions.add(string);
                    }
                }

            }
        }
        return suggestions;
    }

    public List<String> suggestions(String[] names, CommandSpec root) throws IOException {

        List<String> suggestions = new ArrayList<>();
        StringJoiner sj = new StringJoiner(" ");

        //simpleToString(sj, root);

        CommandSpec current = root;

        for (int x = 0; x < names.length; ++x) {

            String currentName = names[x];

            if ((x + 1) < names.length) {
                CommandSpec full = full(currentName, current);
                if (full == null) {

                    if (current.getArguments().size() > 0) {

                        String fullName = toString(null, current, sj.toString());

                        appendSuggestions(current, fullName, x, names, suggestions);
                    }

                    return Collections.emptyList();
                } else {
                    current = full;
                    simpleToString(sj, current);
                }
            } else {
                List<CommandSpec> partial = partial(currentName, current);

                if (partial.isEmpty()) {

                    String fullName = toString(null, current, sj.toString());

                    suggestions.add(fullName);

                    appendSuggestions(current, fullName, x, names, suggestions);

                    return suggestions;
                } else {

                    for (CommandSpec next : partial) {
                        String onString = sj.toString();

                        suggestions.add(toString(next, onString));

                    }
                }

                // partial
            }
        }

        return suggestions;
    }

    private void appendSuggestions(CommandSpec current, String currentString, int start, String[] names, List<String> suggestions) {

        String fullName = toString(null, current, currentString);

        String[] end = ArrayUtils.subarray(names, start, names.length);
        suggestions.addAll(argumentSuggestions(current, end).stream().map(s -> (fullName != null && !fullName.isEmpty() ? fullName + " " : "") + s).collect(Collectors.toList()));
    }

    private List<String> argumentSuggestions(CommandSpec current, String[] args) {
        List<String> argumentSuggestions = new ArrayList<>();
        if (manager == null)
            return argumentSuggestions;
        for (ArgumentSpec<?, ?> argumentSpec : current.getArguments()) {
            argumentSuggestions.addAll(manager.getSuggestionsFor(argumentSpec, args));
        }

        return argumentSuggestions;
    }

    private String toString(CommandSpec spec, String current) {
        return toString(spec, null, current);
    }

    private String toString(CommandSpec spec, CommandSpec argSpec, String current) {
        String stringArg = argSpec != null ? argsToString(argSpec) : (spec != null ? argsToString(spec) : null);

        boolean appendWhiteSpace = (current != null && !current.isEmpty());

        return current + (spec != null ? (appendWhiteSpace ? " " : "") + spec.getName().getPlainString() + (stringArg != null ? (appendWhiteSpace ? " " : "") + stringArg : "") : "");
    }

    public List<String> getFor(WCommand<?> wCommand, String commandName, String[] args) throws IOException {

        List<String> suggestions = new ArrayList<>();

        for (CommandSpec commandSpec : wCommand.getCommandList()) {

            String result;

            if ((result = testName(commandName, commandSpec)) != null) {

                List<String> suggestionsOfSub = suggestionsV2(args, commandSpec);
                if (!suggestionsOfSub.isEmpty()) {
                    suggestions.addAll(suggestionsOfSub);
                    break;
                } else if (args.length != 0) {

                    List<String> argsList = argumentSuggestions(commandSpec, args);

                    if (!argsList.isEmpty()) {
                        suggestions.addAll(argsList);
                    }
                } else {
                    suggestions.add(result);
                }


            }
        }

        return suggestions;
    }

    private String full(String first, String... other) {
        StringJoiner sj = new StringJoiner(" ");
        sj.add(first);

        for (String o : other) {
            sj.add(o);
        }

        return sj.toString();
    }

    /*
    public static void main(String[] args) {
        CommandFinder commandFinder = new CommandFinder(null);

        ReflectionCommandProcessor processor = new ReflectionCommandProcessor();

        ReflectionRegister<CommandFinder> register = processor.getRegister(RegistrationTicket.empty(commandFinder));

        register.addCommands(new Simple());

        try {
            System.out.println(commandFinder.getFor(processor, "cmd", new String[]{"cmdxx"}));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static class Simple {
        @Command
        public void cmd() {

        }

        @SubCommand(value = "cmd", commandSpec = @Command(name = "cmd2"))
        public void cmd2() {

        }


        @SubCommand(value = "cmd", commandSpec = @Command(name = "cmd2V"))
        public void cmd2V() {

        }

        @SubCommand(value = {"cmd", "cmd2"}, commandSpec = @Command(name = "cmd3"))
        public void abl() {

        }

        @Command
        public void cmdX() {

        }

    }
*/
}
