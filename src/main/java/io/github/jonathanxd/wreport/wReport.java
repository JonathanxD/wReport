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
package io.github.jonathanxd.wreport;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;

import com.github.jonathanxd.iutils.object.Reference;
import com.github.jonathanxd.wcommands.ext.reflect.ReflectionAPI;
import com.github.jonathanxd.wcommands.ext.reflect.processor.ReflectionCommandProcessor;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.user.UserStorageService;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import io.github.jonathanxd.wreport.actions.Action;
import io.github.jonathanxd.wreport.actions.ActionProcessor;
import io.github.jonathanxd.wreport.actions.BanAction;
import io.github.jonathanxd.wreport.config.ReasonSerializer;
import io.github.jonathanxd.wreport.config.ReportTypeSerializer;
import io.github.jonathanxd.wreport.config.UUIDSerializer;
import io.github.jonathanxd.wreport.data.PlayerReportData;
import io.github.jonathanxd.wreport.data.Reports;
import io.github.jonathanxd.wreport.exception.CannotFoundDeclaration;
import io.github.jonathanxd.wreport.history.ChatHistory;
import io.github.jonathanxd.wreport.managers.BaseReasonManager;
import io.github.jonathanxd.wreport.registry.DefaultRegister;
import io.github.jonathanxd.wreport.registry.IReasonRegister;
import io.github.jonathanxd.wreport.registry.Register;
import io.github.jonathanxd.wreport.registry.registers.ActionRegister;
import io.github.jonathanxd.wreport.registry.registers.CommandRegister;
import io.github.jonathanxd.wreport.registry.registers.DefaultReasonsRegister;
import io.github.jonathanxd.wreport.registry.registers.EventRegister;
import io.github.jonathanxd.wreport.registry.registers.ReasonSerializerRegister;
import io.github.jonathanxd.wreport.registry.registers.SerializersRegister;
import io.github.jonathanxd.wreport.registry.registers.ServiceRegister;
import io.github.jonathanxd.wreport.reports.IReportManager;
import io.github.jonathanxd.wreport.reports.ReportType;
import io.github.jonathanxd.wreport.reports.reasons.Aggression;
import io.github.jonathanxd.wreport.reports.reasons.Reason;
import io.github.jonathanxd.wreport.statics.wReportInfos;
import io.github.jonathanxd.wreport.translator.InstantTranslator;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

@Plugin(name = "wReport", id = "wreport", version = "0.1-SNAPSHOT")
public class wReport implements wReportInfos, ConfigurationUpdater {


    private static wReport wReportPlugin;
    private ChatHistory chatHistory;

    private Game game;
    private Logger logger;
    private File configFolderFile;
    private UserStorageService userStorageService;


    private IReasonRegister reasonRegister;
    private IReportManager reportManager;
    private DefaultRegister commandRegister;
    private DefaultRegister defaultReasonsRegister;
    private DefaultRegister eventRegister;
    private DefaultRegister serviceRegister;

    private Register<Class<? extends Reason>, Reason.Serializer<?>> reasonSerializerRegister;
    private Register<Reference<?>, Action> actionRegister;
    private SerializersRegister serializersRegister;

    private final ReflectionCommandProcessor processor = ReflectionAPI.createWCommand();

    private ActionProcessor actionProcessor;

    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private ConfigurationNode rootNode;


    protected static Optional<wReport> getwReportPlugin() {
        return Optional.ofNullable(wReportPlugin);
    }

    /**
     * Will call {@link #fail()} and throw {@link CannotFoundDeclaration} if declaration is not
     * present.
     */
    public static wReport wReportPlugin() {
        if (!getwReportPlugin().isPresent()) {
            fail();
            throw new CannotFoundDeclaration("wReportPlugin");
        }

        return getwReportPlugin().get();
    }

    public static void fail() {

        Logger logger = wReportPlugin.getLogger();

        logger.error("getwReportPlugin: %s ", getwReportPlugin());

        logger.error("getChatHistory: %s ", wReportPlugin.getChatHistory());
        logger.error("getReasonRegister: %s ", wReportPlugin.getReasonRegister());

        logger.error("Sponge API: %s ", wReportPlugin.getGame().getPlatform().getApi().getVersion());
        logger.error("Platform Execution Type: %s ", wReportPlugin.getGame().getPlatform().getExecutionType());
        logger.error("Platform Name: %s ", wReportPlugin.getGame().getPlatform().getImplementation().getName());
        logger.error("Platform Version: %s ", wReportPlugin.getGame().getPlatform().getImplementation().getVersion());
        logger.error("MC Version: %s ", wReportPlugin.getGame().getPlatform().getMinecraftVersion().getName());

    }

