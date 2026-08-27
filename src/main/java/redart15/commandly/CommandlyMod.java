package redart15.commandly;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.core.data.gamerule.GameRuleBoolean;
import net.minecraft.core.data.gamerule.GameRules;
import org.slf4j.Logger;
import redart15.commandly.api.CommandlyEvent;
import redart15.commandly.api.CommandlyPlugin;
import redart15.commandly.veincapitator.OreGroups;
import redart15.commandly.veincapitator.PickAxeRegister;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.event.defs.CommonEvents;
import turniplabs.halplibe.util.dependency.Key;

public class CommandlyMod implements ModInitializer{
	public static final String MOD_ID = HalpLibe.registerMod(CommandlyGlobals.MOD_ID);
	public static final Logger LOGGER = CommandlyGlobals.LOGGER;
	private static final int MASK = 7;
	private static final int MAX_BLOCK_COUNT = 256 * 16 * 16 * 25;
	public static final GameRuleBoolean MOSS_SPREADING = GameRules.register(new GameRuleBoolean("doMossSpreading", "gamerule.do_moss_spread", true));
	public static final GameRuleBoolean GRASS_SPREADING = GameRules.register(new GameRuleBoolean("doGrassSpreading", "gamerule.do_grass_spread", true));
	public static final GameRuleBoolean VEINMINING = GameRules.register(new GameRuleBoolean("veinmining", "gamerule.veinmining", false));

	@Override
	public void onInitialize() {
		LOGGER.info("Commandly initialized");
		CommandlyConfig.init();
		CommonEvents.AFTER_GAME_START.listen(Key.of(MOD_ID), CommandlyMod::afterGameStart);
	}

	public static void afterGameStart() {
		LOGGER.info("Loading implementation");
		OreGroups.init();
		PickAxeRegister.init();
		FabricLoader.getInstance()
			.getEntrypointContainers("commandly", CommandlyPlugin.class)
			.forEach(CommandlyMod::initialize);
		CommandlyEvent.REGISTER_ORE.emit(ore -> ore.accept(OreGroups.getInstance()));
		CommandlyEvent.REGISTER_PICKAXE.emit(ore -> ore.accept(PickAxeRegister.getInstance()));
	}

	private static void initialize(EntrypointContainer<CommandlyPlugin> plugin) {
		CommandlyPlugin entrypoint = plugin.getEntrypoint();
		entrypoint.registerOreGroups(OreGroups.getInstance());
		entrypoint.registerPickaxe(PickAxeRegister.getInstance());
	}

	public static int getMask(){return MASK;}
	public static int getMaxBlockCount(){return MAX_BLOCK_COUNT;}
}
