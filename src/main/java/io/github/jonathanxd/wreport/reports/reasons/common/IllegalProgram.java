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
package io.github.jonathanxd.wreport.reports.reasons.common;

import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collection;
import java.util.Optional;

import io.github.jonathanxd.wreport.data.Data;
import io.github.jonathanxd.wreport.reports.HistoryFetcher;
import io.github.jonathanxd.wreport.reports.reasons.Reason;
import io.github.jonathanxd.wreport.reports.reasons.Severity;

/**
 * Created by jonathan on 09/04/16.
 */
public class IllegalProgram implements Reason {
    @Override
    public Optional<Severity> severity() {
        return Optional.of(Severity.HIGH);
    }

    @Override
    public Text reasonMessage() {
        return Text.of(TextColors.RED, "Illegal Program");
    }

    @Override
    public Optional<Data<?>> apply(User subject) {

        if (subject != null) {
            Optional<Collection<String>> history = HistoryFetcher.getHistory(subject);

            if (history.isPresent()) {
                Collection<String> strings = history.get();

            }

        }

        return Reason.super.apply(subject);
    }

}
