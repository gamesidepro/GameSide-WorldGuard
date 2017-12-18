/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.universeguard.commands;

import com.universeguard.UniverseGuard;
import com.universeguard.region.Region;
import com.universeguard.utils.RegionUtils;
import com.universeguard.utils.Utils;
import java.util.UUID;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.format.TextColors;

/**
 *
 * @author Twelvee
 */
public class RegionClaimExecutor implements CommandExecutor{

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        		if(src instanceof Player) {
			Player player = (Player)src;

			Region r = UniverseGuard.instance.pendings.get(player);
			if(r != null) {
				if(args.hasAny("name")) {
					String name = args.<String>getOne("name").get();
                                        int maxprivates;
                                        if(player.hasPermission("wg.maxprivate.admin")){
                                            maxprivates = Utils.getMaxPrivateCount("maxprivate_admin");
                                        }else if(player.hasPermission("wg.maxprivate.vip")){
                                            maxprivates = Utils.getMaxPrivateCount("maxprivate_vip");
                                        }else if(player.hasPermission("wg.maxprivate.prem")){
                                            maxprivates = Utils.getMaxPrivateCount("maxprivate_prem");
                                        }else if(player.hasPermission("wg.maxprivate.megaprem")){
                                            maxprivates = Utils.getMaxPrivateCount("maxprivate_megaprem");
                                        }else{
                                            maxprivates = Utils.getMaxPrivateCount("maxprivate_default");
                                        }
                                        int mr = 0;
                                        UUID p = player.getUniqueId();
					for(Region region : UniverseGuard.instance.regions) {
						if(region.isOwner(p)) {
                                                    mr = mr + 1;
						}
					}
                                        if(mr>=maxprivates){
                                            Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Вы уже использовали все приваты доступные вам.");
                                            return CommandResult.success();
                                        }
                                        if(RegionUtils.regionInRegion(player.getLocation(), r)){
                                            Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Вы пытаетесь разместить свой приват в чужом.");
                                            return CommandResult.success();  
                                        }
					for(Region region : UniverseGuard.instance.regions) {
						if(region.getName().equalsIgnoreCase(name)) {
							Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Имя региона уже занято.");
							return CommandResult.success();
						}
					}
                                        
                                        if(r.getSize()>45000 && !player.hasPermission("wg.maxprivate.admin")){
                                            Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Максимальный размер региона - 45000 блоков.");
                                            return CommandResult.success();
                                        }else if(r.getSize()<=0){
                                            Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Вы не можете заприватить 0 блоков.");
                                            return CommandResult.success();   
                                        }
					r.setName(name);
					if(r.getTeleport() == null)
						r.setTeleport(r.getPos1());
					if(r.getSpawn() == null)
						r.setSpawn(r.getPos1());
                                        
                                       
					if(!r.getOwners().contains(p)) {
						r.addOwner(p);
						RegionUtils.save(r);
                                        }
					
					UniverseGuard.instance.pendings.remove(player);
                                        Sponge.getCommandManager().process(player, "/sel");
					Utils.sendMessage(player, TextColors.GREEN, "[Игровая Сторона] ", TextColors.WHITE, "Регион ", TextColors.GREEN, r.getName(), TextColors.WHITE, " создан! Осталось регионов: ", maxprivates-mr-1);
					//Utils.sendMessage(player, TextColors.GREEN, "Region updated!");
				}
				else {
					Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Введите название региона.");
				}
			}
			else
				Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Вы не выделили ни одного региона.");
			
			return CommandResult.success();
		}
		return CommandResult.empty();
    }
    
}
