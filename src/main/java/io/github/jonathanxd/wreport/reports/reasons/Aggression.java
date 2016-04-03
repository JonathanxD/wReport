/*
 *
 * 	wReport - An Sponge plugin to report bad players and start a vote kick.
 *     Copyright (C) 2016 TheRealBuggy/JonathanxD (Jonathan Ribeiro Lopes) <jonathan.scripter@programmer.net>
 *
 * 	GNU GPLv3
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
