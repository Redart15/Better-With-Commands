package redart15.commandly;

import net.fabricmc.api.DedicatedServerModInitializer;
import redart15.commandly.command.CommadlyCommands;

import static redart15.commandly.CommandlyMod.LOGGER;

public class CommandlyServer implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		CommadlyCommands.registerServerCommands();
		LOGGER.info("Commandly server initialized.");
	}
}
