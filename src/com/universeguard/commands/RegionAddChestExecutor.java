/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.universeguard.commands;

import com.universeguard.config.ConfigurationManager;
import com.universeguard.utils.Utils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import static org.spongepowered.api.command.args.GenericArguments.player;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.format.TextColors;

/**
 *
 * @author redap
 */
public class RegionAddChestExecutor implements CommandExecutor {
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		if(src instanceof Player) {
                    Player player = (Player) src;
                    ItemStack stackInHand = player.getItemInHand(HandTypes.MAIN_HAND).orElseGet(()-> ItemStack.of(ItemTypes.NONE, 0));
                    String first_id = stackInHand.getItem().getName();
                    if(ConfigurationManager.getInstance().getConfig().getNode("blockuse").getNode(first_id).getBoolean()==false){
                        ConfigurationManager.getInstance().getConfig().getNode("blockuse").getNode(first_id).setValue(true);
                        Utils.sendMessage(player, TextColors.GREEN, "[Игровая Сторона] ", first_id, TextColors.WHITE, " Успешно добавлен в список.");
                        ConfigurationManager.getInstance().saveConfig();
                    }else{
                        Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", first_id, TextColors.WHITE, " Уже есть в списке.");   
                    }
                }
                
                return CommandResult.success();
        }
}
