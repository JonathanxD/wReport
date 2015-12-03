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

import io.github.jonathanxd.wreport.exception.CannotFoundDeclaration;
import io.github.jonathanxd.wreport.history.ChatHistory;
import io.github.jonathanxd.wreport.managers.BaseReasonManager;
import io.github.jonathanxd.wreport.registry.IReasonRegister;
import io.github.jonathanxd.wreport.registry.Register;
import io.github.jonathanxd.wreport.registry.registers.CommandRegister;
import io.github.jonathanxd.wreport.registry.registers.DefaultReasonsRegister;
import io.github.jonathanxd.wreport.registry.registers.EventRegister;
import io.github.jonathanxd.wreport.registry.registers.ServiceRegister;
import io.github.jonathanxd.wreport.statics.wReportInfos;

@Plugin(name = "wReport", id = "wReport", version = "0.1-SNAPSHOT")
public class wReport implements wReportInfos {

	private File configFolderFile;
	private ChatHistory chatHistory;
	private Game game;
	private Logger logger;
	private IReasonRegister reasonRegister;
	private static wReport wReportPlugin;

	private Register commandRegister;
	private Register defaultReasonsRegister;
	private Register eventRegister;
	private Register serviceRegister;

	@Listener
	protected void gamePreInitialization(GamePreInitializationEvent gamePreInitEvent) {
		
		chatHistory = new ChatHistory();
		reasonRegister = new BaseReasonManager();
		
		commandRegister = new CommandRegister();
		defaultReasonsRegister = new DefaultReasonsRegister();
		eventRegister = new EventRegister();		
		serviceRegister = new ServiceRegister();
		
		wReportPlugin = this;
	}

	@Listener
	protected void gameInitialization(GameInitializationEvent gameInitEvent) {
		
		logger.info("%s initializing...", NAME);
		

		defaultReasonsRegister.doRegister(wReportPlugin, game, logger);
		serviceRegister.doRegister(wReportPlugin, game, logger);

		eventRegister.doRegister(wReportPlugin, game, logger);
		commandRegister.doRegister(wReportPlugin, game, logger);

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

	protected static Optional<wReport> getwReportPlugin() {
		return Optional.ofNullable(wReportPlugin);
	}

	protected Optional<ChatHistory> getChatHistory(){
		return Optional.ofNullable(chatHistory);
	}
	
	protected Optional<IReasonRegister> getReasonRegister() {
		return Optional.ofNullable(reasonRegister);
	}

	/**
	 * Will call {@link #fail()} and throw {@link CannotFoundDeclaration} if declaration is not present.
	 * @return
	 * @throws CannotFoundDeclaration
	 */
	public static wReport wReportPlugin() {
		if(!getwReportPlugin().isPresent()){
			fail();
			throw new CannotFoundDeclaration("wReportPlugin");
		}
		
		return getwReportPlugin().get();
	}

	/**
	 * Will call {@link #fail()} and throw {@link CannotFoundDeclaration} if declaration is not present.
	 * @return
	 * @throws CannotFoundDeclaration
	 */
	public ChatHistory chatHistory(){
		if(!getChatHistory().isPresent()){
			fail();
			throw new CannotFoundDeclaration("chatHistory");
		}
		
		return getChatHistory().get();
	}
	
	/**
	 * Will call {@link #fail()} and throw {@link CannotFoundDeclaration} if declaration is not present.
	 * @return
	 * @throws CannotFoundDeclaration
	 */
	public IReasonRegister reasonRegister() {
		
		if(!getReasonRegister().isPresent()){
			fail();
			throw new CannotFoundDeclaration("reasonRegister");
		}
		
		return getReasonRegister().get();
	}
	
	public static void fail(){
		
		Logger logger = wReportPlugin.getLogger();
			
		logger.error("getwReportPlugin: %s ", getwReportPlugin());
		
		logger.error("getChatHistory: %s ", wReportPlugin.getChatHistory());
		logger.error("getReasonRegister: %s ", wReportPlugin.getReasonRegister());

		logger.error("Sponge API: %s ", wReportPlugin.getGame().getPlatform().getApiVersion());
		logger.error("Platform Execution Type: %s ", wReportPlugin.getGame().getPlatform().getExecutionType());
		logger.error("Platform Name: %s ", wReportPlugin.getGame().getPlatform().getName());
		logger.error("Platform Version: %s ", wReportPlugin.getGame().getPlatform().getVersion());
		logger.error("MC Version: %s ", wReportPlugin.getGame().getPlatform().getMinecraftVersion().getName());
		
	}
	
}
