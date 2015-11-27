package io.github.jonathanxd.wreport.commands;

import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class AdminReportCommand extends wCommandBase implements CommandExecutor {

	public AdminReportCommand(Game game) {
		super(game);
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		boolean isPlayer = src instanceof Player;
		boolean argumentPresent = args.hasAny("player");

		if (!isPlayer && !argumentPresent) {
			src.sendMessage(Texts.builder("Error, only players can use this command!").style(TextStyles.BOLD)
					.color(TextColors.RED).build());
			return CommandResult.empty();
		} else {
			Player player = args.<Player>getOne("player").get();
			// TODO Admins see all player reports in a GUI
		}
		return CommandResult.success();
	}

}
