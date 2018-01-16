package com.universeguard.commands;

import com.universeguard.UniverseGuard;
import com.universeguard.region.Region;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.universeguard.utils.RegionUtils;
import com.universeguard.utils.Utils;
import java.util.UUID;
import org.spongepowered.api.text.format.TextColors;

public class RegionDeleteExecutor implements CommandExecutor {
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		if(src instanceof Player) {
			Player player = (Player)src;
			Region r;
			String name = null;
			if(UniverseGuard.instance.pendings.containsKey(player))
				r = UniverseGuard.instance.pendings.get(player);
			else {
				if(args.hasAny(Text.of("name"))) {
					name = args.<String>getOne("name").get();
					r = RegionUtils.getByName(name);
				}
				else {
					r = RegionUtils.load(player.getLocation());
				}
			}
                        if(r==null){
                            Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Выбранного региона не существует.");
                            return CommandResult.success();
                        }
                        UUID p = player.getUniqueId();
                        if(r.isOwner(p) || player.hasPermission(Utils.getPermission("bypass"))) {
                            if(args.hasAny(Text.of("name"))) {
                            	RegionUtils.delete(player, name);
                            }
                            else {
                            	RegionUtils.delete(player, player.getLocation());
                            }
                        }else{
                            Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Недостаточно прав.");
                        }
			
			return CommandResult.success();
		}
		return CommandResult.empty();
	}

}
