package redart15.commandly;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.spongepowered.asm.mixin.Mixins;

import static redart15.commandly.CommandlyMod.LOGGER;

public class CommandlyPreLaunch implements PreLaunchEntrypoint {

	@Override
	public void onPreLaunch() {
		CommandlyConfig.init();
		// need to be present before any mixins are done
		if(CommandlyConfig.SMART_VEINMINER){
			Mixins.addConfiguration("addon/veinminer.mixin.json");
			LOGGER.info("Smart veinminer loaded.");
		}
		if(CommandlyConfig.SMART_TREECAPITATOR){
			Mixins.addConfiguration("addon/treecapitator.mixin.json");
			LOGGER.info("Smart treecapitator loaded.");
		}
		LOGGER.info("Commandly MixinPlugin initialized.");
	}
}
