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
package io.github.jonathanxd.wreport.translator;

import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.Translator;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jonathan on 03/04/16.
 */
public class InstantTranslator implements Translator<Instant> {

    static final Pattern PATTERN = Pattern.compile("(?<years>[0-9]+y)?(?<months>[0-9]+mo)?(?<days>[0-9]+d)?(?<hours>[0-9]+h)?(?<minutes>[0-9]+m)?(?<seconds>[0-9]s)?");

    @Override
    public boolean isAcceptable(List<String> text) {
        return !text.isEmpty() && translate(text) != null;
    }

    @Override
    public Instant translate(List<String> text) {
        if(text.isEmpty())
            return null;

        String at0 = text.get(0);

        Matcher matcher = PATTERN.matcher(at0);

        int years = 0;
        int months = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        if(matcher.matches()) {
            String ye = matcher.group("years");
            years = from(ye);

            String mo = matcher.group("months");
            months = from(mo);

            String da = matcher.group("days");
            days = from(da);

            String ho = matcher.group("hours");
            hours = from(ho);

            String mi = matcher.group("minutes");
            minutes = from(mi);

            String se = matcher.group("seconds");
            seconds = from(se);
        }

        Instant now = Instant.now();
        if(years > 0) {
            now = now.plus(years, ChronoUnit.YEARS);
        }
        if(months > 0) {
            now = now.plus(months, ChronoUnit.MONTHS);
        }
        if(days > 0) {
            now = now.plus(days, ChronoUnit.DAYS);
        }

        if(hours > 0) {
            now = now.plus(hours, ChronoUnit.HOURS);
        }

        if(minutes > 0) {
            now = now.plus(minutes, ChronoUnit.MINUTES);
        }

        if(seconds > 0) {
            now = now.plus(seconds, ChronoUnit.SECONDS);
        }

        return now;
    }

    private int from(String str) {
        return str != null && !str.isEmpty() ? Integer.valueOf(str.replaceAll("[^0-9]+", "")) : 0;
    }
}
