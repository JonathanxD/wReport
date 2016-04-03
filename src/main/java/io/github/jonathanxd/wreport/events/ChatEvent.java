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
package io.github.jonathanxd.wreport.events;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.message.MessageChannelEvent.Chat;

import java.util.Optional;

import io.github.jonathanxd.wreport.history.ChatHistory;
import io.github.jonathanxd.wreport.wReport;

public class ChatEvent implements EventListener<Chat> {

    private wReport wReportPlugin = wReport.wReportPlugin();
    private ChatHistory chatHistory = wReportPlugin.chatHistory();

    @Override
    public void handle(Chat event) throws Exception {

        Optional<Player> player = event.getCause().first(Player.class);
        if (player.isPresent()) {
            chatHistory.addToHistory(player.get(), event.getMessage().toPlain());
        }

    }

}
