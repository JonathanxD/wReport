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
package io.github.jonathanxd.wreport.reports.reasons;

import com.google.common.reflect.TypeToken;

import com.github.jonathanxd.iutils.object.Reference;

import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collection;
import java.util.Optional;

import io.github.jonathanxd.wreport.data.Data;
import io.github.jonathanxd.wreport.history.ChatHistory;
import io.github.jonathanxd.wreport.wReport;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class Aggression implements Reason {

    @Override
    public Text reasonMessage() {
        // TODO Auto-generated method stub
        return Text.builder("Agression.").color(TextColors.RED).build();
    }

    @Override
    public Optional<Data<?>> apply(User subject) {

        if (subject != null) {

            wReport wReportPlugin = wReport.wReportPlugin();

            ChatHistory chatHistory = wReportPlugin.chatHistory();

            Optional<Collection<String>> completeHistory = chatHistory.getCompleteHistory(subject);

            if(completeHistory.isPresent()) {
                return Optional.of(new Data<>(Reference.aEnd(ChatHistory.class), completeHistory.get()));
            }

            return Optional.empty();
        }

        return Reason.super.apply(subject);
    }

    public static class Serializer implements Reason.Serializer<Aggression> {

        final static String REASON_MESSAGE_NODE = "reasonMessage";

        @Override
        public void serialize(Aggression reason, ConfigurationNode node) {
            try {
                node.getNode(REASON_MESSAGE_NODE).setValue(TypeToken.of(Text.class), reason.reasonMessage());
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Aggression deserialize(ConfigurationNode node) {
            return new Aggression();
        }
    }
}
