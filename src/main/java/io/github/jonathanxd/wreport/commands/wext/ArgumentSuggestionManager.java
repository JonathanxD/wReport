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

/**
 * Created by jonathan on 26/03/16.
 *
 * Part of SpongeWCommands
 */

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;

import org.spongepowered.api.Game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.jonathanxd.wreport.commands.wext.exceptions.AlreadyRegisteredException;
import io.github.jonathanxd.wreport.commands.wext.suggestions.AppendType;
import io.github.jonathanxd.wreport.commands.wext.suggestions.Mixer;

/**
 * If you are using Reflection, the argument suggestion will automatically detect the type, if not,
 * you need to use common arguments {@link io.github.jonathanxd.wreport.commands.wext.common.arguments},
 * common ids {@link io.github.jonathanxd.wreport.commands.wext.common.id.CommonIDs}, register a
 * {@link ArgumentSuggestion} or add a Reference data like that (Player example):
 * <pre>
 *
 * {@code
 *
 * ArgumentSpec... spec = ...;
 *
 * spec.getReferenceData().registerData(Reference.a(Reference.class).hold(
 * Reference.aEnd(Player.class);
 * ).build());
 *
 * }
 *
 * </pre>
 */
public class ArgumentSuggestionManager {

    private final Game game;
    private final GameModule gameModule;
    private final Injector injector;
    private final Set<ArgumentSuggestion> argumentSuggestions = new HashSet<>();

    public ArgumentSuggestionManager(Game game) {
        this.game = game;
        this.gameModule = new GameModule(this.game);
        this.injector = Guice.createInjector(gameModule);
    }

    /**
     * Register argument suggestion class. Dependency injection is available for {@link Game} only.
     *
     * @param suggestionClass Argument suggestion class
     * @return Instance.
     * @throws AlreadyRegisteredException Throw {@link AlreadyRegisteredException} if the Suggestion
     *                                    instance is already registered.
     */
    public ArgumentSuggestion register(Class<? extends ArgumentSuggestion> suggestionClass) throws AlreadyRegisteredException {
        ArgumentSuggestion instance = injector.getInstance(suggestionClass);

        if (!register(instance)) {
            throw new AlreadyRegisteredException(String.valueOf(suggestionClass.getCanonicalName()), ArgumentSuggestion.class);
        }

        return instance;
    }

    /**
     * Register argument suggestion instance.
     *
     * @param argumentSuggestion Argument suggestion instance
     * @return {@link Set#add(Object)}
     */
    public boolean register(ArgumentSuggestion argumentSuggestion) {
        return this.argumentSuggestions.add(argumentSuggestion);
    }

    /**
     * Unregister argument suggestion
     *
     * @param argumentSuggestion Argument suggestion instance.
     * @return {@link Set#remove(Object)}
     */
    public boolean unregister(ArgumentSuggestion argumentSuggestion) {
        return this.argumentSuggestions.remove(argumentSuggestion);
    }

    /**
     * Return Game
     *
     * @return Game
     */
    public Game getGame() {
        return game;
    }

    public List<String> getSuggestionsFor(ArgumentSpec<?, ?> spec, String[] args) {
        List<String> original = new ArrayList<>();
        List<String> yourSuggestions = new ArrayList<>();
        List<String> lastSuggestions = new ArrayList<>();

        for (ArgumentSuggestion suggestion : argumentSuggestions) {
            yourSuggestions.clear();
            lastSuggestions.clear();
            lastSuggestions.addAll(original);

            AppendType appendType = suggestion.getSuggestions(spec, args, lastSuggestions, yourSuggestions);

            AppendType.apply(appendType, original, yourSuggestions, new Mixer.StringListMixer());
        }

        return original;
    }

    private static class GameModule extends AbstractModule {

        private final Game game;

        private GameModule(Game game) {
            this.game = game;
        }

        @Override
        protected void configure() {
            bind(Game.class).toInstance(game);
        }
    }
}
