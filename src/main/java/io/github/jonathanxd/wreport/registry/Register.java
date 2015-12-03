package io.github.jonathanxd.wreport.registry;

import org.slf4j.Logger;
import org.spongepowered.api.Game;

public interface Register {

	static final String successMessage = "Success registered %s!";
	static final String errorMessage = "Cannot register %s!";

	default void doRegister(Object plugin, Game game, Logger logging){
		if(register(plugin, game)){
			logging.info(successMessage, getName());
		}else{
			logging.error(errorMessage, getName());
		}
	}

	boolean register(Object plugin, Game game);
	String getName();
}