    @Listener
    public void gamePreInitialization(GamePreInitializationEvent gamePreInitEvent) {

        processor.addGlobalTranslator(Reference.aEnd(Instant.class), InstantTranslator.class);

        chatHistory = new ChatHistory();
        reasonRegister = new BaseReasonManager();
        reportManager = new Reports(this, actionRegister, actionProcessor);

        commandRegister = new CommandRegister(reportManager);
        defaultReasonsRegister = new DefaultReasonsRegister();
        eventRegister = new EventRegister();
        serviceRegister = new ServiceRegister();
        reasonSerializerRegister = new ReasonSerializerRegister();
        actionRegister = new ActionRegister(processor);
        serializersRegister = new SerializersRegister();
        actionProcessor = new ActionProcessor(this.game, processor);

        wReportPlugin = this;

    }

    @Listener
    public void gameInitialization(GameInitializationEvent gameInitEvent) {


        logger.info(String.format("%s initializing...", NAME));

        defaultReasonsRegister.doRegister(wReportPlugin, game, logger);
        serviceRegister.doRegister(wReportPlugin, game, logger);

        eventRegister.doRegister(wReportPlugin, game, logger);
        commandRegister.doRegister(wReportPlugin, game, logger);

        reasonSerializerRegister.tryRegister(wReportPlugin, game, logger, Aggression.class, new Aggression.Serializer());

        actionRegister.tryRegister(wReportPlugin, game, logger, Reference.aEnd(BanAction.class), new BanAction());

        serializersRegister.tryRegister(wReportPlugin, game, logger, Reference.aEnd(ChatHistory.class), new ChatHistory.Serializer());


        logger.info(String.format("%s initialized!", NAME));

    }

    public Register<Class<? extends Reason>, Reason.Serializer<?>> getReasonSerializerRegister() {
        return reasonSerializerRegister;
    }

    @Listener
    public void gamePostInitialization(GamePostInitializationEvent gamePostInitEvent) {
        // ?
    }

    @Listener
    public void gameStarted(GameStartedServerEvent event) {
        userStorageService = game.getServiceManager().provideUnchecked(UserStorageService.class);
        loadConfiguration();
    }

    protected void loadConfiguration() {
        loader = HoconConfigurationLoader.builder().setPath(getConfigFolderFile().toPath()).build();

        TypeSerializerCollection serializers = TypeSerializers.getDefaultSerializers().newChild();

        serializers.registerType(TypeToken.of(UUID.class), new UUIDSerializer());
        serializers.registerType(TypeToken.of(ReportType.class), new ReportTypeSerializer());
        serializers.registerType(TypeToken.of(Reason.class), new ReasonSerializer(getReasonSerializerRegister()));
        serializers.registerType(TypeToken.of(IReportManager.class), new PlayerReportData(game, logger, serializersRegister, userStorageService, this));

        ConfigurationOptions options = ConfigurationOptions.defaults().setSerializers(serializers);

        try {
            rootNode = loader.load(options);

            rootNode.getValue(TypeToken.of(IReportManager.class), reportManager).exportTo(reportManager);
        } catch (IOException | ObjectMappingException e) {
            throw new RuntimeException(e);
        }
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

    @Inject
    private void setGame(Game game) {
        this.game = game;
    }

    public Logger getLogger() {
        return logger;
    }

    @Inject
    private void setLogger(Logger logger) {
        this.logger = logger;
    }

    protected Optional<ChatHistory> getChatHistory() {
        return Optional.ofNullable(chatHistory);
    }

    protected Optional<IReasonRegister> getReasonRegister() {
        return Optional.ofNullable(reasonRegister);
    }

    /**
     * Will call {@link #fail()} and throw {@link CannotFoundDeclaration} if declaration is not
     * present.
     */
    public ChatHistory chatHistory() {
        if (!getChatHistory().isPresent()) {
            fail();
            throw new CannotFoundDeclaration("chatHistory");
        }

        return getChatHistory().get();
    }

    /**
     * Return current {@link IReportManager}
     *
     * @return Current {@link IReportManager} or null
     */
    public IReportManager getReportManager() {
        return reportManager;
    }

    /**
     * Will call {@link #fail()} and throw {@link CannotFoundDeclaration} if declaration is not
     * present.
     */
    public IReasonRegister reasonRegister() {

        if (!getReasonRegister().isPresent()) {
            fail();
            throw new CannotFoundDeclaration("reasonRegister");
        }

        return getReasonRegister().get();
    }

    public ReflectionCommandProcessor getProcessor() {
        return processor;
    }

    @Override
    public void save() {
        try {
            rootNode.setValue(TypeToken.of(IReportManager.class), reportManager);
            loader.save(rootNode);
        } catch (IOException | ObjectMappingException e) {
            logger.error("Cannot save configuration", e);
        }
    }
}
