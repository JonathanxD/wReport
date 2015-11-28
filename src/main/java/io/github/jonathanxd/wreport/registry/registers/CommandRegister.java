package io.github.jonathanxd.wreport.registry.registers;

import org.spongepowered.api.Game;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandSpec;

import io.github.jonathanxd.wreport.wReport;
import io.github.jonathanxd.wreport.commands.AdminReportCommand;
import io.github.jonathanxd.wreport.commands.PlayerReportCommand;
import io.github.jonathanxd.wreport.registry.IReasonRegister;
import io.github.jonathanxd.wreport.registry.Register;
import io.github.jonathanxd.wreport.statics.wReportInfos;

public class CommandRegister implements Register, wReportInfos {

	@Override
	public boolean register(Object plugin, Game game) {
		wReport wReportPlugin = wReport.wReportPlugin();
		IReasonRegister reasonRegister = wReportPlugin.reasonRegister();
		
		CommandSpec adminReportCommand = CommandSpec.builder()
			      .description(Texts.builder("Open a GUI to show all or specific player report(s)!").color(TextColors.GREEN).build())
			      .permission(NAME.toLowerCase()+".admin.reports")
			      .arguments(
			    		  GenericArguments.optional(GenericArguments.player(Texts.of("player"), game))
			      )
			      .executor(new AdminReportCommand(game)).build();
		game.getCommandDispatcher().register(plugin, adminReportCommand, "wradmin", "wreportadmin", "reportadmin", "wradm");
		
		CommandSpec playerReportCommand = CommandSpec.builder()
			      .description(Texts.builder("Report an specific player!").color(TextColors.GREEN).build())
			      .permission(NAME.toLowerCase()+".player.report")
			      .arguments(
			    		  GenericArguments.player(Texts.of("player"), game),
			    		  GenericArguments.choices(Texts.of("reason"), reasonRegister.mapOfRegisteredReasonsName()),
			    		  GenericArguments.remainingJoinedStrings(Texts.of("description"))
			      )
			      .executor(new PlayerReportCommand(game)).build();
		game.getCommandDispatcher().register(plugin, playerReportCommand, "report", "wreport", "reportplayer", "playerreport");
		return true;
	}

}
