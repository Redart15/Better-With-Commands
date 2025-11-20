package redart15.commandly;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.data.gamerule.GameRuleBoolean;
import net.minecraft.core.data.gamerule.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redart15.commandly.veincapitator.OreGroups;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

public class CommandlyMod implements ModInitializer, RecipeEntrypoint, GameStartEntrypoint {
	public static final String MOD_ID = "commandly";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final int MASK = 7;
	public static final int MAX_BLOCK_COUNT = 256 * 16 * 16 * 25;
	public static GameRuleBoolean MOSS_SPREADING = GameRules.register(new GameRuleBoolean("doMossSpreading", true));
	public static GameRuleBoolean GRASS_SPREADING = GameRules.register(new GameRuleBoolean("doGrassSpreading", true));
	public static GameRuleBoolean VEIN_MINING = GameRules.register(new GameRuleBoolean("veinmining", false));
	@Override
	public void onInitialize() {
		OreGroups.init();
		LOGGER.info("Commandly initialized");
	}

	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {

	}

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}
}
