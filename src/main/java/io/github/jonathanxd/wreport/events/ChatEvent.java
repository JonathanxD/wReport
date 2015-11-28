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

	private Optional<wReport> wReportPlugin = wReport.getwReportPlugin();
	private Optional<ChatHistory> chatHistory = Optional.empty();
	
	@Override
	public void handle(Chat event) throws Exception {
		if(wReportPlugin.isPresent()){
			
			if(!chatHistory.isPresent()){
				chatHistory = wReportPlugin.get().getChatHistory();
			}
			
			if(chatHistory.isPresent()){
				ChatHistory chatHistoryInstance = chatHistory.get();
				Optional<Player> player = event.getCause().first(Player.class);
				if(player.isPresent()){
					chatHistoryInstance.addToHistory(player.get(), Texts.toPlain(event.getMessage()));
				}
				
			}
		
		}
		
	}

}
