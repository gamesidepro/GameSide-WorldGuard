package com.universeguard;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.universeguard.commands.RegionAddMemberExecutor;
import com.universeguard.commands.RegionAddOwnerExecutor;
import com.universeguard.commands.RegionCommandExecutor;
import com.universeguard.commands.RegionClaimExecutor;
import com.universeguard.commands.RegionExpandExecutor;
import com.universeguard.commands.RegionDeleteExecutor;
import com.universeguard.commands.RegionEditExecutor;
import com.universeguard.commands.RegionExecutor;
import com.universeguard.commands.RegionFlagExecutor;
import com.universeguard.commands.RegionFlagHelpExecutor;
import com.universeguard.commands.RegionGamemodeExecutor;
import com.universeguard.commands.RegionGlobalExecutor;
import com.universeguard.commands.RegionGlobalListExecutor;
import com.universeguard.commands.RegionHelpExecutor;
import com.universeguard.commands.RegionInfoExecutor;
import com.universeguard.commands.RegionListExecutor;
import com.universeguard.commands.RegionNameExecutor;
import com.universeguard.commands.RegionPriorityExecutor;
import com.universeguard.commands.RegionRemoveMemberExecutor;
import com.universeguard.commands.RegionRemoveOwnerExecutor;
import com.universeguard.commands.RegionSaveExecutor;
import com.universeguard.commands.RegionSetPos1;
import com.universeguard.commands.RegionSetPos2;
import com.universeguard.commands.RegionSetSpawnExecutor;
import com.universeguard.commands.RegionSetTeleportExecutor;
import com.universeguard.commands.RegionSpawnExecutor;
import com.universeguard.commands.RegionTeleportExecutor;
import com.universeguard.config.ConfigurationManager;
import com.universeguard.events.EventLeftClick;
import com.universeguard.events.EventRightClick;
import com.universeguard.events.flags.EventBlockBreak;
import com.universeguard.events.flags.EventBlockExplosion;
import com.universeguard.events.flags.EventBlockPlace;
import com.universeguard.events.flags.EventBlockUse;
import com.universeguard.events.flags.EventChat;
import com.universeguard.events.flags.EventCommandSend;
import com.universeguard.events.flags.EventDecay;
import com.universeguard.events.flags.EventExperienceDrop;
import com.universeguard.events.flags.EventFlow;
import com.universeguard.events.flags.EventHunger;
import com.universeguard.events.flags.EventItemDrop;
import com.universeguard.events.flags.EventItemUse;
import com.universeguard.events.flags.EventPvp;
import com.universeguard.events.flags.EventSetGamemode;
import com.universeguard.events.flags.EventSleep;
import com.universeguard.events.flags.EventSpawnEnderPearl;
import com.universeguard.events.flags.EventSpawnEntity;
import com.universeguard.events.flags.EventSpawnItem;
import com.universeguard.events.flags.EventSpawnPotion;
import com.universeguard.events.flags.EventSpawnVehicle;
import com.universeguard.region.Region;
import com.universeguard.utils.RegionUtils;
import com.universeguard.utils.Utils;
import java.util.ArrayList;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "universeguard", name = "UniverseGuard", version = "1.2", description="A World Guard solution for Sponge", authors = "Twelvee")
public class UniverseGuard {
        //PlayerInfo pl;
	public HashMap<Player, Region> pendings = new HashMap<Player, Region>();
        public HashMap<Player, Integer> traders = new HashMap<Player, Integer>();
	public static UniverseGuard instance;
	public static Region r;
        public ArrayList<Region> regions = new ArrayList<Region>();
        
        
        
	@Inject
	Game game;
	
	@Inject
	@DefaultConfig(sharedRoot = false)
	private File configFile;

	@Inject
	@DefaultConfig(sharedRoot = false)
	private ConfigurationLoader<CommentedConfigurationNode> configManager;

	@Inject
	private Logger logger;


	public File getConfigFile() {
		return configFile;
	}

	public ConfigurationLoader<CommentedConfigurationNode> getConfigManager() {
		return configManager;
	}

	public Logger getLogger() {
		return logger;
	}

	@Listener
	public void onPreInit(GamePreInitializationEvent event) {
	    instance = this;
	    logger.info("Loading GameSide World Guard...");
	}
        
