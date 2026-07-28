package redart15.commandly;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import redart15.commandly.command.CommandlyCommands;

@Environment(EnvType.CLIENT)
public class CommandlyClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CommandlyCommands.registerClientCommands();
	}
}
