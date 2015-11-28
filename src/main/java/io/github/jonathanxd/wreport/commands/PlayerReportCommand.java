package io.github.jonathanxd.wreport.commands;

import org.spongepowered.api.Game;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class PlayerReportCommand extends wCommandBase implements CommandExecutor{

	public PlayerReportCommand(Game game) {
		super(game);
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		return CommandResult.empty();
	}

}
