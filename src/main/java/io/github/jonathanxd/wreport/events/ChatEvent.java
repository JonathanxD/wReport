package io.github.jonathanxd.wreport.events;

import java.util.Optional;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.command.MessageSinkEvent;
import org.spongepowered.api.event.command.MessageSinkEvent.Chat;
import org.spongepowered.api.text.Texts;

import io.github.jonathanxd.wreport.wReport;
import io.github.jonathanxd.wreport.history.ChatHistory;

public class ChatEvent implements EventListener<MessageSinkEvent.Chat>{

	private wReport wReportPlugin = wReport.wReportPlugin();
	private ChatHistory chatHistory = wReportPlugin.chatHistory();
	
	@Override
	public void handle(Chat event) throws Exception {
		
		Optional<Player> player = event.getCause().first(Player.class);
		if(player.isPresent()){
			chatHistory.addToHistory(player.get(), Texts.toPlain(event.getMessage()));
		}
		
	}

}
