package redart15.commandly;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.data.gamerule.GameRuleBoolean;
import net.minecraft.core.data.gamerule.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redart15.commandly.veincapitator.OreGroups;
import turniplabs.halplibe.util.GameStartEntrypoint;

public class CommandlyMod implements GameStartEntrypoint, ModInitializer {
	public static final String MOD_ID = "commandly";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
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
}
