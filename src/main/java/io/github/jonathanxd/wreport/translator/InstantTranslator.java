package io.github.jonathanxd.wreport.translator;

import com.github.jonathanxd.wcommands.ext.reflect.arguments.translators.Translator;

import java.time.Instant;
import java.time.LocalDateTime;
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

        return Instant.from(LocalDateTime.of(years, months, days, hours, minutes, seconds));
    }

    private int from(String str) {
        return str != null && !str.isEmpty() ? Integer.valueOf(str.replaceAll("[^0-9]+", "")) : 0;
    }
}
