package io.github.jonathanxd.wreport;

import java.io.File;
import java.util.Optional;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.config.DefaultConfig;

import com.google.inject.Inject;

import io.github.jonathanxd.wreport.history.ChatHistory;
import io.github.jonathanxd.wreport.registry.Register;
import io.github.jonathanxd.wreport.registry.registers.CommandRegister;
import io.github.jonathanxd.wreport.statics.wReportInfos;

@Plugin(name = "wReport", id = "wReport", version = "0.1-SNAPSHOT")
public class wReport implements wReportInfos {

	private File configFolderFile;
	private Game game;
	private Logger logger;
	private static wReport wReportPlugin;
	private ChatHistory chatHistory;
	
	private Register commandRegister;

	@Listener
	protected void gamePreInitialization(GamePreInitializationEvent gamePreInitEvent) {
		// TODO
		commandRegister = new CommandRegister();
		chatHistory = new ChatHistory();
		wReportPlugin = this;
	}

	@Listener
	protected void gameInitialization(GameInitializationEvent gameInitEvent) {
		// Event handlers and command registration
		logger.info("%s initializing...", NAME);
		
		commandRegister.register(wReportPlugin, game);
		// registerCommands(this, game);
		// registerEvents(this, game);

		logger.info("%s initialized!", NAME);

	}

	protected void gamePostInitialization(GamePostInitializationEvent gamePostInitEvent) {
		// ?
	}

	@Inject
	private void setLogger(Logger logger) {
		this.logger = logger;
	}

	@Inject
	private void setGame(Game game) {
		this.game = game;
	}

	@Inject
	private void setConfigFolder(@DefaultConfig(sharedRoot = false) File file) {
		this.configFolderFile = file;
	}

	public File getConfigFolderFile() {
		return configFolderFile;
	}

	public Game getGame() {
		return game;
	}

	public Logger getLogger() {
		return logger;
	}

	public static Optional<wReport> getwReportPlugin() {
		return wReportPlugin != null ? Optional.of(wReportPlugin) : Optional.empty();
	}

	public Optional<ChatHistory> getChatHistory(){
		return chatHistory != null ? Optional.of(chatHistory) : Optional.empty();
	}
	
}
