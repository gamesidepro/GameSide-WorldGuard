package com.universeguard.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.universeguard.region.GlobalRegion;
import com.universeguard.region.Region;
import com.universeguard.utils.RegionUtils;
import com.universeguard.utils.Utils;

public class RegionTeleportExecutor implements CommandExecutor {
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		if(src instanceof Player) {
			Player player = (Player)src;
			Region r;
			
			r = RegionUtils.load(player.getLocation());
			if(r != null && r.isOwner(player.getUniqueId())) {
				if(!r.getFlag("cantp") && !RegionUtils.hasPermission(player, r)) {
					Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Вы не можете использовать эту команду в этом регионе.");
					return CommandResult.success();
				}	
			} else {
				GlobalRegion gr = RegionUtils.loadGlobal(player.getWorld().getName());
				if(gr != null) {
					if(!gr.getFlag("cantp") && !RegionUtils.hasGlobalPermission(player)) {
						Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Вы не можете использовать эту команду в этом регионе.");
						return CommandResult.success();
					}
				}
					
			}
			
			if(args.hasAny(Text.of("name"))) {
				String name = args.<String>getOne("name").get();
				r = RegionUtils.getByName(name);
				if(r != null) {
					if(RegionUtils.hasPermission(player, r)) {
						player.setLocation(r.getTeleport());
					} else
						Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Вы не можете телепортироваться в этот регион.");
				} else {
					Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Регион не найден.");
				}
					
			} else {
				Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Уточните имя региона.");
			}
				
			return CommandResult.success();
		}
		return CommandResult.empty();
	}

}
