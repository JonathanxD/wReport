package io.github.jonathanxd.wreport.registry.registers;

import org.spongepowered.api.Game;
import org.spongepowered.api.event.command.MessageSinkEvent;

import io.github.jonathanxd.wreport.events.ChatEvent;
import io.github.jonathanxd.wreport.registry.Register;

public class EventRegister implements Register {

	@Override
	public boolean register(Object plugin, Game game) {
		ChatEvent chatListener = new ChatEvent();
		
		game.getEventManager().registerListener(plugin, MessageSinkEvent.Chat.class, chatListener);
		return false;
	}

}
