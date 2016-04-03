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
package io.github.jonathanxd.wreport.commands.wext.common.arguments;

import com.github.jonathanxd.wcommands.arguments.ArgumentSpec;
import com.github.jonathanxd.wcommands.common.Matchable;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import io.github.jonathanxd.wreport.commands.wext.common.id.CommonIDs;

/**
 * Created by jonathan on 26/03/16.
 *
 * Part of SpongeWCommands
 */
public class PlayerArgumentSpec extends ArgumentSpec<CommonIDs, Player> {


    public PlayerArgumentSpec(boolean isInfinite, Supplier<Matchable<String>> checker, boolean optional) {
        super(CommonIDs.PLAYER, isInfinite, checker, PlayerPredicate.DEFAULT, optional, PlayerConverter.DEFAULT);
    }

    public static final class PlayerPredicate implements Predicate<List<String>> {

        private static final PlayerPredicate DEFAULT = new PlayerPredicate();

        @Override
        public boolean test(List<String> strings) {
            return !strings.isEmpty() && Sponge.getServer().getPlayer(strings.get(0)).isPresent();
        }
    }

    public static final class PlayerConverter implements Function<List<String>, Player> {

        private static final PlayerConverter DEFAULT = new PlayerConverter();

        @Override
        public Player apply(List<String> strings) {

            Supplier<RuntimeException> throwableSupplier = () -> new RuntimeException("Cannot convert '" + strings + "' to Player instance");

            if (strings.isEmpty())
                throw throwableSupplier.get();

            return Sponge.getServer().getPlayer(strings.get(0)).orElseThrow(throwableSupplier);
        }
    }
}
