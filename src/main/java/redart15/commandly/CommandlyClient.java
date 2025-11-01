package redart15.commandly;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import redart15.commandly.command.CommandlyCommands;
import turniplabs.halplibe.util.ClientStartEntrypoint;

@Environment(EnvType.CLIENT)
public class CommandlyClient implements ClientModInitializer, ClientStartEntrypoint {
	@Override
	public void onInitializeClient() {
		CommandlyCommands.registerClientCommands();
	}

	@Override
	public void beforeClientStart() {

	}

	@Override
	public void afterClientStart() {

	}
}
