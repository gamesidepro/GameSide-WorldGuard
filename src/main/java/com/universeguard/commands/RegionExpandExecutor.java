package com.universeguard.commands;

import com.universeguard.UniverseGuard;
import com.universeguard.region.Region;
import com.universeguard.utils.Utils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 *
 * @author Twelvee
 */
public class RegionExpandExecutor implements CommandExecutor{
    String position;
    int value;
    Location<World> nl;                
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player) {
            Player player = (Player)src;
            Region r = UniverseGuard.instance.pendings.get(player);
            if(r!=null){
                if(r.getPos1() == null || r.getPos2() == null) {
                    Utils.sendMessage(player, TextColors.RED,"[Игровая Сторона] ", TextColors.WHITE, "Вы не выделили ни одного региона.");
                }else{
                    position = args.<String>getOne("position").get().toString();
                    value = args.<Integer>getOne("value").get();
                    
                    if(position.equals("up") || position.equals("u")){
                        Location<World> l = r.getPos2();
                        int newx = l.getBlockX();
                        int newy = l.getBlockY()+value;
                        int newz = l.getBlockZ();
                        World world = Sponge.getServer().getWorld("world").get();
                        Location<World> nl1 = new Location<World>(world, newx, newy, newz);
                        r.setPos2(nl1);
                        //Utils.sendMessage(player, TextColors.RED,"[Игровая Сторона] ", TextColors.WHITE, "Текущая позиция Y: ", r.getPos2().getBlockY());
                        Sponge.getCommandManager().process(player, "/expand "+value+" up");
                    }else if(position.equals("down") || position.equals("d")){
                        Location<World> l = r.getPos1();
                        int newx = l.getBlockX();
                        int newy = l.getBlockY()-value;
                        int newz = l.getBlockZ();
                                                World world = Sponge.getServer().getWorld("world").get();                        Location<World> nl1 = new Location<World>(world, newx, newy, newz);
                        r.setPos1(nl1);
                        
                        Sponge.getCommandManager().process(player, "/expand "+value+" down");
                    }else if(position.equals("west") || position.equals("w")){
                        if(r.getPos1().getBlockX()>r.getPos2().getBlockX()){
                            Location<World> l = r.getPos2();
                            int newx = l.getBlockX()-value;
                            int newy = l.getBlockY();
                            int newz = l.getBlockZ();
                                                    World world = Sponge.getServer().getWorld("world").get();                        Location<World> nl1 = new Location<World>(world, newx, newy, newz);
                            r.setPos2(nl1);
                            Sponge.getCommandManager().process(player, "/expand "+value+" west");
                        }else{
                            Location<World> l = r.getPos1();
                            int newx = l.getBlockX()-value;
                            int newy = l.getBlockY();
                            int newz = l.getBlockZ();
                                                    World world = Sponge.getServer().getWorld("world").get();                        Location<World> nl1 = new Location<World>(world, newx, newy, newz);
                            r.setPos1(nl1);
                            Sponge.getCommandManager().process(player, "/expand "+value+" west");
                        }
                    }else if(position.equals("east") || position.equals("e")){
                        if(r.getPos1().getBlockX()>r.getPos2().getBlockX()){
                            Location<World> l = r.getPos1();
                            int newx = l.getBlockX()+value;
                            int newy = l.getBlockY();
                            int newz = l.getBlockZ();
                                                    World world = Sponge.getServer().getWorld("world").get();                        Location<World> nl1 = new Location<World>(world, newx, newy, newz);
                            r.setPos1(nl1);
                            Sponge.getCommandManager().process(player, "/expand "+value+" east");
                        }else{
                            Location<World> l = r.getPos2();
                            int newx = l.getBlockX()+value;
                            int newy = l.getBlockY();
                            int newz = l.getBlockZ();
                                                    World world = Sponge.getServer().getWorld("world").get();                        Location<World> nl1 = new Location<World>(world, newx, newy, newz);
                            r.setPos2(nl1);
                            Sponge.getCommandManager().process(player, "/expand "+value+" east");
                        }
                    }else if(position.equals("north") || position.equals("n")){
                        if(r.getPos1().getBlockZ()>r.getPos2().getBlockZ()){
                            Location<World> l = r.getPos2();
                            int newx = l.getBlockX();
                            int newy = l.getBlockY();
                            int newz = l.getBlockZ()-value;
                                                    World world = Sponge.getServer().getWorld("world").get();                        Location<World> nl1 = new Location<World>(world, newx, newy, newz);
                            r.setPos2(nl1);
                            Sponge.getCommandManager().process(player, "/expand "+value+" north");
                        }else{
                            Location<World> l = r.getPos1();
                            int newx = l.getBlockX();
                            int newy = l.getBlockY();
                            int newz = l.getBlockZ()-value;
                                                    World world = Sponge.getServer().getWorld("world").get();                        Location<World> nl1 = new Location<World>(world, newx, newy, newz);
                            r.setPos1(nl1);
                            Sponge.getCommandManager().process(player, "/expand "+value+" north");
                        }
                    }else if(position.equals("south") || position.equals("s")){
                        if(r.getPos1().getBlockZ()>r.getPos2().getBlockZ()){
                            Location<World> l = r.getPos1();
                            int newx = l.getBlockX();
                            int newy = l.getBlockY();
                            int newz = l.getBlockZ()+value;
                                                    World world = Sponge.getServer().getWorld("world").get();                        Location<World> nl1 = new Location<World>(world, newx, newy, newz);
                            r.setPos1(nl1);
                            Sponge.getCommandManager().process(player, "/expand "+value+" south");
                        }else{
                            Location<World> l = r.getPos2();
                            int newx = l.getBlockX();
                            int newy = l.getBlockY();
                            int newz = l.getBlockZ()+value;
                                                    World world = Sponge.getServer().getWorld("world").get();                        Location<World> nl1 = new Location<World>(world, newx, newy, newz);
                            r.setPos2(nl1);
                            Sponge.getCommandManager().process(player, "/expand "+value+" south");   
                        }
                    }
                    
                }
            }else{
                Utils.sendMessage(player, TextColors.RED,"[Игровая Сторона] ", TextColors.WHITE, "Вы не выделили ни одного региона.");
                return CommandResult.success();
            }
        }
        return CommandResult.success();
    }
    
}
