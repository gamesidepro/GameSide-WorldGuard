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
import org.spongepowered.api.event.entity.ChangeEntityEquipmentEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.text.format.TextColors;

public class EventLeftClick {
   
	@Listener
	public void onLeftClickMainHand(InteractBlockEvent.Primary.MainHand event) {
		Player player = event.getCause().last(Player.class).get();
		if (player != null) {
			if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
				ItemStack stack = player.getItemInHand(HandTypes.MAIN_HAND).get();
				if (Utils.isSelector(stack)) {
					//event.setCancelled(true);
					Location<World> l;
					if (event.getTargetBlock().getState().getType() != BlockTypes.AIR)
						l = event.getTargetBlock().getLocation().get();
					else
						l = player.getLocation();
					if (!UniverseGuard.instance.pendings.containsKey(player)) {
						Region r = new Region(l, null, player.getWorld().getDimension().getType().getId(),
								player.getWorld().getName());
						UniverseGuard.instance.pendings.put(player, r);
					} else {
						Region r = UniverseGuard.instance.pendings.get(player);
						r.setPos1(l);
						r.setDimension(player.getWorld().getDimension().getType().getId());
						r.setWorld(player.getWorld().getName());
					}
                                        
                                        //PlayerInfo.plinfo.setFP(player.getName(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
                                        
					//Utils.sendMessage(player, TextColors.YELLOW, "Первая позиция: ", l.getBlockX(), " ", l.getBlockY(), " ", l.getBlockZ());
				}
			}
			
		}
	}
        
}
