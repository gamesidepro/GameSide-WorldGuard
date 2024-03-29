package com.universeguard.events.flags;

import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.animal.Animal;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.SpawnEntityEvent;

import com.universeguard.region.GlobalRegion;
import com.universeguard.region.Region;
import com.universeguard.utils.RegionUtils;
import com.universeguard.utils.Utils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

public class EventSpawnEntity {
	
	@Listener
	public void onEntitySpawn(SpawnEntityEvent event) {
		if(!event.getEntities().isEmpty()) {
			
                            if (event.getEntities().get(0) instanceof Animal) {
                                Region r = RegionUtils.load(event.getEntities().get(0).getLocation());
                                    if (r != null) {
                                            event.setCancelled(!r.getFlag("animals"));
                                    } else {
                                            GlobalRegion gr = RegionUtils.loadGlobal(event.getEntities().get(0).getLocation().getExtent().getName());
                                            if(gr != null)
                                                    event.setCancelled(!gr.getFlag("animals"));
                                    }
                            } else if (event.getEntities().get(0) instanceof Monster) {
                                    Region r = RegionUtils.load(event.getEntities().get(0).getLocation());
                                            if (r != null) {
                                                    event.setCancelled(!r.getFlag("mobs"));
                                            } else {
                                                    GlobalRegion gr = RegionUtils.loadGlobal(event.getEntities().get(0).getLocation().getExtent().getName());
                                                    if(gr != null)
                                                            event.setCancelled(!gr.getFlag("mobs"));
                                            }
                            } else {
                                    if (!(event.getEntities().get(0) instanceof Item)) {
                                        
                                        Region r = RegionUtils.load(event.getEntities().get(0).getLocation());
                                            if (r != null) {
                                                    event.setCancelled(!r.getFlag("mobs"));
                                            } else {
                                                    GlobalRegion gr = RegionUtils.loadGlobal(event.getEntities().get(0).getLocation().getExtent().getName());
                                                    if(gr != null)
                                                            event.setCancelled(!gr.getFlag("mobs"));
                                            }
                                    }
                            }   
                        
		}
		
	}
}
