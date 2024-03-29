package com.universeguard.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.universeguard.UniverseGuard;
import com.universeguard.region.Region;
import com.universeguard.utils.RegionUtils;
import com.universeguard.utils.Utils;

public class RegionEditExecutor implements CommandExecutor {
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		if(src instanceof Player) {
			Player player = (Player)src;
			
			Region r;
			String name = null;
			if(args.hasAny(Text.of("name"))) {
				name = args.<String>getOne("name").get();
				r = RegionUtils.getByName(name);
			}
			else {
				r = RegionUtils.load(player.getLocation());
			}
			
			if((r != null && r.isOwner(player.getUniqueId())) || player.hasPermission(Utils.getPermission("bypass"))) {
				if(UniverseGuard.instance.pendings.get(player) != null) {
					UniverseGuard.instance.pendings.remove(player);
				}
				UniverseGuard.instance.pendings.put(player, r);
				Sponge.getCommandManager().process(player, "region info " + r.getName());
				Utils.sendMessage(player, TextColors.GREEN, "[Игровая Сторона] ", TextColors.WHITE, "Изменение региона ", r.getName(), ". Не забудьте написать /region save когда завершите редактирование региона!");
			}
			else {
				if(name != null)
					Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Регион ", name, " не найден!");
				else
					Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "В этом месте региона нет!");
			}
			
			return CommandResult.success();
		}
		return CommandResult.empty();
	}

}
