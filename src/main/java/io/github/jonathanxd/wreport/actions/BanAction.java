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
package io.github.jonathanxd.wreport.actions;

import com.github.jonathanxd.iutils.object.TypeInfo;
import com.github.jonathanxd.wcommands.ext.reflect.arguments.Argument;
import com.github.jonathanxd.wcommands.ext.reflect.commands.Command;
import com.github.jonathanxd.wcommands.ext.reflect.infos.Info;
import com.github.jonathanxd.wcommands.infos.Information;
import com.github.jonathanxd.wcommands.result.Result;

import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.ban.BanService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.ban.Ban;

import java.time.Instant;
import java.util.Collection;

import io.github.jonathanxd.wreport.reports.Report;

/**
 * Created by jonathan on 03/04/16.
 */
public class BanAction implements Action {


    @Command(desc = "Ban player, Ban time format: [XyXmoXdXhXmXs]. y = years, mo = months, d = days, h = hours, m = minutes, s = seconds")
    public Result<State> ban(@Argument(id = "Ban Time") Instant time,
                             @Info Information<MessageReceiver> receiverInformation,
                             @Info Information<User> causer,
                             @Info Information<Report> reportInformation,
                             @Info(tags = "description") Information<String> description,
                             @Info(tags = "affectedPlayers") Information<Collection<User>> affectedPlayers,
                             @Info Information<BanService> banServiceInfo) {

        if (!banServiceInfo.isPresent()) {
            throw new RuntimeException("Cannot get ban service");
        }

        BanService banService = banServiceInfo.get();


        Report report = reportInformation.get();
        MessageReceiver messageReceiver = receiverInformation.get();

        affectedPlayers.get().forEach(user -> {
            banService.addBan(Ban.builder().profile(user.getProfile()).reason(Text.of(TextColors.RED, description)).startDate(Instant.now()).expirationDate(time).build());
            messageReceiver.sendMessage(Text.of(TextColors.RED, "Banned '", user.getName(), "'"));
        });

        return new Result<>(State.OK, State.OK);

    }

    @Override
    public String getName() {
        return "Ban";
    }

    @Override
    public TypeInfo<? extends Action> getReference() {
        return TypeInfo.aEnd(BanAction.class);
    }
}
