package redart15.commandly;

import net.fabricmc.api.DedicatedServerModInitializer;
import redart15.commandly.command.CommandlyCommands;

import static redart15.commandly.CommandlyMod.LOGGER;

public class CommandlyServer implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		CommandlyCommands.registerServerCommands();
		LOGGER.info("Commandly server initialized.");
	}
}
