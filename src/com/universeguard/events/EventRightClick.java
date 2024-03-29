package com.universeguard.events;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.item.inventory.ItemStack;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.UniverseGuard;
import com.universeguard.region.Region;
import com.universeguard.utils.Utils;


public class EventRightClick {
        long size;

	@Listener
	public void onRightClickMainHand(InteractBlockEvent.Secondary.MainHand event) {

		Player player = event.getCause().last(Player.class).get();
		if (player != null) {
			if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
				ItemStack stack = player.getItemInHand(HandTypes.MAIN_HAND).get();
				if (Utils.isSelector(stack)) {
					event.setCancelled(true);
					Location<World> l;
					if (event.getTargetBlock().getState().getType() != BlockTypes.AIR)
						l = event.getTargetBlock().getLocation().get();
					else
						l = player.getLocation();
					if (!UniverseGuard.instance.pendings.containsKey(player)) {
						Region r = new Region(null, l, player.getWorld().getDimension().getType().getId(),
								player.getWorld().getName());
						UniverseGuard.instance.pendings.put(player, r);
                                                size = r.getSize();
					} else {
						Region r = UniverseGuard.instance.pendings.get(player);
						r.setPos2(l);
						r.setDimension(player.getWorld().getDimension().getType().getId());
						r.setWorld(player.getWorld().getName());
                                                size = r.getSize();
					}
                                        //UniverseGuard.instance.plrg.put(player.getName(), plrinfo);
                                        //PlayerInfo.plinfo.setSP(player.getName(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
                                        //PlayerInfo plrinfo = PlayerInfo.plinfo;
					//Utils.sendMessage(player, TextColors.YELLOW, "Вторая позиция: ", l.getBlockX(), " ", l.getBlockY(), " ", l.getBlockZ(), TextColors.AQUA, " [Размер: "+size+"]");
				}
			}
                        
		}
                
	}
	
	@Listener
	public void onRightClickOffHand(InteractBlockEvent.Secondary.OffHand event) {
		Player player = event.getCause().last(Player.class).get();
		if (player != null) {
			if(player.getItemInHand(HandTypes.OFF_HAND).isPresent()) {
				ItemStack stack = player.getItemInHand(HandTypes.OFF_HAND).get();
				if (Utils.isSelector(stack)) {
					event.setCancelled(true);
					Location<World> l;
					if (event.getTargetBlock().getState().getType() != BlockTypes.AIR)
						l = event.getTargetBlock().getLocation().get();
					else
						l = player.getLocation();
					if (!UniverseGuard.instance.pendings.containsKey(player)) {
						Region r = new Region(null, l, player.getWorld().getDimension().getType().getId(),
								player.getWorld().getName());
						UniverseGuard.instance.pendings.put(player, r);
					} else {
						Region r = UniverseGuard.instance.pendings.get(player);
						r.setPos2(l);
						r.setDimension(player.getWorld().getDimension().getType().getId());
						r.setWorld(player.getWorld().getName());
					}
                                        //PlayerInfo.plinfo.setSP(player.getName(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
					//Utils.sendMessage(player, TextColors.YELLOW, "Вторая позиция: ", l.getBlockX(), " ", l.getBlockY(), " ", l.getBlockZ(), TextColors.AQUA, " [Размер: "+size+"]");
				}
			}
			
		}

	}
}
