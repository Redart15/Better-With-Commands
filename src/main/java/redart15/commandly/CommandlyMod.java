package redart15.commandly;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.core.data.gamerule.GameRuleBoolean;
import net.minecraft.core.data.gamerule.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redart15.commandly.api.CommandlyPlugin;
import redart15.commandly.veincapitator.OreGroups;
import redart15.commandly.veincapitator.PickAxeRegister;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

public class CommandlyMod implements ModInitializer, RecipeEntrypoint, GameStartEntrypoint {
	public static final String MOD_ID = "commandly";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final int MASK = 7;
	public static final int MAX_BLOCK_COUNT = 256 * 16 * 16 * 25;
	public static final GameRuleBoolean MOSS_SPREADING = GameRules.register(new GameRuleBoolean("doMossSpreading", true));
	public static final GameRuleBoolean GRASS_SPREADING = GameRules.register(new GameRuleBoolean("doGrassSpreading", true));
	public static final GameRuleBoolean VEINMINING = GameRules.register(new GameRuleBoolean("veinmining", false));

	@Override
	public void onInitialize() {
		LOGGER.info("Commandly initialized");
		// no need
	}

	@Override
	public void beforeGameStart() {
		// no need
	}

	@Override
	public void afterGameStart() {
		LOGGER.info("Loading implementation");
		OreGroups.init();
		PickAxeRegister.init();
		FabricLoader.getInstance()
			.getEntrypointContainers("commandly", CommandlyPlugin.class)
			.forEach(CommandlyMod::initialize);

	}

	private static void initialize(EntrypointContainer<CommandlyPlugin> plugin) {
		CommandlyPlugin entrypoint = plugin.getEntrypoint();
		entrypoint.registerOreGroups(OreGroups.getInstance());
		entrypoint.registerPickaxe(PickAxeRegister.getInstance());
	}

	@Override
	public void onRecipesReady() {
		// no need
	}

	@Override
	public void initNamespaces() {
		// no need
	}
}
