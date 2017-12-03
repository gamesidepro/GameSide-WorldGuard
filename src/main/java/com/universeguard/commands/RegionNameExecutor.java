package com.universeguard.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.format.TextColors;

import com.universeguard.UniverseGuard;
import com.universeguard.region.Region;
import com.universeguard.utils.RegionUtils;
import com.universeguard.utils.Utils;

public class RegionNameExecutor implements CommandExecutor {
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		if(src instanceof Player) {
			Player player = (Player)src;

			Region r = UniverseGuard.instance.pendings.get(player);
			if(r != null && r.isOwner(player.getUniqueId())) {
				if(args.hasAny("name")) {
					String name = args.<String>getOne("name").get();
					for(Region region : RegionUtils.getAllRegions()) {
						if(region.getName().equalsIgnoreCase(name)) {
							Utils.sendMessage(player, TextColors.RED, "Данное имя региона занято!");
							return CommandResult.success();
						}
					}
					r.setName(name);
					Utils.sendMessage(player, TextColors.GREEN, "Регион обновлен!");
				}
				else {
					Utils.sendMessage(player, TextColors.RED, "Пожалуйста, уточните имя!");
				}
			}
			else
				Utils.sendMessage(player, TextColors.RED, "Вы не выделили ни одного региона, чтобы переименовать!");
			
			return CommandResult.success();
		}
		return CommandResult.empty();
	}

}
