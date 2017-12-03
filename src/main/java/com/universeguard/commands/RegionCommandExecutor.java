package com.universeguard.commands;

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

public class RegionCommandExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		if (src instanceof Player) {
			Player player = (Player) src;

			Region r;
			String name = null;
			if (UniverseGuard.instance.pendings.containsKey(player))
				r = UniverseGuard.instance.pendings.get(player);
			else {
				if (args.hasAny(Text.of("region"))) {
					name = args.<String>getOne("region").get();
					r = RegionUtils.getByName(name);
				} else {
					r = RegionUtils.load(player.getLocation());
				}
			}

			if (r != null) {
				if (args.hasAny("name")) {
					String p = args.<String>getOne("name").get();
					if (r.getCommands().contains(p)) {
						Utils.sendMessage(player, TextColors.GREEN,"[Игровая Сторона] ", args.<String>getOne("name").get(),
								TextColors.WHITE ," включен!");
						r.removeCommand(p);
						RegionUtils.save(r);
					}

					else {
						Utils.sendMessage(player, TextColors.GREEN,"[Игровая Сторона] ", args.<String>getOne("name").get(),
								TextColors.WHITE ," выключена!");
						r.addCommand(p);
						RegionUtils.save(r);
					}

				} else {
					Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Проверьте правильность ввода команды!");
				}
			} else {
				if (args.hasAny(Text.of("region"))) {
					Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Данный регион не найден");
				} else
					Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Введите название региона");
			}

			return CommandResult.success();
		}
		return CommandResult.empty();
	}

}
