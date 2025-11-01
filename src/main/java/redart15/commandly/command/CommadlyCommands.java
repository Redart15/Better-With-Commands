package redart15.commandly.command;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.net.command.CommandManager;

public class CommadlyCommands {


	@Environment(EnvType.CLIENT)
	public static void registerClientCommands() {
		CommandManager.registerCommand(new CommandGrow());
	}

	@Environment(EnvType.SERVER)
	public static void registerServerCommands() {
		CommandManager.registerServerCommand(new CommandGrow());
	}

}
