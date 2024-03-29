package com.universeguard.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.universeguard.region.Region;
import com.universeguard.utils.RegionUtils;
import com.universeguard.utils.Utils;

public class RegionInfoExecutor implements CommandExecutor {
	
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
				
			
			if(r != null) {
				Utils.sendMessage(player, TextColors.YELLOW,	"[--------Информация о регионе ", TextColors.AQUA, r.getName(), "--------]");
				Utils.sendMessage(player, TextColors.GREEN, 	"|От: ", Utils.locationToString(r.getPos1()));
				Utils.sendMessage(player, TextColors.GREEN, 	"|До: ", Utils.locationToString(r.getPos2()));
			
				Utils.sendMessage(player, TextColors.AQUA, 		"|--Флаги:");
				
				ArrayList<Text> temp = new ArrayList<Text>();
				for (int i = 0; i < Region.getFlagNames().size(); i++) {
					if(r.getFlag(Region.getFlagNames().get(i))) {
						if(i != Region.getFlagNames().size() -1)
							temp.add(Text.of(TextColors.GREEN, Region.getFlagNames().get(i), ", "));
						else
							temp.add(Text.of(TextColors.GREEN, Region.getFlagNames().get(i)));
					}
					else
					{
						if(i != Region.getFlagNames().size() -1)
							temp.add(Text.of(TextColors.RED, Region.getFlagNames().get(i), ", "));
						else
							temp.add(Text.of(TextColors.RED, Region.getFlagNames().get(i)));
					}
				}
				
				
				Utils.sendMessage(player, temp.toArray());
				
				temp.clear();
				Utils.sendMessage(player, TextColors.AQUA, 		"|--Владельцы:");
				for(UUID owner : r.getOwners()) {
					Text p = Utils.getUser(owner);
					temp.add(p);
				}

				Utils.sendMessage(player, temp.toArray());
				
				temp.clear();

				Utils.sendMessage(player, TextColors.AQUA, 		"|--Жители:");
				for(UUID member : r.getMembers()) {
					Text p = Utils.getUser(member);
					temp.add(p);
				}
				
				Utils.sendMessage(player, temp.toArray());
			}
			else {
				if(name != null) 
					Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Невозможно найти регион ", name, "!");
				else
					Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Введите название региона");
			}
			
			return CommandResult.success();
		}
		return CommandResult.empty();
	}

}
