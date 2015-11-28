package io.github.jonathanxd.wreport.reports.reasons;

import java.util.Optional;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

import io.github.jonathanxd.wreport.wReport;
import io.github.jonathanxd.wreport.history.ChatHistory;

public class Aggression implements Reason{

	@Override
	public Text reasonMessage() {
		// TODO Auto-generated method stub
		return Texts.builder("Agression.").color(TextColors.RED).build();
	}

	@Override
	public Optional<Object> apply(Optional<Player> subject) {
		
		if(subject.isPresent()){
			
			wReport wReportPlugin = wReport.wReportPlugin();
			
			ChatHistory chatHistory = wReportPlugin.chatHistory();
			
			return Optional.of(chatHistory.getCompleteHistory(subject.get()));
		}
		
		return Reason.super.apply(subject);
	}
}
