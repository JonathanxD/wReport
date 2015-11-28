package io.github.jonathanxd.wreport.reports.reasons;

import java.util.Optional;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public interface Reason {
	
	default Optional<Severity> severity(){
		return Optional.empty();
	}
	
	Text reasonMessage();
	
	default Optional<Object> apply(Optional<Player> subject){
		return Optional.empty();
	}
}