	@Listener
	public void onInit(GameInitializationEvent event) {
		ConfigurationManager.getInstance().setup(configFile, configManager);

		CommandSpec regionInfoSpec = CommandSpec.builder().description(Text.of("Информация о регионе"))
				.executor(new RegionInfoExecutor()).
				arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))))
				.build();
		
		CommandSpec regionEditSpec = CommandSpec.builder().description(Text.of("Редактирование региона"))
				.executor(new RegionEditExecutor()).
				arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))))
				.permission(Utils.getPermission("permission"))
				.build();
		
		CommandSpec regionDeleteSpec = CommandSpec.builder().description(Text.of("Удаление региона"))
				.executor(new RegionDeleteExecutor()).
				arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))))
				.permission(Utils.getPermission("userperm"))
				.build();
		
		CommandSpec regionNameSpec = CommandSpec.builder().description(Text.of("Переименование региона"))
				.executor(new RegionNameExecutor()).
				arguments(GenericArguments.remainingJoinedStrings(Text.of("name")))
				.permission(Utils.getPermission("permission"))
				.build();

		CommandSpec regionExpandAction = CommandSpec.builder().description(Text.of("Расширить регион"))
				.executor(new RegionExpandExecutor()).
				arguments(GenericArguments.integer(Text.of("value")), GenericArguments.string(Text.of("position")))
				.permission(Utils.getPermission("userperm"))
				.build();
                
		CommandSpec regionPrioritySpec = CommandSpec.builder().description(Text.of("Установить приоритет региона"))
				.executor(new RegionPriorityExecutor()).
				arguments(GenericArguments.integer(Text.of("value")))
				.permission(Utils.getPermission("permission"))
				.build();
		
		CommandSpec regionFlagSpec = CommandSpec.builder().description(Text.of("Установить флаг в регионе"))
				.executor(new RegionFlagExecutor()).
				arguments(GenericArguments.string(Text.of("flag")), GenericArguments.bool(Text.of("value")))
				.permission(Utils.getPermission("permission"))
				.build();
		
		CommandSpec regionGlobalSpec = CommandSpec.builder().description(Text.of("Установить глобальный флаг"))
				.executor(new RegionGlobalExecutor()).
				arguments(GenericArguments.string(Text.of("flag")), GenericArguments.bool(Text.of("value")))
				.permission(Utils.getPermission("permission"))
				.build();
		
		CommandSpec regionSetTeleportSpec = CommandSpec.builder().description(Text.of("Установить точку телепортации"))
				.executor(new RegionSetTeleportExecutor()).
				arguments(GenericArguments.integer(Text.of("locX")),
						GenericArguments.integer(Text.of("locY")),
						GenericArguments.integer(Text.of("locZ")))
				.permission(Utils.getPermission("permission"))
				.build();
		
		CommandSpec regionSetSpawnSpec = CommandSpec.builder().description(Text.of("Установить точку возрождения"))
				.executor(new RegionSetSpawnExecutor()).
				arguments(GenericArguments.integer(Text.of("locX")),
						GenericArguments.integer(Text.of("locY")),
						GenericArguments.integer(Text.of("locZ")))
				.permission(Utils.getPermission("permission"))
				.build();
		
		CommandSpec regionGamemodeSpec = CommandSpec.builder().description(Text.of("Установить режим игры"))
				.executor(new RegionGamemodeExecutor()).
				arguments(GenericArguments.string(Text.of("gamemode")))
				.permission(Utils.getPermission("permission"))
				.build();
		
		CommandSpec regionSetPos1 = CommandSpec.builder().description(Text.of("Первая точка в регионе"))
				.permission(Utils.getPermission("userperm"))
				.executor(new RegionSetPos1()).build();
                
		CommandSpec regionSetPos2 = CommandSpec.builder().description(Text.of("Вторая точка в регионе"))
				.permission(Utils.getPermission("userperm"))
				.executor(new RegionSetPos2()).build();
                
		CommandSpec regionSaveSpec = CommandSpec.builder().description(Text.of("Сохранить регион"))
				.permission(Utils.getPermission("permission"))
				.executor(new RegionSaveExecutor()).build();
                
		CommandSpec regionClaim = CommandSpec.builder().description(Text.of("Создать регион"))
				.permission(Utils.getPermission("userperm"))
				.arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))))
				.executor(new RegionClaimExecutor()).build();
                
		CommandSpec regionGlobalListSpec = CommandSpec.builder().description(Text.of("Отобразить глобальные флаги"))
				.permission(Utils.getPermission("permission"))
				.executor(new RegionGlobalListExecutor()).build();
		
		CommandSpec regionListSpec = CommandSpec.builder().description(Text.of("Лист всех регионов"))
                                .permission(Utils.getPermission("permission"))
				.executor(new RegionListExecutor()).build();
		
		CommandSpec regionAddOwnerSpec = CommandSpec.builder().description(Text.of("Добавить владельца в регион"))
				.permission(Utils.getPermission("userperm"))
				.executor(new RegionAddOwnerExecutor()).
				arguments(GenericArguments.player(Text.of("name")),GenericArguments.optional(GenericArguments.string(Text.of("region"))))
				.build();
		
		CommandSpec regionAddMemberSpec = CommandSpec.builder().description(Text.of("Добавить жителя в регион"))
				.permission(Utils.getPermission("userperm"))
				.executor(new RegionAddMemberExecutor()).
				arguments(GenericArguments.player(Text.of("name")),GenericArguments.optional(GenericArguments.string(Text.of("region"))))
				.build();
                
		CommandSpec regionRemoveMemberSpec = CommandSpec.builder().description(Text.of("Удалить жителя из региона"))
				.permission(Utils.getPermission("userperm"))
				.executor(new RegionRemoveMemberExecutor()).
				arguments(GenericArguments.player(Text.of("name")),GenericArguments.optional(GenericArguments.string(Text.of("region"))))
				.build();
                
		CommandSpec regionRemoveOwnerSpec = CommandSpec.builder().description(Text.of("Удалить владельца региона"))
				.permission(Utils.getPermission("userperm"))
				.executor(new RegionRemoveOwnerExecutor()).
				arguments(GenericArguments.player(Text.of("name")), GenericArguments.optional(GenericArguments.string(Text.of("region"))))
				.build();
                
		CommandSpec regionTeleportSpec = CommandSpec.builder().description(Text.of("Телепортироваться в точку телепорта"))
				.executor(new RegionTeleportExecutor()).
				arguments(GenericArguments.string(Text.of("name")))
                                .permission(Utils.getPermission("permission"))
				.build();
		
		CommandSpec regionSpawnSpec = CommandSpec.builder().description(Text.of("Телепортироваться в точку спавна"))
				.executor(new RegionSpawnExecutor()).
				arguments(GenericArguments.string(Text.of("name")))
                                .permission(Utils.getPermission("permission"))
				.build();
		
		CommandSpec regionCmdSpec = CommandSpec.builder().description(Text.of("Включить или выключить использование команд в регионе"))
				.executor(new RegionCommandExecutor()).
				arguments(GenericArguments.string(Text.of("name")),GenericArguments.optional(GenericArguments.string(Text.of("region"))))
				.permission(Utils.getPermission("permission"))
				.build();
		
		CommandSpec regionHelpSpec = CommandSpec.builder().description(Text.of("Помощь"))
				.executor(new RegionHelpExecutor())
				.arguments(GenericArguments.optional(GenericArguments.integer(Text.of("page"))))
				.build();
		
		CommandSpec regionFlagHelpSpec = CommandSpec.builder().description(Text.of("Флаги"))
				.executor(new RegionFlagHelpExecutor())
				.arguments(GenericArguments.optional(GenericArguments.integer(Text.of("page"))))
				.build();
		
		CommandSpec regionCommandSpec = CommandSpec.builder().description(Text.of("Команды плагина"))
				.executor(new RegionExecutor())
				.child(regionSaveSpec, "save")
                                .child(regionClaim, "claim")
				.child(regionInfoSpec, "info")
				.child(regionDeleteSpec, "delete")
				.child(regionNameSpec, "name")
				.child(regionListSpec, "list")
                                .child(regionExpandAction, "expand")
				.child(regionGamemodeSpec, "gamemode")
				.child(regionEditSpec, "edit")
				.child(regionFlagSpec, "flag")
				.child(regionAddOwnerSpec, "addowner")
                                .child(regionAddMemberSpec, "addmember")
                                .child(regionRemoveMemberSpec, "removemember")
				.child(regionRemoveOwnerSpec, "removeowner")
				.child(regionGlobalSpec, "global")
				.child(regionGlobalListSpec, "globallist")
				.child(regionSetTeleportSpec, "setteleport")
				.child(regionSetSpawnSpec, "setspawn")
                                .child(regionSetPos1, "pos1")
                                .child(regionSetPos2, "pos2")
				.child(regionTeleportSpec, "teleport")
				.child(regionSpawnSpec, "spawn")
				.child(regionPrioritySpec, "priority")
				.child(regionCmdSpec, "command")
				.child(regionHelpSpec, "help")
				.child(regionFlagHelpSpec, "flaghelp")
				.build();

		Sponge.getCommandManager().register(this, regionCommandSpec, Lists.newArrayList("region", "rg"));
		
		game.getEventManager().registerListeners(this, new EventLeftClick());
		game.getEventManager().registerListeners(this, new EventRightClick());
		
		game.getEventManager().registerListeners(this, new EventBlockPlace());
		game.getEventManager().registerListeners(this, new EventBlockBreak());
		game.getEventManager().registerListeners(this, new EventPvp());
		game.getEventManager().registerListeners(this, new EventBlockExplosion());
		game.getEventManager().registerListeners(this, new EventItemDrop());
		game.getEventManager().registerListeners(this, new EventExperienceDrop());
		game.getEventManager().registerListeners(this, new EventItemUse());
		game.getEventManager().registerListeners(this, new EventSpawnEnderPearl());
		game.getEventManager().registerListeners(this, new EventSleep());
		game.getEventManager().registerListeners(this, new EventBlockUse());
		game.getEventManager().registerListeners(this, new EventSpawnVehicle());
		game.getEventManager().registerListeners(this, new EventChat());
		//game.getEventManager().registerListeners(this, new EventFlow());
		game.getEventManager().registerListeners(this, new EventDecay());
		game.getEventManager().registerListeners(this, new EventSpawnItem());
		game.getEventManager().registerListeners(this, new EventCommandSend());
		game.getEventManager().registerListeners(this, new EventSpawnPotion());
		game.getEventManager().registerListeners(this, new EventSpawnEntity());
                
		
		
		logger.info("GameSide World Guard loaded!");
	}
	
	@Listener
	public void onStart(GameStartedServerEvent event) {
		RegionUtils.loadGlobalRegions();
                RegionUtils.getAllRegions(regions);
	}
}