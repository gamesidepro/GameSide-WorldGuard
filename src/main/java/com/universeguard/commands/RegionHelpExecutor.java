package com.universeguard.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.universeguard.utils.Utils;

public class RegionHelpExecutor implements CommandExecutor {
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		if(src instanceof Player) {
			Player player = (Player)src;
			int page = 1;
			if(args.hasAny(Text.of("page"))) {
				page = args.<Integer>getOne("page").get();
			}
			if(page <= 1) {
				Utils.sendMessage(player, TextColors.GOLD, "------------------Помощь(1/5)------------------");
				Utils.sendMessage(player, TextColors.YELLOW, "/region -", TextColors.WHITE, " ", TextColors.WHITE, " Получить палку-приват-создавалку");
				Utils.sendMessage(player, TextColors.YELLOW, "/region save -", TextColors.WHITE, " Сохранить регион");
				Utils.sendMessage(player, TextColors.YELLOW, "/region info (region) -", TextColors.WHITE, " Информация о регионе");
				Utils.sendMessage(player, TextColors.YELLOW, "/region delete (region) -", TextColors.WHITE, " Удалить регион");
				Utils.sendMessage(player, TextColors.YELLOW, "/region name [name] -", TextColors.WHITE, " Переименовать регион");
			}
			if(page == 2) {
				Utils.sendMessage(player, TextColors.GOLD, "------------------Помощь(2/5)------------------");
				Utils.sendMessage(player, TextColors.YELLOW, "/region list -", TextColors.WHITE, " Посмотреть все свои регионы");
				Utils.sendMessage(player, TextColors.YELLOW, "/region gamemode [gamemode] -", TextColors.WHITE, " Установить режим игры в регионе");
				Utils.sendMessage(player, TextColors.YELLOW, "/region edit (region) -", TextColors.WHITE, " Разрешить редактирование региона");
				Utils.sendMessage(player, TextColors.YELLOW, "/region flag [flag] [value] -", TextColors.WHITE, " Установить флаг");
				Utils.sendMessage(player, TextColors.YELLOW, "/region add [player] (region) -", TextColors.WHITE, " Добавить пользователя в регион");
			}
			if(page == 3) {
				Utils.sendMessage(player, TextColors.GOLD, "------------------Помощь(3/5)------------------");
				Utils.sendMessage(player, TextColors.YELLOW, "/region remove [player] (region) -", TextColors.WHITE, " Удалить пользователя из региона");
				Utils.sendMessage(player, TextColors.YELLOW, "/region global [flag] [value] -", TextColors.WHITE, " Установить глобальный флаг");
				Utils.sendMessage(player, TextColors.YELLOW, "/region globallist -", TextColors.WHITE, " Посмотреть все флаги (глобальные)");
				Utils.sendMessage(player, TextColors.YELLOW, "/region setteleport [x] [y] [z] -", TextColors.WHITE, " Установить точку телепорта");
				Utils.sendMessage(player, TextColors.YELLOW, "/region setspawn [x] [y] [z] -", TextColors.WHITE, " Установить точку спавна");
				
			}
			if(page == 4) {
				Utils.sendMessage(player, TextColors.GOLD, "------------------Помощь(4/5)------------------");
				Utils.sendMessage(player, TextColors.YELLOW, "/region teleport [region] -", TextColors.WHITE, " Телепортироваться в регион");
				Utils.sendMessage(player, TextColors.YELLOW, "/region spawn [region] -", TextColors.WHITE, " Возродиться в регионе");
				Utils.sendMessage(player, TextColors.YELLOW, "/region priority [value] -", TextColors.WHITE, " Установить приоритет региона");
				Utils.sendMessage(player, TextColors.YELLOW, "/region command [command] (region) -", TextColors.WHITE, " Разрешить или запретить использование команд");
				Utils.sendMessage(player, TextColors.YELLOW, "/region help -", TextColors.WHITE, " Помощь");
			}
			if(page >= 5) {
				Utils.sendMessage(player, TextColors.GOLD, "------------------Help(5/5)------------------");
				Utils.sendMessage(player, TextColors.YELLOW, "/region flaghelp -", TextColors.WHITE, " Помощь по флагам");
				Utils.sendMessage(player, TextColors.GOLD, "--------------------------------------------");
			}
			
			return CommandResult.success();
		}
		return CommandResult.empty();
	}

}
