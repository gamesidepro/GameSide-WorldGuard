package com.universeguard.utils;

import com.universeguard.UniverseGuard;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.universeguard.region.GlobalRegion;
import com.universeguard.region.Region;
import java.util.ConcurrentModificationException;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class RegionUtils {

	public static void loadGlobalRegions() {
		for(World w : Sponge.getServer().getWorlds()) {
			if(RegionUtils.loadGlobal(w.getName()) == null)
				RegionUtils.saveGlobal(new GlobalRegion(w.getName()));
		}
	}
	
	public static void getAllRegions(ArrayList<Region> regions) {
		
		File dir = new File("config/universeguard/regions");
		if (dir.exists()) {
			File[] files = dir.listFiles();

			for (File file : files) {
				if (file.exists()) {
					Region r = getByName(file.getName().substring(0, file.getName().indexOf(".json")));
					if (r != null)
						regions.add(r);
				}
			}

		}

	}

	public static boolean isInRegion(Region r, Location<World> l) {
		Location<World> pos1 = r.getPos1();
		Location<World> pos2 = r.getPos2();
		
		int x1 = Math.min(pos1.getBlockX(), pos2.getBlockX());
		int y1 = Math.min(pos1.getBlockY(), pos2.getBlockY());
		int z1 = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
		int x2 = Math.max(pos1.getBlockX(), pos2.getBlockX());
		int y2 = Math.max(pos1.getBlockY(), pos2.getBlockY());
		int z2 = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
		
		return r.getWorld().equalsIgnoreCase(l.getExtent().getName())
				&& r.getDimension().equals(l.getExtent().getDimension().getType().getId())
				&& ((l.getBlockX() >= x1 && l.getBlockX() <= x2)
				&& (l.getBlockY() >= y1 && l.getBlockY() <= y2)
				&& (l.getBlockZ() >= z1 && l.getBlockZ() <= z2));
	}

	public static Region load(Location<World> location) {
                
		ArrayList<Region> regions = new ArrayList<Region>();
		if(UniverseGuard.instance.regions != null) {
			for (Region r : UniverseGuard.instance.regions) {
				if (isInRegion(r, location))
					regions.add(r);
			}
		}
		
		if(!regions.isEmpty()) {
			Region r = regions.get(0);
			for(Region reg : regions) {
				if(reg.getPriority() > r.getPriority())
					r = reg;
			}
			return r;
		}
		
		return null;
	}
	
        private static boolean regionInRegionT(Region r1, Region r2){
            boolean isX = false;
            boolean isY = false;
            boolean isZ = false;
            
            int x_1_min = Math.min(Math.abs(r1.getPos1().getBlockX()), Math.abs(r1.getPos2().getBlockX()));
            int x_1_max = Math.max(Math.abs(r1.getPos1().getBlockX()), Math.abs(r1.getPos2().getBlockX()));
            int x_2_min = Math.min(Math.abs(r2.getPos1().getBlockX()), Math.abs(r2.getPos2().getBlockX()));
            int x_2_max = Math.max(Math.abs(r2.getPos1().getBlockX()), Math.abs(r2.getPos2().getBlockX()));
            
            int y_1_min = Math.min(Math.abs(r1.getPos1().getBlockY()), Math.abs(r1.getPos2().getBlockY()));
            int y_1_max = Math.max(Math.abs(r1.getPos1().getBlockY()), Math.abs(r1.getPos2().getBlockY()));
            int y_2_min = Math.min(Math.abs(r2.getPos1().getBlockY()), Math.abs(r2.getPos2().getBlockY()));
            int y_2_max = Math.max(Math.abs(r2.getPos1().getBlockY()), Math.abs(r2.getPos2().getBlockY()));
            
            int z_1_min = Math.min(Math.abs(r1.getPos1().getBlockZ()), Math.abs(r1.getPos2().getBlockZ()));
            int z_1_max = Math.max(Math.abs(r1.getPos1().getBlockZ()), Math.abs(r1.getPos2().getBlockZ()));
            int z_2_min = Math.min(Math.abs(r2.getPos1().getBlockZ()), Math.abs(r2.getPos2().getBlockZ()));
            int z_2_max = Math.max(Math.abs(r2.getPos1().getBlockZ()), Math.abs(r2.getPos2().getBlockZ()));
            

                for(int j = x_2_min; j<=x_2_max;j++){
                    if(j<=x_1_max && j>=x_1_min){
                        isX = true;
                        break;
                    }
                }
            

                for(int j = y_2_min; j<=y_2_max;j++){
                    if(j<=y_1_max && j>=y_1_min){
                        isY = true;
                        break;
                    }
                }
            

                for(int j = z_2_min; j<=z_2_max;j++){
                    if(j<=z_1_max && j>=z_1_min){
                        isZ = true;
                        break;
                    }
                }
            

            if (isX && isY && isZ)
                return true;
            else
                return false;
        }
        
        public static boolean regionInRegion(Location<World> location, Region region){
		if(UniverseGuard.instance.regions != null) {
			for (Region r : UniverseGuard.instance.regions) {
				if (regionInRegionT(region, r)){
                                        return true;
                                }
			}
		}
            return false;
        }
        
	public static void save(Region r) {
		String name = r.getName();
		if(name.isEmpty()) {
			name = "Region";
			int d = 0;
			for(Region region : UniverseGuard.instance.regions) {
				String n = region.getName();
				if(n.startsWith("Region"))
					d++;
			}
			name += String.valueOf(d);
		}
		File file = new File("config/universeguard/regions/" + name + ".json");
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		ConfigurationLoader<ConfigurationNode> loader = GsonConfigurationLoader.builder().setFile(file).build();

		ConfigurationNode root;

		try {
			root = loader.load();
		} catch (IOException e) {
			root = loader.createEmptyNode(ConfigurationOptions.defaults());
		}

		ConfigurationNode region = root.getNode(name);

		HashMap<String, Integer> pos1 = new HashMap<String, Integer>();
		pos1.put("x", r.getPos1().getBlockX());
		pos1.put("y", r.getPos1().getBlockY());
		pos1.put("z", r.getPos1().getBlockZ());
		region.getNode("pos1").setValue(pos1);

		HashMap<String, Integer> pos2 = new HashMap<String, Integer>();
		pos2.put("x", r.getPos2().getBlockX());
		pos2.put("y", r.getPos2().getBlockY());
		pos2.put("z", r.getPos2().getBlockZ());
		region.getNode("pos2").setValue(pos2);

		region.getNode("dimension").setValue(r.getDimension());
		region.getNode("world").setValue(r.getWorld());
		region.getNode("priority").setValue(r.getPriority());
		region.getNode("gamemode").setValue(r.getGameMode().getId());

		HashMap<String, Integer> teleport = new HashMap<String, Integer>();
		teleport.put("x", r.getTeleport().getBlockX());
		teleport.put("y", r.getTeleport().getBlockY());
		teleport.put("z", r.getTeleport().getBlockZ());
		region.getNode("teleport").setValue(teleport);

		HashMap<String, Integer> spawn = new HashMap<String, Integer>();
		spawn.put("x", r.getSpawn().getBlockX());
		spawn.put("y", r.getSpawn().getBlockY());
		spawn.put("z", r.getSpawn().getBlockZ());
		region.getNode("spawn").setValue(spawn);

		HashMap<String, Boolean> flags = new HashMap<String, Boolean>();
		for (int i = 0; i < r.getAllFlags().size(); i++) {
			flags.put(Region.getFlagNames().get(i), r.getAllFlags().get(i));
		}

		ArrayList<String> owners = new ArrayList<String>();
		for (int i = 0; i < r.getOwners().size(); i++) {
			owners.add(r.getOwners().get(i).toString());
		}

		ArrayList<String> members = new ArrayList<String>();
		for (int i = 0; i < r.getMembers().size(); i++) {
			members.add(r.getMembers().get(i).toString());
		}
                
		region.getNode("flags").setValue(flags);
		region.getNode("owners").setValue(owners);
		region.getNode("members").setValue(members);
                
		ArrayList<String> commands = new ArrayList<String>();
		for (int i = 0; i < r.getCommands().size(); i++) {
			commands.add(r.getCommands().get(i).toString());
		}
		
		region.getNode("commands").setValue(commands);

		try {
			loader.save(root);
			int ind = UniverseGuard.instance.regions.indexOf(r);
			if (ind != -1)
			    UniverseGuard.instance.regions.remove(ind);
			UniverseGuard.instance.regions.add(r);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

        public static long getSizeByName(String name){
            File file = new File("config/universeguard/regions/" + name + ".json");
            if (!file.exists()) {
		return 0;
            }
            
            ConfigurationLoader<ConfigurationNode> loader = GsonConfigurationLoader.builder().setFile(file).build();

            ConfigurationNode root;

            try {
            	root = loader.load();
            } catch (IOException e) {
		root = loader.createEmptyNode(ConfigurationOptions.defaults());
            }

            ConfigurationNode region = root.getNode(name);
            if(Sponge.getServer().getWorld(region.getNode("world").getString()).isPresent()) {
                long size;
                
                World world = Sponge.getServer().getWorld(region.getNode("world").getString()).get();
		Location<World> pos1 = new Location<World>(world, region.getNode("pos1").getNode("x").getInt(),
		region.getNode("pos1").getNode("y").getInt(), region.getNode("pos1").getNode("z").getInt());

		Location<World> pos2 = new Location<World>(world, region.getNode("pos2").getNode("x").getInt(),
		region.getNode("pos2").getNode("y").getInt(), region.getNode("pos2").getNode("z").getInt());
                int a = region.getNode("pos2").getNode("x").getInt()-region.getNode("pos1").getNode("x").getInt();
                int b = region.getNode("pos2").getNode("y").getInt()-region.getNode("pos1").getNode("y").getInt();
                int c = region.getNode("pos2").getNode("z").getInt()-region.getNode("pos1").getNode("z").getInt();
                size = a*b*c;
                return size;
            }else{
                return 0;
            }
        }
        
	public static Region getByName(String name) {
		File file = new File("config/universeguard/regions/" + name + ".json");
		if (!file.exists()) {
			return null;
		}

		ConfigurationLoader<ConfigurationNode> loader = GsonConfigurationLoader.builder().setFile(file).build();

		ConfigurationNode root;

		try {
			root = loader.load();
		} catch (IOException e) {
			root = loader.createEmptyNode(ConfigurationOptions.defaults());
		}

		ConfigurationNode region = root.getNode(name);
		if(Sponge.getServer().getWorld(region.getNode("world").getString()).isPresent()) {
			World world = Sponge.getServer().getWorld(region.getNode("world").getString()).get();
			Location<World> pos1 = new Location<World>(world, region.getNode("pos1").getNode("x").getInt(),
					region.getNode("pos1").getNode("y").getInt(), region.getNode("pos1").getNode("z").getInt());

			Location<World> pos2 = new Location<World>(world, region.getNode("pos2").getNode("x").getInt(),
					region.getNode("pos2").getNode("y").getInt(), region.getNode("pos2").getNode("z").getInt());

			Location<World> teleport = new Location<World>(world, region.getNode("teleport").getNode("x").getInt(),
					region.getNode("teleport").getNode("y").getInt(), region.getNode("teleport").getNode("z").getInt());

			Location<World> spawn = new Location<World>(world, region.getNode("spawn").getNode("x").getInt(),
					region.getNode("spawn").getNode("y").getInt(), region.getNode("spawn").getNode("z").getInt());

			String d = region.getNode("dimension").getString();

			String w = region.getNode("world").getString();
			


			Region r = new Region(pos1, pos2, d, w);

			r.setName(name);
			r.setPriority(region.getNode("priority").getInt());
			r.setGameMode(region.getNode("gamemode").getString());
			r.setTeleport(teleport);
			r.setSpawn(spawn);

			for (int i = 0; i < r.getAllFlags().size(); i++) {
				r.setFlag(Region.getFlagNames().get(i), region.getNode("flags").getNode(Region.getFlagNames().get(i)).getBoolean());
			}

			ArrayList<UUID> owners = new ArrayList<UUID>();
			for (int i = 0; i < region.getNode("owners").getChildrenList().size(); i++) {
				String s = region.getNode("owners").getChildrenList().get(i).getString();
				UUID id = UUID.fromString(s);
				owners.add(id);
			}

			r.setOwners(owners);
			
			ArrayList<UUID> members = new ArrayList<UUID>();
			for (int i = 0; i < region.getNode("members").getChildrenList().size(); i++) {
				String s = region.getNode("members").getChildrenList().get(i).getString();
				UUID id = UUID.fromString(s);
				members.add(id);
			}

			r.setMembers(members);
                        
			ArrayList<String> commands = new ArrayList<String>();
			for (int i = 0; i < region.getNode("commands").getChildrenList().size(); i++) {
				String s = region.getNode("commands").getChildrenList().get(i).getString();
				commands.add(s);
			}
			
			r.setCommands(commands);

			return r;
		}
		return null;
	}
	
	public static void delete(Player p, String name) {
		File file = new File("config/universeguard/regions/" + name + ".json");
		if (file.exists()) {
			if(file.delete()){
                            try{
                                for(Region ar : UniverseGuard.instance.regions){
                                    if(ar.getName().equals(name))
                                        UniverseGuard.instance.regions.remove(ar);
                                }
				Utils.sendMessage(p, TextColors.GREEN, "[Игровая Сторона] ", name, TextColors.WHITE, " успешно удален!");
                            }catch(ConcurrentModificationException e){
                                Utils.sendMessage(p, TextColors.GREEN, "[Игровая Сторона] ", name, TextColors.WHITE, " успешно удален!");
                                //Utils.sendMessage(p, TextColors.RED, "[Игровая Сторона] ", name, TextColors.WHITE, " Произошла ошибка сервера!");
                                e.printStackTrace();
                            }
                        }else
				Utils.sendMessage(p, TextColors.RED, "Can't delete the region ", name, "!");
		}
		else
			Utils.sendMessage(p, TextColors.RED, "Can't find the region ", name, "!");
	}
	
	public static void delete(String name) {
		File file = new File("config/universeguard/regions/" + name + ".json");
		if (file.exists()) {
                    try{
                        for(Region ar : UniverseGuard.instance.regions){
                            if(ar.getName().equals(name))
                                UniverseGuard.instance.regions.remove(ar);
                        }
			file.delete();   
                    }catch(ConcurrentModificationException e){
                        e.printStackTrace();
                    }
		}
	}
	
	public static void delete(Player p, Location<World> l) {
		Region r = load(l);
		if(r != null){
			delete(p, r.getName());
                        //Utils.sendMessage(p, TextColors.GREEN, "[Игровая Сторона] ", r.getName(), TextColors.WHITE, " успешно удален!");
                }else
			Utils.sendMessage(p, TextColors.RED, "There's no region here!");
		
	}
	
	public static boolean hasPermission(Player p, Region r) {
		return r.getOwners().contains(p.getUniqueId()) || r.getMembers().contains(p.getUniqueId()) || p.hasPermission(Utils.getPermission("bypass"));
	}
	
	public static boolean hasGlobalPermission(Player p) {
		return p.hasPermission(Utils.getPermission("permission")) || p.hasPermission(Utils.getPermission("bypass"));
	}
	
	
	public static GlobalRegion loadGlobal(String name) {
		File file = new File("config/universeguard/globals/" + name + ".json");
		if (!file.exists()) {
			return null;
		}

		ConfigurationLoader<ConfigurationNode> loader = GsonConfigurationLoader.builder().setFile(file).build();

		ConfigurationNode root;

		try {
			root = loader.load();
		} catch (IOException e) {
			root = loader.createEmptyNode(ConfigurationOptions.defaults());
		}

		ConfigurationNode region = root.getNode(name);
		String w = region.getNode("world").getString();

		GlobalRegion r = new GlobalRegion(w);

		r.setName(name);

		for (int i = 0; i < r.getAllFlags().size(); i++) {
			r.setFlag(Region.getFlagNames().get(i), region.getNode("flags").getNode(Region.getFlagNames().get(i)).getBoolean());
		}
		
		ArrayList<String> commands = new ArrayList<String>();
		for (int i = 0; i < region.getNode("commands").getChildrenList().size(); i++) {
			String s = region.getNode("commands").getChildrenList().get(i).getString();
			commands.add(s);
		}
		
		r.setCommands(commands);

		return r;
	}
	
	public static void saveGlobal(GlobalRegion r) {
		String name = r.getName();
		File file = new File("config/universeguard/globals/" + name + ".json");
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		ConfigurationLoader<ConfigurationNode> loader = GsonConfigurationLoader.builder().setFile(file).build();

		ConfigurationNode root;

		try {
			root = loader.load();
		} catch (IOException e) {
			root = loader.createEmptyNode(ConfigurationOptions.defaults());
		}

		ConfigurationNode region = root.getNode(name);

		region.getNode("world").setValue(r.getWorld());
		

		HashMap<String, Boolean> flags = new HashMap<String, Boolean>();
		for (int i = 0; i < r.getAllFlags().size(); i++) {
			flags.put(Region.getFlagNames().get(i), r.getAllFlags().get(i));
		}

		region.getNode("flags").setValue(flags);
		
		ArrayList<String> commands = new ArrayList<String>();
		for (int i = 0; i < r.getCommands().size(); i++) {
			commands.add(r.getCommands().get(i).toString());
		}
		
		region.getNode("commands").setValue(commands);

		try {
			loader.save(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
