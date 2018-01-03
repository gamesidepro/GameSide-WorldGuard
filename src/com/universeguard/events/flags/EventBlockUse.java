package com.universeguard.events.flags;

import com.universeguard.UniverseGuard;
import com.universeguard.config.ConfigurationManager;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

import com.universeguard.region.GlobalRegion;
import com.universeguard.region.Region;
import com.universeguard.utils.RegionUtils;
import com.universeguard.utils.Utils;
import java.util.Optional;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.mutable.item.BlockItemData;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.text.format.TextColors;

public class EventBlockUse {
        
	@Listener
	public void onChestUse(InteractBlockEvent.Secondary event, @First Player player) {
		BlockSnapshot block = event.getTargetBlock();
		Region r;
                //Utils.sendMessage(player, TextColors.GOLD, "ITEMID: ", block.toString());
                boolean isTradeMarket = false;
                if(block.toString().contains("type=trade_o_mat")){
                        isTradeMarket = true;
                }
                //Block{ironchest:iron_chest}
                
                String first_id = block.getState().getType().getId();

                //Utils.sendMessage(player, TextColors.AQUA, "[1-st id] ", TextColors.RED, first_id);

		if(isTradeMarket==true){
                    if(!UniverseGuard.instance.traders.containsValue(event.getTargetBlock().hashCode())){
                        UniverseGuard.instance.traders.put(player, event.getTargetBlock().hashCode());
                    }else{
                        event.setCancelled(true);
                        Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона] ", TextColors.WHITE, "Этот торговый автомат уже используют.");
                    }
                }else if ((block.getState().getType().equals(BlockTypes.CHEST)
				|| block.getState().getType().toString().split(":")[0].equals("Block{ironchest")
                                || block.getState().getType().toString().equals("Block{advanced_solar_panels:machines}")
                                || block.getState().getType().toString().split(":")[0].equals("Block{refinedstorage")
                                || block.getState().getType().toString().equals("Block{ic2:te}")
                                || ConfigurationManager.getInstance().getConfig().getNode("blockuse").getNode(first_id).getBoolean()==true)
                        && isTradeMarket==false) {
			r = RegionUtils.load(block.getLocation().get());
			if (r != null) {
				if (!RegionUtils.hasPermission(player, r)){
					event.setCancelled(!r.getFlag("chests"));
                                        Utils.sendMessage(player, TextColors.RED, "[Игровая Сторона]", TextColors.WHITE," Вам запрещено взаимодействовать с этим блоком.");
                                }
			}
		} else if (block.getState().getType().equals(BlockTypes.ENDER_CHEST)) {
			r = RegionUtils.load(block.getLocation().get());
			if (r != null) {
				if (!RegionUtils.hasPermission(player, r))
					event.setCancelled(!r.getFlag("enderchests"));
			}
		} else if (!block.getState().getType().equals(BlockTypes.AIR)
				&& !block.getState().getType().equals(BlockTypes.CRAFTING_TABLE)
				&& !block.getState().getType().equals(BlockTypes.ANVIL)
				&& !block.getState().getType().equals(BlockTypes.ENCHANTING_TABLE)) {
			r = RegionUtils.load(block.getLocation().get());
			if (r != null) {
				if (!RegionUtils.hasPermission(player, r))
					event.setCancelled(!r.getFlag("use"));
			}

		}
                
                
	}

        @Listener
        public void chestClose(InteractInventoryEvent.Close event, @First Player player){
            if(UniverseGuard.instance.traders.containsKey(player)){
                UniverseGuard.instance.traders.remove(player);
            }
        }
        
	@Listener
	public void interact(InteractEntityEvent.Secondary event, @First Player player) {
		EntityType e = event.getTargetEntity().getType();
		if (e == EntityTypes.ITEM_FRAME || e == EntityTypes.ARMOR_STAND || e == EntityTypes.PAINTING) {
			Region r = RegionUtils.load(event.getTargetEntity().getLocation());
			if (r != null) {
				if (!RegionUtils.hasPermission(player, r))
					event.setCancelled(!r.getFlag("use"));
			}

		}
	}

}
