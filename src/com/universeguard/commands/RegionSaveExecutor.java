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

public class RegionSaveExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		if(src instanceof Player) {
			Player player = (Player)src;

			Region r = UniverseGuard.instance.pendings.get(player);
			if(r != null) {
				if(r.getPos1() == null || r.getPos2() == null) {
					Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Вы должны выделить регион!");
					return CommandResult.success();
				} else {
					if(r.getTeleport() == null)
						r.setTeleport(r.getPos1());
					if(r.getSpawn() == null)
						r.setSpawn(r.getPos1());
					RegionUtils.save(r);
					UniverseGuard.instance.pendings.remove(player);
					Utils.sendMessage(player, TextColors.GREEN, "[Игровая Сторона] ", TextColors.WHITE, "Регион ", r.getName(), " успешно сохранен!");
				}
				
			}
				
			else
				Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Вы должны выделить регион!");
			
			return CommandResult.success();
		}
		return CommandResult.empty();
	}

}
