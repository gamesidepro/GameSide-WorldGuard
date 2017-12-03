/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.universeguard.commands;

import com.universeguard.UniverseGuard;
import com.universeguard.region.Region;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 *
 * @author Twelvee
 */
public class RegionSetPos2 implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player) {
            Player player = (Player)src;
            Location<World> l;
                        l = player.getLocation();
                        if (!UniverseGuard.instance.pendings.containsKey(player)) {
                            Region r = new Region(null, l, player.getWorld().getDimension().getType().getId(),
                                player.getWorld().getName());
                                UniverseGuard.instance.pendings.put(player, r);
                                return CommandResult.success();
                        } else {
                            Region r = UniverseGuard.instance.pendings.get(player);
                            r.setPos2(l);
                            r.setDimension(player.getWorld().getDimension().getType().getId());
                            r.setWorld(player.getWorld().getName());
                            Sponge.getCommandManager().process(player, "/pos2");
                            return CommandResult.success();
                        }

        }else{
            return CommandResult.success();
        }
    }
    
}
